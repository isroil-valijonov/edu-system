package com.example.edusystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`group`", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
})
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private LocalDateTime createDate;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Teacher teacher;

    @ManyToMany
    private Set<Student> students;

    @OneToMany
    private Set<Attendance> attendances;

    @OneToMany
    private Set<Rating> ratings;

    @OneToMany
    private Set<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Exam> exams;
}
