package cl.zeruscode.treip.domain.changelog.action;

import cl.zeruscode.treip.domain.changelog.ChangelogBase;
import cl.zeruscode.treip.entities.changelog.ChangeLogDentalEntity;
import cl.zeruscode.treip.entities.user.RoleEntity;
import cl.zeruscode.treip.entities.user.RoleType;
import cl.zeruscode.treip.entities.user.UserEntity;
import cl.zeruscode.treip.repository.RoleRepository;
import cl.zeruscode.treip.repository.UserRepository;
import java.util.List;
import javax.annotation.Nonnull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CreateAdminUser extends ChangelogBase {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public CreateAdminUser(@Nonnull PasswordEncoder passwordEncoder, @Nonnull UserRepository userRepository, @Nonnull RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.changeLogEntity = new ChangeLogDentalEntity(Long.valueOf(1), "Create admin user");
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveInDb() {
        var adminRole = roleRepository.findByName(RoleType.ADMIN);
        var userRole = roleRepository.findByName(RoleType.USER);
        var users = List.of(
            new UserEntity("superadmin", "superadmin@dentalprofit.it", passwordEncoder.encode("changePassword"),
                           List.of(adminRole)),
            new UserEntity("user", "user@dentalprofit.it", passwordEncoder.encode("changePassword"),
                           List.of(userRole))
        );
        this.userRepository.saveAll(users);
    }
}
