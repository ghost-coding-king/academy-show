package project.academyshow.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class BatchLikes {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long count = 0L;

    public void resetCountForBatch() {
        this.count = 0L;
    }

    public void incrementCountForBatch() {
        this.count += 1L;
    }
}
