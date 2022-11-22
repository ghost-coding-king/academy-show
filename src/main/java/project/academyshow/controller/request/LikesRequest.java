package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.academyshow.entity.Likes;
import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;

@Getter @Setter
@NoArgsConstructor
public class LikesRequest {
    private ReferenceType type;
    private Long referenceId;

    public Likes toEntity(Member member) {
        return Likes.builder()
                .type(type)
                .referenceId(referenceId)
                .member(member)
                .build();
    }
}
