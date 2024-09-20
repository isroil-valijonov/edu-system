package com.example.edusystem.repository;

import com.example.edusystem.entity.Attendance;
import com.example.edusystem.entity.Group;
import com.example.edusystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findAllByStudentId(Long id);
    List<Attendance> findAllByGroupId(Long group);
    boolean existsByStudentAndGroupAndDate(Student student, Group group, LocalDate date);
}
