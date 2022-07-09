package cl.zeruscode.treip.repository;

import cl.zeruscode.treip.entities.user.RoleEntity;
import cl.zeruscode.treip.entities.user.RoleType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(RoleType roleType);

    @Query(value = "SELECT r FROM Role r WHERE r.name in :names")
    List<RoleEntity> findAllByNames(@Param(value = "names") List<String> names);
}
