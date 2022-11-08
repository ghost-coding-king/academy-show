package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.UpRequest;
import project.academyshow.controller.response.ReferenceUpStatistics;
import project.academyshow.entity.ProviderType;
import project.academyshow.entity.Up;

import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;
import project.academyshow.repository.LikeRepository;
import project.academyshow.repository.MemberRepository;
import project.academyshow.security.entity.CustomUserDetails;

import java.util.Objects;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;

    public void createOrDestroy(UpRequest request, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType());

        Optional<Up> like = getLikeByComponentAndMember(
                request.getType(),
                request.getReferenceId(),
                member
        );

        if(like.isPresent())
            likeRepository.delete(like.get());
        else
            likeRepository.save(request.toEntity(member));
    }

    public ReferenceUpStatistics getLikeInfoByReference(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        if(Objects.isNull(userDetails))
            return ReferenceUpStatistics.notAuthenticatedOf(likeCountByReference(type, componentId));

        return ReferenceUpStatistics.of(likeCountByReference(type, componentId),
                               isLikeClicked(type, componentId, userDetails));
    }

    private Long likeCountByReference(ReferenceType type, Long componentId) {
        return likeRepository.countByTypeAndReferenceId(type, componentId);
    }

    private boolean isLikeClicked(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        Member member = findByUsernameAndProviderType(userDetails.getUsername(), userDetails.getProviderType());

        return getLikeByComponentAndMember(type, componentId, member).isPresent();
    }

    private Optional<Up> getLikeByComponentAndMember(ReferenceType type, Long componentId, Member member) {
        return likeRepository.findByTypeAndReferenceIdAndMember(type, componentId, member);
    }

    private Member findByUsernameAndProviderType(String username, ProviderType providerType) {
        return memberRepository.findByUsernameAndProviderType(username, providerType)
                .orElseThrow(() -> new RuntimeException("유저를 찾을수 없습니다."));
    }
}
