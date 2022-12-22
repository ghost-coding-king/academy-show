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
    private ReferenceLikesStatistics likes;

    private PostResponse(Post post, ReferenceLikesStatistics likes) {
        super(post);
        this.id = post.getId();
        this.nickname = post.getMember().getName();
        this.title = post.getTitle();
        this.profile =
                post.getCategory() == PostCategory.ACADEMY_NEWS ? post.profileOfAcademy() : post.profileOfMember();
        this.nickname =
                post.getCategory() == PostCategory.ACADEMY_NEWS ? post.nameOfAcademy() : post.nameOfMember();
        this.likes = likes;
    }

    public static PostResponse ofList(Post post, BatchLikes batchLikes) {
        return new PostResponse(post, ReferenceLikesStatistics.notAuthenticatedOf(batchLikes.getCount()));
    }

    public static PostResponse ofAuthenticatedListRequest(Post post, BatchLikes batchLikes, boolean isLike) {
        return new PostResponse(post, ReferenceLikesStatistics.of(batchLikes.getCount(), isLike));
    }

    public static PostResponse of(Post post, BatchLikes batchLikes) {
        PostResponse response = new PostResponse(
                post, ReferenceLikesStatistics.notAuthenticatedOf(batchLikes.getCount()));
        response.content = post.getContent();
        return response;
    }

    public static PostResponse ofAuthenticatedRequest(Post post, BatchLikes batchLikes, boolean isLike) {
        PostResponse response = new PostResponse(post, ReferenceLikesStatistics.of(batchLikes.getCount(), isLike));
        response.content = post.getContent();
        return response;
    }
}
