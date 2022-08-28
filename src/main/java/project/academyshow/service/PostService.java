package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.PostSaveRequest;
import project.academyshow.entity.Academy;
import project.academyshow.entity.Member;
import project.academyshow.repository.AcademyRepository;
import project.academyshow.repository.PostRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AcademyRepository academyRepository;

    public void save(PostSaveRequest postSaveRequest, Member member) {
        Optional<Academy> academy = academyRepository.findById(postSaveRequest.getAcademyId());
        academy.orElseThrow(() -> new IllegalArgumentException("없는 academy_id 입니다."));
        postRepository.save(postSaveRequest.toEntity(member, academy.get()));
    }
}
