package com.example.edusystem.repository;

import com.example.edusystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT s.* FROM student s JOIN users u ON s.user_id = u.id WHERE u.phone_number LIKE %:phoneNumber%", nativeQuery = true)
    List<Student> findByPhoneNumberPart(@Param("phoneNumber") String phoneNumber);

    List<Student> findStudentsByGroupsId(Long id);
}
