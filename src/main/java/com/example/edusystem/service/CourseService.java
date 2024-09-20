package com.example.edusystem.service;

import com.example.edusystem.dto.CourseRequestDto;
import com.example.edusystem.entity.Course;
import com.example.edusystem.entity.Group;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.CourseRepository;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.CourseResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;

    public CourseResponseDto createCourse(CourseRequestDto courseRequestDto) throws CustomException {
        Optional<Course> courseByName = courseRepository.findCourseByName(courseRequestDto.getName());
        if (courseByName.isPresent()) {
            throw new CustomException("Course with this name already exists");
        }
        if (courseRequestDto.getPrice() == 0) {
            throw new CustomException("Course price cannot be null");
        }
        Course course = new Course(courseRequestDto.getName(), courseRequestDto.getPrice());
        courseRepository.save(course);
        return new CourseResponseDto(course.getId(), course.getName(), course.getPrice(), HttpStatus.CREATED.value());
    }

    public CourseResponseDto updateCourse(Long id, CourseRequestDto courseUpdateDto) throws CustomException, CustomNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Course not found", HttpStatus.NOT_FOUND.value()));

        Optional<Course> courseByName = courseRepository.findCourseByName(courseUpdateDto.getName());
        if (courseByName.isPresent()) {
            throw new CustomException("Course name already exist", HttpStatus.BAD_REQUEST.value());
        }
        course.setName(courseUpdateDto.getName());

        if (courseUpdateDto.getPrice() <= 0) {
            throw new CustomException("Course price must be greater than zero", HttpStatus.BAD_REQUEST.value());
        }
        course.setPrice(courseUpdateDto.getPrice());

        courseRepository.save(course);

        return new CourseResponseDto(course.getId(), course.getName(), course.getPrice(), HttpStatus.OK.value());
    }

    public CourseResponseDto getCourse(Long id) throws CustomNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Course not found", HttpStatus.NOT_FOUND.value()));
        return new CourseResponseDto(course.getId(), course.getName(), course.getPrice(), HttpStatus.OK.value());
    }
    public List<Course> getAllCourse() {
        return courseRepository.findAll();
    }

    public CommonResponse deleteCourse(Long id) throws CustomNotFoundException {
        Course courses = courseRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Course not found", HttpStatus.NOT_FOUND.value()));
        List<Group> groups = courses.getGroups();

        groups.forEach(group -> group.setCourse(null));
        groupRepository.saveAll(groups);

        courseRepository.delete(courses);
        return new CommonResponse(HttpStatus.OK.value(), "Course successfully deleted", LocalDateTime.now());
    }

}