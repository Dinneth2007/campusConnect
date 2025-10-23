package com.example.CampusConnectService.repository;

import com.example.CampusConnectService.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPost_IdAndDeletedFalse(Long postId);
    List<Comment> findByParentComment_IdAndDeletedFalse(Long parentCommentId);
}
