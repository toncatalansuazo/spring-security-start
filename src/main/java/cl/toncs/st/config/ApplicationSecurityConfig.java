package cl.toncs.st.config;


import cl.toncs.st.filter.JwtAuthenticationFilter;
import cl.toncs.st.filter.JwtAuthorizationFilter;
import cl.toncs.st.util.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.validation.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {
    public static final String LOGIN_URL = "/api/v1/login";
    public static final String REFRESH_TOKEN_URL = "/api/v1/refreshtoken";

    @Primary
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil, Validator validator, ObjectMapper objectMapper) throws Exception {
        var authorizationFilter = new JwtAuthorizationFilter(jwtUtil);
        var authenticationFilter = new JwtAuthenticationFilter(jwtUtil, authenticationConfiguration.getAuthenticationManager(), validator, objectMapper);
        authenticationFilter.setFilterProcessesUrl(LOGIN_URL);

        http.cors()
            .and()
            // dont use csrf token
            .csrf()
            .disable()
            .sessionManagement()
            // dont save nothing in session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .antMatchers(LOGIN_URL, "/swagger-ui.html", "/swagger-ui", "/*.js", "/*.css", "/resources/**")
            .permitAll()
            .antMatchers("/api/**")
            .authenticated()
            .and()
            .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
            // implement jwt filter, that will create jwt token
            .addFilter(authenticationFilter);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }



}
