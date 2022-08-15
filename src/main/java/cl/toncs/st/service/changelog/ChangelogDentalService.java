package cl.toncs.st.service.changelog;

import cl.toncs.st.entities.changelog.ChangeLogDentalEntity;
import cl.toncs.st.repository.ChangeLogDentalRepository;
import cl.toncs.st.domain.changelog.ChangelogBase;

import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service(value = "ChangeLogDental")
public class ChangelogDentalService {

    private final ChangeLogDentalRepository changeLogRepository;

    @Transactional
    public void execute(@Nonnull ChangelogBase changelogBase) {
        ChangeLogDentalEntity changeLogEntity = changelogBase.getChangeLogEntity();
        checkConstrains(changeLogEntity);

        var changelogVersion = changeLogEntity.getVersion();
        var changelog = changeLogRepository.findByVersion(changelogVersion);
        boolean alreadySaved = changelog != null;
        if (!alreadySaved) {
            executeChanges(changelogBase);
        }
    }

    private void executeChanges(ChangelogBase changelogBase) {
        changelogBase.saveInDb();
        changeLogRepository.save(changelogBase.getChangeLogEntity());
    }

    private void checkConstrains(ChangeLogDentalEntity changeLogEntity) {
        if (changeLogEntity == null) {
            throw new RuntimeException("please define a ChangeLogEntity");
        }
    }

}
