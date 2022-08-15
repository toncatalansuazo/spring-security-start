package cl.toncs.st.domain.changelog.action;

import cl.toncs.st.entities.changelog.ChangeLogDentalEntity;
import cl.toncs.st.entities.user.RoleEntity;
import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.repository.RoleRepository;
import cl.toncs.st.domain.changelog.ChangelogBase;

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
        var roles = List.of(new RoleEntity(RoleType.ROLE_ADMIN),
                new RoleEntity(RoleType.ROLE_USER));
        this.roleRepository.saveAll(roles);
    }
}
