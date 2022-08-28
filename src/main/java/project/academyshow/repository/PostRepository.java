package project.academyshow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.academyshow.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
