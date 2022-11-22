package project.academyshow.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.academyshow.entity.Likes;
import project.academyshow.entity.LikesType;
import project.academyshow.entity.Member;

@Getter @Setter
@NoArgsConstructor
public class LikesRequest {
    private LikesType type;
    private Long referenceId;

    public Likes toEntity(Member member) {
        return Likes.builder()
                .type(type)
                .member(member)
                .build();
    }
}
