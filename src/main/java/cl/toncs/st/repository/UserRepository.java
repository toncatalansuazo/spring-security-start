package cl.toncs.st.repository;

import cl.toncs.st.entities.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<UserEntity> findByName(@Param(value = "username") String name);

    Optional<UserEntity> findByEmail(@Param(value = "email") String email);
}
