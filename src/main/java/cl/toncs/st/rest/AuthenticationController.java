package cl.toncs.st.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cl.toncs.st.config.ApplicationSecurityConfig;
import cl.toncs.st.service.auth.AuthService;
import cl.toncs.st.service.user.UserService;
import cl.toncs.st.util.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
    public ResponseEntity<Map> refreshToken(@RequestHeader("refresh_token") String refreshToken, HttpServletResponse response)
        throws AuthenticationException {
        var tokens = authService.generateTokens(refreshToken);
        return ResponseEntity.ok(tokens);
    }

}
