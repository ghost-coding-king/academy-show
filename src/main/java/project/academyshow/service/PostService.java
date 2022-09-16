package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.entity.*;
import project.academyshow.repository.AcademyRepository;
import project.academyshow.repository.PostRepository;
import project.academyshow.repository.TutorInfoRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TutorInfoRepository tutorInfoRepository;
    private final AcademyRepository academyRepository;

    public Post save(PostRequest postRequest, Member member) {
        if(member.getRole() == RoleType.ROLE_ACADEMY) {
            Optional<Academy> academy = academyRepository.findById(member.getAcademy().getId());
            academy.orElseThrow(() -> new IllegalArgumentException("없는 academy_id 입니다."));
            return postRepository.save(postRequest.toEntity(member, academy.get(), null));
        }
        else if(member.getRole() == RoleType.ROLE_TUTOR) {
            Optional<TutorInfo> tutorInfo = tutorInfoRepository.findById(member.getTutorInfo().getId());
            tutorInfo.orElseThrow(() -> new IllegalArgumentException("없는 tutor_info_id 입니다."));
            return postRepository.save(postRequest.toEntity(member, null, tutorInfo.get()));
        }
        return null;
    }

    public Page<Post> findAllByAcademy(Long id, Pageable pageable) {
        return postRepository.findAllByAcademy(id, pageable);
    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new RuntimeException("해당 게시물을 찾을수 없습니다."));
    }

    public Page<Post> findAllByCategory(PostCategory category, Pageable pageable) {
        return postRepository.findAllByCategory(category, pageable);
    }
}
