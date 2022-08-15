package cl.toncs.st.entities.user;

import cl.toncs.st.entities.BaseEntity;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@ToString(callSuper = true)
@Setter
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "User")
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude // exclude from lombok to string or you get a stackoverflow
    @ManyToMany(fetch = FetchType.EAGER)
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
    private boolean accountExpired = false;

    @Column
    private boolean locked = false;

    @Column
    private boolean credentialsExpired = false;

    @Column
    private boolean enabled = true;

    @Column(nullable = true)
    private LocalDateTime expiredAt;

    public UserEntity(@Nonnull String username,
                       @Nonnull String email,
                       @Nonnull String password,
                       @Nonnull List<RoleEntity> roles) {
        super();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
    }

    public UserEntity(@Nonnull String username,
                       @Nonnull String email,
                       @Nonnull String password,
                       @Nonnull List<RoleEntity> roles,
                       boolean isEnabled) {
        super();
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.email = email;
        this.enabled = isEnabled;
    }
}
