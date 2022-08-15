package cl.toncs.st.domain.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class DentalUserLogin {

    @NotEmpty
    @Size(min= 5)
    private String password;

    @Email(regexp = "[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+")
    @NotEmpty
    private String email;

}
