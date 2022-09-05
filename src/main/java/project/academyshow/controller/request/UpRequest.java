package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.academyshow.entity.Up;
import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;

@Getter @Setter
@NoArgsConstructor
public class UpRequest {
    private ReferenceType type;
    private Long referenceId;

    public Up toEntity(Member member) {
        return Up.builder()
                .type(type)
                .referenceId(referenceId)
                .member(member)
                .build();
    }
}
