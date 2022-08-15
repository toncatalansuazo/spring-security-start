package cl.toncs.st.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cl.toncs.st.config.ApplicationSecurityConfig;
import cl.toncs.st.service.user.UserService;
import cl.toncs.st.util.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AuthenticationController {

    private final UserService userDetailsService;

    private JwtUtil jwtUtil;

    @GetMapping(value = ApplicationSecurityConfig.REFRESH_TOKEN_URL)
    public void refreshToken(@RequestHeader("refresh_token") String refreshToken, HttpServletResponse response)
        throws AuthenticationException, IOException {
        boolean isValid = this.jwtUtil.validateToken(refreshToken);
        if (isValid) {
            var username = this.jwtUtil.getUsernameFromToken(refreshToken);
            var user = this.userDetailsService.loadUserByUsername(username);
            var tokens = this.jwtUtil.generateTokens(user);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } else {
            throw new BadCredentialsException("refresh token not valid");
        }
    }

}
