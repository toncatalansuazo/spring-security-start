package cl.toncs.st.rest.v1;

import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.ALL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

import cl.toncs.st.domain.user.DentalUser;
import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.repository.RoleRepository;
import cl.toncs.st.repository.UserRepository;
import cl.toncs.st.SpringSecurityStart;
import cl.toncs.st.config.ApplicationSecurityConfig;
import cl.toncs.st.rest.BaseTestContainer;
import cl.toncs.st.util.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringSecurityStart.class)
@ExtendWith(SpringExtension.class)
class UserControllerIntegrationTest extends BaseTestContainer {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void given_request_without_access_token_then_dail() {
        var body = """
             {
                "email":"some-email@gmail.com",
                "authorities":[{"authority":"ROLE_USER"}],
                "password":"password"
             }
            """;

        given()
                .accept(ContentType.JSON)
                .port(port)
                .contentType("application/json")
                .body(body)
                .when()
                .get("/api/v1/users")
                .then()
                .log()
                .ifValidationFails(ALL)
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }
    @Test
    void given_user_json_when_json_is_valid_then_create_user() {
        var body = """
             {
                "email":"some-email@gmail.com",
                "authorities":[{"authority":"ROLE_USER"}],
                "password":"password"
             }
            """;

        given()
            .accept(ContentType.JSON)
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .body(body)
            .when()
            .post("/api/v1/users")
            .then()
            .log()
            .ifValidationFails(ALL)
            .statusCode(OK.value())
            .body("email", Matchers.equalTo("some-email@gmail.com"))
            .body("authorities", Matchers.hasSize(1))
            .body("accountNonExpired", Matchers.is(true))
            .body("accountNonLocked", Matchers.is(true))
            .body("credentialsNonExpired", Matchers.is(true))
            .body("enabled", Matchers.is(true))
            .body("id", Matchers.notNullValue());
    }

    @Test
    void given_user_json_with_invalid_email_should_return_error() {
        // given bad email
        var body = """
             {
                "email":"some-email@gmail",
                "authorities":[{"authority":"ROLE_USER"}],
                "password":"password"
             }
            """;

        // when save user with invalid email
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .body(body)
            .when()
            .post("/api/v1/users")
            .then()
            .log()
            .ifValidationFails(ALL)
            // should return error
            .statusCode(BAD_REQUEST.value())
            .body("httpStatus", Matchers.equalTo(BAD_REQUEST.name()))
            .body("message", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.equalTo("Wrong field email: must be a well-formed email address. Value in request = some-email@gmail."));
    }

    @Test
    void given_user_json_with_empties_authorities_should_return_error() {
        // given bad email
        var body = """
             {
                "email":"some-email@gmail.com",
                "authorities":[],
                "password":"password"
             }
            """;

        // when save user without authorities
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .body(body)
            .when()
            .post("/api/v1/users")
            .then()
            .log()
            .ifValidationFails(ALL)
        // should return error with proper description
            .statusCode(BAD_REQUEST.value())
            .body("httpStatus", Matchers.equalTo(BAD_REQUEST.name()))
            .body("message", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.equalTo("Wrong field authorities: Invalid ROLE. Value in request = []. (through reference chain: cl.toncs.st.domain.user.DentalUser[\"authorities\"])"));
    }

    @Test
    void given_user_json_with_empty_password_should_return_error() {
        // given empty password
        var body = """
             {
                "email":"some-email@gmail.com",
                "authorities":[{"authority":"ROLE_USER"}],
                "password":""
             }
            """;

        // when save user without password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .body(body)
            .when()
            .post("/api/v1/users")
            .then()
            .log()
            .ifValidationFails(ALL)
        // should return error with proper description
            .statusCode(BAD_REQUEST.value())
            .body("httpStatus", Matchers.equalTo(BAD_REQUEST.name()))
            .body("message", Matchers.equalTo("Validation Error"));
    }

    @Test
    void given_user_json_with_wrong_authority_should_return_error() {
        // given empty password
        var body = """
             {
                "email":"some-email@gmail.com",
                "authorities":[{"authority":"ROLE_USER_XXX"}],
                "password":"some-password"
             }
            """;

        // when save user without password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .body(body)
            .when()
            .post("/api/v1/users")
            .then()
            .log()
            .ifValidationFails(ALL)
        // should return error with proper description
            .statusCode(BAD_REQUEST.value())
            .body("httpStatus", Matchers.equalTo(BAD_REQUEST.name()))
            .body("message", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.equalTo("Wrong field authorities: Invalid ROLE. Value in request = [ROLE_USER_XXX]. (through reference chain: cl.toncs.st.domain.user.DentalUser[\"authorities\"])"));
    }

