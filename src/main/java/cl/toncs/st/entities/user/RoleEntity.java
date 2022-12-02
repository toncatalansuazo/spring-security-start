package cl.toncs.st.entities.user;

import cl.toncs.st.entities.BaseEntity;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString(callSuper = true)
@Entity(name = "Role")
@Table(name = "roles")
public class RoleEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", unique = true)
    private RoleType name;

    @ToString.Exclude // exclude from lombok to string or you get a stackoverflow
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserEntity> users;

    public RoleEntity(RoleType name) {
        this.name = name;
    }
}
