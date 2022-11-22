package project.academyshow.service.dtos;

import project.academyshow.entity.Post;

public interface PostLikeCountDto {
    Post getPost();
    Long getLikeCount();
}
