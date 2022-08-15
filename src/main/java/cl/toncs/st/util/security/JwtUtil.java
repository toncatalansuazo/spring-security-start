package cl.toncs.st.util.security;

import cl.toncs.st.domain.user.DentalUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private String secret;
    private int jwtExpirationInMinutes;
    private int refreshExpirationDateInMinutes;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMinutes}")
    public void setJwtExpirationInMinutes(int jwtExpirationInMinutes) {
        this.jwtExpirationInMinutes = jwtExpirationInMinutes;
    }

    @Value("${jwt.refreshExpirationDateInMinutes}")
    public void setRefreshExpirationDateInMinutes(int refreshExpirationDateInMinutes) {
        this.refreshExpirationDateInMinutes = refreshExpirationDateInMinutes;
    }

    public Map<String, Object> generateTokens(DentalUserDetails user) {
        var accessToken = this.generateToken(user);
        var refreshToken = this.generateRefreshToken(user.getEmail());
        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
    }

    public String generateToken(DentalUserDetails userDetails) {

        var roles = userDetails.getAuthorities().stream()
                               .map(role -> role.getAuthority())
                               .toList();
        var claims = Map.of("roles", roles);

        return doGenerateToken(claims, userDetails.getEmail());
    }

    public String doGenerateToken(Map<String, List<String>> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(Date.from(Instant.now()))
                   .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * jwtExpirationInMinutes)))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();

    }

    public String generateRefreshToken(String subject) {
        return Jwts.builder()
                   .setSubject(subject)
                   .setIssuedAt(Date.from(Instant.now()))
                   .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * refreshExpirationDateInMinutes)))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();

    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
        } catch (ExpiredJwtException ex) {
            throw ex;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        List<String> roles = claims.get("roles", List.class);

        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

    }

    class ClaimsToken {
        List<String> authority;
    }
}
