package cl.zeruscode.treip.repository;

import cl.zeruscode.treip.entities.changelog.ChangeLogDentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ChangeLogDentalRepository extends CrudRepository<ChangeLogDentalEntity, Long> {

    ChangeLogDentalEntity findByVersion(Long version);
}
