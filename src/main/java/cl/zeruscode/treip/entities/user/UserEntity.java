package cl.zeruscode.treip.entities.user;

import cl.zeruscode.treip.entities.BaseEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Data
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "User")
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column
    @ManyToMany
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column
    private boolean accountExpired;

    @Column
    private boolean locked;

    @Column
    private boolean credentialsExpired;

    @Column
    private boolean enabled;

    @Column
    private LocalDateTime expiredAt;

    public UserEntity (@Nonnull String username, @Nonnull String email, @Nonnull String password, @Nonnull List<RoleEntity> roles) {
        super();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }
}
