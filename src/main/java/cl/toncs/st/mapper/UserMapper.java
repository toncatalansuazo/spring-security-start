package cl.toncs.st.mapper;

import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.domain.user.DentalUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public DentalUser userToUserDetail(DentalUser user) {
        return new DentalUser(user.getPassword(), user.getEmail(), user.getAuthorities());
    }

    public DentalUser toDentalUser(UserEntity user) {
        return new DentalUser(user);
    }
}
