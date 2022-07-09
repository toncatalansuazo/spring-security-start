package cl.zeruscode.treip.changelog;

import cl.zeruscode.treip.domain.changelog.ChangelogBase;
import cl.zeruscode.treip.domain.changelog.action.CreateAdminUser;
import cl.zeruscode.treip.domain.changelog.action.CreateBaseRoles;
import cl.zeruscode.treip.repository.ChangeLogDentalRepository;
import cl.zeruscode.treip.repository.RoleRepository;
import cl.zeruscode.treip.repository.UserRepository;
//import cl.zeruscode.treip.service.changelog.ChangelogDentalService;
import cl.zeruscode.treip.service.changelog.ChangelogDentalService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class ChangelogDbDevConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ChangelogDentalService changelogService;

    @EventListener(ApplicationReadyEvent.class)
    void executeChangeLogActions() {
        log.info("Executing changelogs in db");
        List<ChangelogBase> changeLogs = List.of(
            new CreateBaseRoles(roleRepository),
            new CreateAdminUser(passwordEncoder, userRepository, roleRepository)
        );

        changeLogs.forEach(changeLog -> changelogService.execute(changeLog));
    }

}
