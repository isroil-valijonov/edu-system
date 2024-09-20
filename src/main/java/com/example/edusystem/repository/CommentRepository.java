package com.example.edusystem.repository;

import com.example.edusystem.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByGroupId(Long id);
    List<Comment> findCommentsByStudentId(Long id);
}
