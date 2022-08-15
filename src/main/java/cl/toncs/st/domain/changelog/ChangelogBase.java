package cl.toncs.st.domain.changelog;

import cl.toncs.st.entities.changelog.ChangeLogDentalEntity;
import lombok.Getter;
import lombok.Setter;

public abstract class ChangelogBase {

    @Setter
    @Getter
    protected ChangeLogDentalEntity changeLogEntity;

    public abstract void saveInDb();
}
