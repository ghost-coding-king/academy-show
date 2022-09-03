package project.academyshow.controller.response;

import lombok.Getter;
import project.academyshow.entity.*;

@Getter
public class PostResponse extends AbstractAcademyshowResponse{
    private Long id;
    private String nickname; //학원명, 괴외명(선생님 이름)
    private String title;
    private String content;
    private String profile;

    private PostResponse(Post post) {
        super(post);
        this.id = post.getId();
        this.nickname = post.getMember().getName();
        this.title = post.getTitle();
        this.profile =
                post.getCategory() == PostCategory.ACADEMY_NEWS ? post.profileOfAcademy() : post.profileOfMember();
        this.nickname =
                post.getCategory() == PostCategory.ACADEMY_NEWS ? post.nameOfAcademy() : post.nameOfMember();
    }
    
    public static PostResponse ofList(Post post) {
        return new PostResponse(post);
    }

    public static PostResponse of(Post post) {
        PostResponse response =  new PostResponse(post);
        response.content = post.getContent();
        return response;
    }
}
