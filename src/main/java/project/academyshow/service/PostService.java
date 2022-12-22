package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.controller.response.PostResponse;
import project.academyshow.entity.*;
import project.academyshow.repository.*;
import project.academyshow.security.entity.CustomUserDetails;

import java.util.*;

/**
 * 게시물에 대한 모든 비즈니스 로직을 처리한다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TutorInfoRepository tutorInfoRepository;
    private final AcademyRepository academyRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    public Post save(PostRequest postRequest, CustomUserDetails userDetails) {
        Member member = memberRepository.findByUsernameAndProviderType(
                userDetails.getUsername(), userDetails.getProviderType()
        ).orElseThrow(() -> new UsernameNotFoundException("Username not found."));

        if(userDetails.getRole() == RoleType.ROLE_ACADEMY) {
            Optional<Academy> academy = academyRepository.findById(member.getAcademy().getId());
            academy.orElseThrow(() -> new IllegalArgumentException("없는 academy_id 입니다."));
            return postRepository.save(postRequest.toEntity(member, academy.get(), null));
        }
        else if(userDetails.getRole() == RoleType.ROLE_TUTOR) {
            Optional<TutorInfo> tutorInfo = tutorInfoRepository.findById(member.getTutorInfo().getId());
            tutorInfo.orElseThrow(() -> new IllegalArgumentException("없는 tutor_info_id 입니다."));
            return postRepository.save(postRequest.toEntity(member, null, tutorInfo.get()));
        }
        return null;
    }

    public Page<PostResponse> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(post -> PostResponse.of(post, post.getBatchLikes()));
    }

    public Page<PostResponse> findAllByAcademy(Long id, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByAcademyId(id, pageable);
        return posts.map(post -> PostResponse.ofList(post, post.getBatchLikes()));
    }

    public Page<PostResponse> findAllByTutorInfo(Long id, Pageable pageable) {
        Page<Post> posts = postRepository.findAllByTutorInfoId(id, pageable);
        return posts.map(post -> PostResponse.ofList(post, post.getBatchLikes()));
    }

    public PostResponse findById(Long id, CustomUserDetails userDetails) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 게시물을 찾을수 없습니다."));

        if(Objects.isNull(userDetails))
            return PostResponse.of(post, post.getBatchLikes());

        // 현재 로그인한 멤버를 조회한다.
        Member member = findByUserDetails(userDetails);

        boolean isLiked = Objects.nonNull(postRepository.likedByMemberAndInPost(member, post));

        return PostResponse.ofAuthenticatedRequest(post, post.getBatchLikes(), isLiked);

    }

    public Page<Post> findAllByCategory(PostCategory category, Pageable pageable) {
        return postRepository.findAllByCategoryOrderByCreatedAtDesc(category, pageable);
    }

    /**
     * Member member
     */
    private Member findByUserDetails(CustomUserDetails userDetails) {
        if(Objects.isNull(userDetails))
            throw new RuntimeException("해당하는 멤버가 없습니다!");

        return memberRepository
                .findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType())
                .orElseThrow(() -> new RuntimeException("해당하는 멤버가 없습니다!"));
    }
}
