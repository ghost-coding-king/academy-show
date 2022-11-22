package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchLikes extends AbstractTimestampEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private LikesType referenceType;

    @Column
    private Long referenceId;

    @Column
    private Long count;

    public void incrementCountForBatch() {
        this.count += 1L;
    }

    public void resetCountForBatch() {
        this.count = 0L;
    }
}
