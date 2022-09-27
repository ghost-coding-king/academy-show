package project.academyshow.controller.request;

import lombok.Data;
import project.academyshow.entity.*;

@Data
public class PostRequest {

    private PostCategory category;
    private String title;
    private String content;

    public Post toEntity(Member member, Academy academy, TutorInfo tutorInfo) {
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .member(member)
                .academy(academy)
                .tutorInfo(tutorInfo)
                .build();
    }
}
