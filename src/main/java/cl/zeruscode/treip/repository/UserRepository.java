package cl.zeruscode.treip.repository;

import cl.zeruscode.treip.entities.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM User u WHERE u.username = 1")
    Optional<UserEntity> findByName(String name);

}
