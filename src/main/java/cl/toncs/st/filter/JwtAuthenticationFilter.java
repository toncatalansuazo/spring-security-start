package cl.toncs.st.filter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import cl.toncs.st.domain.error.ErrorModel;
import cl.toncs.st.domain.user.DentalUserDetails;
import cl.toncs.st.domain.user.DentalUserLogin;
import cl.toncs.st.util.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@AllArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    private final Validator validator;

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var user = extracted(request);
        var violations = validator.validate(user);
        if (!violations.isEmpty()) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(400);
            ErrorModel error = toErrorJsonResponseError(violations);
            objectMapper.writeValue(response.getOutputStream(), error);
            throw new IllegalArgumentException("Invalid request body");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        return authenticationManager.authenticate(authenticationToken);
    }

    private ErrorModel toErrorJsonResponseError(Set<ConstraintViolation<DentalUserLogin>> violations) {
        var details = violations.stream().map(v -> "Field `" + v.getPropertyPath() + "`: " + v.getMessage()).collect(Collectors.joining("\n. "));
        return new ErrorModel(HttpStatus.BAD_REQUEST, "invalid request body", details);
    }

    private DentalUserLogin extracted(HttpServletRequest request) throws AuthenticationException {
        try {
            return new ObjectMapper().readValue(request.getInputStream().readAllBytes(), DentalUserLogin.class);
        } catch (IOException e) {
            throw new RuntimeException("error on login");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException {
        DentalUserDetails user = (DentalUserDetails)authResult.getPrincipal();
        var tokens = jwtUtil.generateTokens(user);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

}
