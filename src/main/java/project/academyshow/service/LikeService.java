package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.LikesRequest;
import project.academyshow.controller.response.ReferenceLikesStatistics;
import project.academyshow.entity.*;
import project.academyshow.entity.Likes;

import project.academyshow.repository.LikeRepository;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.entity.CustomUserDetails;

import javax.persistence.EntityManager;
import java.util.Objects;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    @Transactional
    public void create(LikesRequest request, CustomUserDetails userDetails) {
        switch (request.getType()) {
            case TUTOR: createTutorLikes(request, userDetails); break;
            case ACADEMY: createAcademyLikes(request, userDetails); break;
            case POST: createPostLikes(request, userDetails); break;
        }
    }

    @Transactional
    public void destroy(LikesRequest request, CustomUserDetails userDetails) {
        switch (request.getType()) {
            case TUTOR: destroyTutorLikes(request, userDetails); break;
            case ACADEMY: destroyAcademyLikes(request, userDetails); break;
            case POST: destroyPostLikes(request, userDetails); break;
        }
    }

    /**
     * PostLikes
     */
    private void createPostLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        if(likeRepository.findByPost_IdAndMember(request.getReferenceId(), member).isPresent())
            return;

        Post post = entityManager.getReference(Post.class, request.getReferenceId());

        likeRepository.save(Likes.createPostLikes(member, post));
    }

    private void destroyPostLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        Optional<Likes> likes = likeRepository.findByPost_IdAndMember(request.getReferenceId(), member);

        if (likes.isPresent())
            likeRepository.delete(likes.get());
    }

    /**
     * AcademyLikes
     */
    private void createAcademyLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        if(likeRepository.findAcademyLikesByAcademy_IdAndMember(request.getReferenceId(), member).isPresent())
            return;

        Academy academy = entityManager.getReference(Academy.class, request.getReferenceId());

        likeRepository.save(Likes.createAcademyLikes(member, academy));
    }

    private void destroyAcademyLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        Optional<Likes> likes = likeRepository
                .findAcademyLikesByAcademy_IdAndMember(request.getReferenceId(), member);

        if (likes.isPresent())
            likeRepository.delete(likes.get());
    }

    /**
     * TutorLikes
     */
    private void createTutorLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        if(likeRepository.findByTutorInfo_IdAndMember(request.getReferenceId(), member).isPresent())
            return;

        TutorInfo tutorInfo = entityManager.getReference(TutorInfo.class, request.getReferenceId());

        likeRepository.save(Likes.createTutorInfoLikes(member, tutorInfo));
    }

    private void destroyTutorLikes(LikesRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        Optional<Likes> likes = likeRepository.findByTutorInfo_IdAndMember(request.getReferenceId(), member);

        if (likes.isPresent())
            likeRepository.delete(likes.get());
    }

    public ReferenceLikesStatistics getLikeInfoByReference(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        if(Objects.isNull(userDetails))
            return ReferenceLikesStatistics.notAuthenticatedOf(likeCountByReference(type, componentId));

        return ReferenceLikesStatistics.of(likeCountByReference(type, componentId),
                               isLikeClicked(type, componentId, userDetails));
    }

    private Long likeCountByReference(ReferenceType type, Long componentId) {
        switch (type) {
            case TUTOR: return likeRepository.countByTutorInfo_Id(componentId);
            case ACADEMY: return likeRepository.countByAcademy_Id(componentId);
            case POST: return likeRepository.countByPost_Id(componentId);
            default: throw new RuntimeException("허용되지 않은 타입");
        }
    }

    private boolean isLikeClicked(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails);

        return getLikeByComponentAndMember(type, componentId, member).isPresent();
    }

    private Optional<Likes> getLikeByComponentAndMember(ReferenceType type, Long componentId, Member member) {
        switch (type) {
            case TUTOR: return likeRepository.findByTutorInfo_IdAndMember(componentId, member);
            case ACADEMY: return likeRepository.findAcademyLikesByAcademy_IdAndMember(componentId, member);
        }
        return Optional.empty();
    }

    private Member findByUsernameAndProviderType(CustomUserDetails customUserDetails) {
        return memberRepository
                .findByUsernameAndProviderType(customUserDetails.getUsername(), customUserDetails.getProviderType())
                .orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다."));
    }
}
