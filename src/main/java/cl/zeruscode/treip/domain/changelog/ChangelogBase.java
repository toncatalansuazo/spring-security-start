package cl.zeruscode.treip.domain.changelog;

import cl.zeruscode.treip.entities.changelog.ChangeLogDentalEntity;
import lombok.Getter;
import lombok.Setter;

public abstract class ChangelogBase {

    @Setter
    @Getter
    protected ChangeLogDentalEntity changeLogEntity;

    public abstract void saveInDb();
}
