package cl.zeruscode.treip.entities.changelog;

import cl.zeruscode.treip.entities.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
