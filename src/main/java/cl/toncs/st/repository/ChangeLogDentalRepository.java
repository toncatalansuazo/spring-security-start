package cl.toncs.st.repository;

import cl.toncs.st.entities.changelog.ChangeLogDentalEntity;
import org.springframework.data.repository.CrudRepository;


public interface ChangeLogDentalRepository extends CrudRepository<ChangeLogDentalEntity, Long> {

    ChangeLogDentalEntity findByVersion(Long version);
}
