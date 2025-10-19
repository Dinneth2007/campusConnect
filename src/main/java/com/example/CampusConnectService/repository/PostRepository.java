package com.example.CampusConnectService.repository;

import com.example.CampusConnectService.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByAuthor_IdAndDeletedFalse(Long authorId, Pageable pageable);
    Page<Post> findByDeletedFalse(Pageable pageable);
    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name = :tagName AND p.deleted = false")
    Page<Post> findByTagName(@Param("tagName") String tagName, Pageable pageable);
}
