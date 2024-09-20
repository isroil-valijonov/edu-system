package com.example.edusystem.repository;

import com.example.edusystem.entity.Exam;
import com.example.edusystem.entity.Student;
import com.example.edusystem.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    List<StudentExam> findByExamId(Long examId);
    boolean existsByStudentAndExam(Student student, Exam exam);
    List<StudentExam> findByStudent_Id(Long studentId);
    List<StudentExam> findByExam_Group_Id(Long groupId);
}
