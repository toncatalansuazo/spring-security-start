package cl.toncs.st.domain.user;

import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.validator.RoleValidation;
import cl.toncs.st.domain.auth.CustomAuthorityDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
public class DentalUser implements DentalUserDetails {

    @Nullable
    private Long id;

    @Getter(onMethod_=@JsonIgnore)
    @NotEmpty
    @Size(min= 5)
    private String password;

    @Nullable
    private String username;

    @Email(regexp = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")
    @NotEmpty
    private String email;

    @RoleValidation
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    private Set<SimpleGrantedAuthority> authorities;

    @Nullable
    private boolean accountNonExpired;

    @Nullable
    private boolean accountNonLocked;

    @Nullable
    private boolean credentialsNonExpired;

    @Nullable
    private boolean enabled;

    @JsonCreator
    public DentalUser(@JsonProperty(value = "password") @NotEmpty String password,
                      @JsonProperty(value = "email") @Email @NotEmpty String email,
                      @JsonProperty(value = "authorities") @NotNull Set<SimpleGrantedAuthority> roles) {
        this.password = password;
        this.username = email;
        this.email = email;
        this.authorities = roles;
    }
    public DentalUser(UserEntity user) {
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                               .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                               .collect(Collectors.toSet());
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.accountNonExpired = !user.isAccountExpired();
        this.credentialsNonExpired = !user.isCredentialsExpired();
        this.enabled = user.isEnabled();
        this.accountNonLocked = !user.isLocked();
        this.id = user.getId();
    }
}
