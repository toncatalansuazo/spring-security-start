package cl.toncs.st.repository;

import cl.toncs.st.entities.user.RoleEntity;
import cl.toncs.st.entities.user.RoleType;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Nonnull
    RoleEntity findByName(@Nonnull RoleType roleType);

    @Query(value = "SELECT * FROM roles r WHERE r.name IN :names", nativeQuery = true)
    List<RoleEntity> findAllByNames(@Nonnull @Param(value = "names") Set<String> names);
}
