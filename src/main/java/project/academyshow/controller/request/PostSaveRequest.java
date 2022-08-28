package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.Academy;
import project.academyshow.entity.Member;
import project.academyshow.entity.Post;
import project.academyshow.entity.PostCategory;

import java.time.LocalDateTime;

@Data
public class PostSaveRequest {

    private PostCategory category;
    private String title;
    private String content;
    private Long academyId;

    public Post toEntity(Member member, Academy academy) {
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .createdAt(LocalDateTime.now())
                .member(member)
                .academy(academy)
                .build();
    }
}
