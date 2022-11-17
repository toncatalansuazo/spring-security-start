package cl.toncs.st.filter;

import cl.toncs.st.config.ApplicationSecurityConfig;
import cl.toncs.st.util.security.JwtUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenUtil;

    //
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        if (request.getServletPath().equals(ApplicationSecurityConfig.LOGIN_URL)) {
            try {
                chain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                return;
            } catch (Exception e) {
                log.error("error login user = " + e.getMessage());
                throw new ServletException(e.getMessage());
            }
        } else {
            String jwtToken = extractJwtFromRequest(request);

            if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)) {
                UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromToken(jwtToken), "", jwtTokenUtil.getRolesFromToken(jwtToken));

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                                                                                                                                  userDetails.getAuthorities());
                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the
                // Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                log.warn("Cannot set the Security Context");
            }
            chain.doFilter(request, response);
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
