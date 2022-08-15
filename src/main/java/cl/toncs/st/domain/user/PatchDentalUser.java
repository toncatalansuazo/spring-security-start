package cl.toncs.st.domain.user;

import cl.toncs.st.domain.auth.CustomAuthorityDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
public class PatchDentalUser {

    @Nullable
    @Size(min= 5)
    private String password;

    @Nullable
    @Size(min= 5)
    private String username;

    @Email(regexp = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")
    @Nullable
    private String email;

    @Nullable
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    private Set<SimpleGrantedAuthority> authorities;
}
