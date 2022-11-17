package cl.toncs.st.service.auth;

import cl.toncs.st.service.user.UserService;
import cl.toncs.st.util.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserService userDetailsService;
    private final JwtUtil jwtUtil;

    @Nonnull
    public Map<String, Object> generateTokens(@Nonnull String refreshToken) {
        var username = this.jwtUtil.getUsernameFromToken(refreshToken);
        var user = this.userDetailsService.loadUserByUsername(username);
        return this.jwtUtil.generateTokens(user);
    }
}
