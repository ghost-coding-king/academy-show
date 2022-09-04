package project.academyshow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Up extends AbstractTimestampEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReferenceType type;

    /** component likes are added */
    @Column
    private Long referenceId;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
