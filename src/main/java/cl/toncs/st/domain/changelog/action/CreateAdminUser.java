package cl.toncs.st.domain.changelog.action;

import cl.toncs.st.entities.changelog.ChangeLogDentalEntity;
import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.entities.user.UserEntity;
import cl.toncs.st.repository.RoleRepository;
import cl.toncs.st.repository.UserRepository;
import cl.toncs.st.domain.changelog.ChangelogBase;

import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CreateAdminUser extends ChangelogBase {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CreateAdminUser(@Nonnull PasswordEncoder passwordEncoder, @Nonnull UserRepository userRepository, @Nonnull RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.changeLogEntity = new ChangeLogDentalEntity(Long.valueOf(1), "Create admin user");
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveInDb() {
        var adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN);
        var userRole = roleRepository.findByName(RoleType.ROLE_USER);
        var users = List.of(
            new UserEntity("superadmin", "superadmin@dentalprofit.it", passwordEncoder.encode("changePassword"),
                           List.of(adminRole), true),
            new UserEntity("user", "user@dentalprofit.it", passwordEncoder.encode("changePassword"),
                           List.of(userRole), true)
        );
        this.userRepository.saveAll(users);
    }
}
