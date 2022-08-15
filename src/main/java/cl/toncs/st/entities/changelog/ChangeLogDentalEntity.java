package cl.toncs.st.entities.changelog;

import cl.toncs.st.entities.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Data
@Entity(name = "Changelog")
@Table(name = "changelogs")
public class ChangeLogDentalEntity extends BaseEntity {
    @Id
    @Column(unique = true)
    private Long version;

    @Column
    private String Description;
}
