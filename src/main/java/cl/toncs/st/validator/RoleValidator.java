package cl.toncs.st.validator;

import cl.toncs.st.entities.user.RoleType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class RoleValidator implements ConstraintValidator<RoleValidation, Set<SimpleGrantedAuthority>> {

    @Override
    public boolean isValid(Set<SimpleGrantedAuthority> grantedAuthorities, ConstraintValidatorContext constraintValidatorContext) {
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) {
            return false;
        }
        var roles = Arrays.stream(RoleType.values()).map(RoleType::name).toList();
        var rolesInRequest = new ArrayList<>(grantedAuthorities);
        for (var role: rolesInRequest) {
            if (!roles.contains(role.toString())) {
                return false;
            }
        }
        return true;
    }
}