    @Test
    void when_delete_user_with_permission_should_delete_user() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // when try to delete it with admin role
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_ADMIN)))
            .when()
            .delete("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
        // should be deleted
            .statusCode(OK.value());
    }

    @Test
    void when_delete_user_without_permission_should_reject_request() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // when try to delete it with admin role
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .when()
            .delete("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
        // should be deleted
            .statusCode(FORBIDDEN.value());
    }

    @Test
    void when_update_user_with_unknow_parameter() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "email":"new-email@gmail.com",
                "password":"new-password",
                "name": "new-name",
                "authorities":[{"authority":"ROLE_USER_XXX"}]
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
        // user should be update
            .statusCode(BAD_REQUEST.value())
            .body("message", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.equalTo("Unrecognized field \"name\" (class cl.toncs.st.domain.user.PatchDentalUser), not marked as ignorable (4 known properties: \"authorities\", \"password\", \"email\", \"username\"])\n at [Source: (org.springframework.util.StreamUtils$NonClosingInputStream); line: 4, column: 14] (through reference chain: cl.toncs.st.domain.user.PatchDentalUser[\"name\"])"));
    }

    @Test
    void when_update_user_with_invalid_role() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "email":"new-email@gmail.com",
                "password":"new-password",
                "username": "new-name",
                "authorities":[{"authority":"ROLE_USER_XXX"}]
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(BAD_REQUEST.value())
            .body("message  ", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.equalTo("Wrong field authorities: Invalid ROLE. Value in request = [ROLE_USER_XXX]. (through reference chain: cl.toncs.st.domain.user.PatchDentalUser[\"authorities\"])"));
    }

    @Test
    void when_update_user_name() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "username": "new-name"
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(OK.value())
            .body("username", Matchers.equalTo("new-name"))
            .body("email", Matchers.equalTo(user.getEmail()));
    }

    @Test
    void when_update_user_email() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "email": "someemail@gmail.com"
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(OK.value())
            .body("email", Matchers.equalTo("someemail@gmail.com"));
    }

    @Test
    void when_update_user_password() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "password": "someemail@gmail.com"
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(OK.value())
            .body("email", Matchers.equalTo("some@email.cl"));
    }

    @Test
    void when_update_user_with_invalid_json() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var body = """
             {
                "email":"",
                "password":"new",
                "username": "new-name",
                "authorities":[{"authority":"ROLE_USER"}]
             }
            """;

        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", authorize(List.of(RoleType.ROLE_USER)))
            .and()
            .body(body)
            .when()
            .patch("/api/v1/users/" + user.getId())
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(HttpStatus.SC_BAD_REQUEST)
            .body("message  ", Matchers.equalTo("Validation Error"))
            .body("details", Matchers.containsString("Wrong field password"))
            .body("details", Matchers.containsString("Wrong field email"));
    }

    @Test
    void when_refresh_token_get_new_tokens() {
        // given user
        UserEntity user = new UserEntity("username", "some@email.cl", "password", List.of(roleRepository.findAll().get(0)));
        userRepository.save(user);

        // given new email, name and password
        var tokens = getTokens(List.of(RoleType.ROLE_USER));
        var refreshToken = tokens.get("refresh_token");
        var accessToken = tokens.get("access_token");
        // when update username, email and password
        given()
            .port(port)
            .contentType("application/json")
            .header("Authorization", "Bearer " + accessToken)
            .and()
            .header("refresh_token", refreshToken)
            .when()
            .get(ApplicationSecurityConfig.REFRESH_TOKEN_URL)
            .then()
            .log()
            .ifValidationFails(ALL)
            // user should be update
            .statusCode(HttpStatus.SC_OK)
            .body("refresh_token", Matchers.notNullValue())
            .body("access_token", Matchers.notNullValue());
    }

    public String authorize(List<RoleType> roles) {
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                                                 .map(role -> new SimpleGrantedAuthority(role.name()))
                                                 .collect(Collectors.toSet());
        var user = new DentalUser("changePassword", "superadmin@dentalprofit.it", authorities);
        var token = this.jwtUtil.generateToken(user);
        return "Bearer " +  token;
    }

    public Map<String, Object> getTokens(List<RoleType> roles) {
        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
        var user = new DentalUser("changePassword", "superadmin@dentalprofit.it", authorities);
        return this.jwtUtil.generateTokens(user);
    }


}