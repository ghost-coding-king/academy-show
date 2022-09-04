package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.UpRequest;
import project.academyshow.controller.response.ReferenceUpStatistics;
import project.academyshow.entity.Up;

import project.academyshow.entity.Member;
import project.academyshow.entity.ReferenceType;
import project.academyshow.repository.LikeRepository;
import project.academyshow.security.entity.CustomUserDetails;

import java.util.Objects;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public void createOrDestroy(UpRequest request, CustomUserDetails userDetails) {
        Optional<Up> like = getLikeByComponentAndMember(request.getType(),
                                                          request.getReferenceId(),
                                                          userDetails.getMember());
        if(like.isPresent())
            likeRepository.delete(like.get());
        else
            likeRepository.save(request.toEntity(userDetails.getMember()));
    }

    public ReferenceUpStatistics getLikeInfoByReference(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        if(Objects.isNull(userDetails))
            return ReferenceUpStatistics.notAuthenticatedof(likeCountByReference(type, componentId));

        return ReferenceUpStatistics.of(likeCountByReference(type, componentId),
                               isLikeClicked(type, componentId, userDetails));
    }

    private Long likeCountByReference(ReferenceType type, Long componentId) {
        return likeRepository.countByTypeAndReferenceId(type, componentId);
    }

    private boolean isLikeClicked(ReferenceType type, Long componentId, CustomUserDetails userDetails) {
        return getLikeByComponentAndMember(type, componentId, userDetails.getMember()).isPresent();
    }

    private Optional<Up> getLikeByComponentAndMember(ReferenceType type, Long componentId, Member member) {
        return likeRepository.findByTypeAndReferenceIdAndMember(type, componentId, member);
    }
}
