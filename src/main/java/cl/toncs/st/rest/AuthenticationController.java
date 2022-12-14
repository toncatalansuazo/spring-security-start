package cl.toncs.st.rest;

import cl.toncs.st.config.ApplicationSecurityConfig;
import cl.toncs.st.service.auth.AuthService;
import cl.toncs.st.util.security.JwtUtil;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @GetMapping(value = ApplicationSecurityConfig.REFRESH_TOKEN_URL)
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestHeader("refresh_token") String refreshToken, HttpServletResponse response)
        throws AuthenticationException {
        var tokens = authService.generateTokens(refreshToken);
        return ResponseEntity.ok(tokens);
    }

}
