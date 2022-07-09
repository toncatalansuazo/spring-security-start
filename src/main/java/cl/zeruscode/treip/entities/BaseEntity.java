package cl.zeruscode.treip.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column(
        name = "created_at"
    )
    private Instant createdAt;

    @LastModifiedDate
    @Column(
        name = "updated_at"
    )
    private Instant lastModified;
}
