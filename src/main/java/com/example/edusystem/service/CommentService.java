package com.example.edusystem.service;

import com.example.edusystem.dto.CommentRequestDto;
import com.example.edusystem.entity.Comment;
import com.example.edusystem.entity.Group;
import com.example.edusystem.entity.Student;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.repository.CommentRepository;
import com.example.edusystem.repository.GroupRepository;
import com.example.edusystem.repository.StudentRepository;
import com.example.edusystem.response.CommentResponseDto;
import com.example.edusystem.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;


    public CommentResponseDto createComment(CommentRequestDto commentRequestDto) throws CustomNotFoundException, BadRequestException {
        if (commentRequestDto.getText() == null || commentRequestDto.getText().trim().isEmpty()) {
            throw new BadRequestException("Comment text cannot be empty");
        }

        Student student = studentRepository.findById(commentRequestDto.getStudentId())
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        Group group = groupRepository.findById(commentRequestDto.getGroupId())
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));

        Comment comment = new Comment();
        comment.setText(commentRequestDto.getText());
        comment.setStudent(student);
        comment.setGroup(group);
        comment.setDate(LocalDateTime.now());

        commentRepository.save(comment);
        return convertToDto(comment);
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto) throws CustomNotFoundException, BadRequestException {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomNotFoundException("Comment not found"));

        groupRepository.findById(commentRequestDto.getGroupId()).orElseThrow(
                () -> new CustomNotFoundException("Group not found"));

        studentRepository.findById(commentRequestDto.getStudentId()).orElseThrow(
                () -> new CustomNotFoundException("Student not found"));

        if (commentRequestDto.getText() == null || commentRequestDto.getText().trim().isEmpty()) {
            throw new BadRequestException("Comment text cannot be empty");
        }

        comment.setText(commentRequestDto.getText());
        commentRepository.save(comment);

        return convertToDto(comment);
    }

    public CommonResponse deleteComment(Long id) throws CustomNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Comment not found"));

        Student student = comment.getStudent();
        Group group = comment.getGroup();

        if (student != null) {
            student.getComments().remove(comment);
            studentRepository.save(student);
        }

        if (group != null) {
            group.getComments().remove(comment);
            groupRepository.save(group);
        }

        commentRepository.delete(comment);
        return new CommonResponse(HttpStatus.OK.value(), "Comment successfully deleted", LocalDateTime.now());
    }

    public CommentResponseDto getComment(Long id) throws CustomNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Comment not found"));
        return convertToDto(comment);
    }

    public List<CommentResponseDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByGroupId(Long groupId) throws CustomNotFoundException {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new CustomNotFoundException("Group not found"));
        List<Comment> comments = commentRepository.findCommentsByGroupId(groupId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> getCommentsByStudentId(Long studentId) throws CustomNotFoundException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomNotFoundException("Student not found"));
        List<Comment> comments = commentRepository.findCommentsByStudentId(studentId);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentResponseDto convertToDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getText(),
                comment.getDate(),
                comment.getGroup().getId(),
                comment.getStudent().getId()
        );
    }
}


