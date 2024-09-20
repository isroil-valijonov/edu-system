package com.example.edusystem.repository;

import com.example.edusystem.entity.Exam;
import com.example.edusystem.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    boolean existsByGroupAndDate(Group group, LocalDateTime date);

    boolean existsByGroupAndDateAndIdNot(Group newGroup, LocalDateTime newDate, Long examId);
}
