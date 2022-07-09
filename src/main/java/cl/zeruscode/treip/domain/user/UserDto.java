package cl.zeruscode.treip.domain.user;

import cl.zeruscode.treip.entities.user.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
public class UserDto {

    private String password;

    private String username;

    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDto(@JsonProperty(value = "password") String password,
                   @JsonProperty(value = "username") String username,
                   @JsonProperty(value = "email") String email,
                   @JsonProperty(value = "email") List<RoleType> roles) {
        this.password = password;
        this.username = username;
        this.email = email;
        this.authorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority(role.name()))
                                .collect(Collectors.toList());
    }
}
