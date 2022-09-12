package project.academyshow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.academyshow.controller.request.PostRequest;
import project.academyshow.entity.Academy;
import project.academyshow.entity.Member;
import project.academyshow.entity.Post;
import project.academyshow.entity.PostCategory;
import project.academyshow.repository.AcademyRepository;
import project.academyshow.repository.PostRepository;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final AcademyRepository academyRepository;

    public Post save(PostRequest postRequest, Member member) {
        Optional<Academy> academy = academyRepository.findById(postRequest.getAcademyId());
        academy.orElseThrow(() -> new IllegalArgumentException("없는 academy_id 입니다."));
        return postRepository.save(postRequest.toEntity(member, academy.get()));
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
