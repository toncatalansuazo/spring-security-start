package cl.zeruscode.treip.domain.changelog.action;

import cl.zeruscode.treip.domain.changelog.ChangelogBase;
import cl.zeruscode.treip.entities.changelog.ChangeLogDentalEntity;
import cl.zeruscode.treip.entities.user.RoleEntity;
import cl.zeruscode.treip.entities.user.RoleType;
import cl.zeruscode.treip.repository.RoleRepository;
import java.util.List;
import javax.annotation.Nonnull;


public class CreateBaseRoles extends ChangelogBase {
    private final RoleRepository roleRepository;

    public CreateBaseRoles(@Nonnull RoleRepository roleRepository) {
        this.changeLogEntity = new ChangeLogDentalEntity(Long.valueOf(0), "Create Roles");
        this.roleRepository = roleRepository;
    }

    @Override
    public void saveInDb() {
        var roles = List.of(new RoleEntity(RoleType.ADMIN),
                new RoleEntity(RoleType.USER));
        this.roleRepository.saveAll(roles);
    }
}
