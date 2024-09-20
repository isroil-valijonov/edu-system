package com.example.edusystem.controller;

import com.example.edusystem.dto.CommentRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommentResponseDto;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create-create")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody @Valid CommentRequestDto commentRequestDto) throws CustomNotFoundException, BadRequestException {
        CommentResponseDto commentResponse = commentService.createComment(commentRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentResponse);
    }

    @PutMapping("/update-comment/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody @Valid CommentRequestDto commentRequestDto) throws CustomNotFoundException, BadRequestException {
        CommentResponseDto updatedComment = commentService.updateComment(id, commentRequestDto);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/delete-comment/{id}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse response = commentService.deleteComment(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-comment/{id}")
    public ResponseEntity<CommentResponseDto> getComment(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommentResponseDto commentResponse = commentService.getComment(id);
        return ResponseEntity.ok(commentResponse);
    }

    @GetMapping("/get-all-comment")
    public ResponseEntity<List<CommentResponseDto>> getAllComments() {
        List<CommentResponseDto> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/get-comments-by-group/{groupId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByGroupId(@PathVariable @Valid Long groupId) throws CustomNotFoundException {
        List<CommentResponseDto> comments = commentService.getCommentsByGroupId(groupId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/get-comments-by-student/{studentId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentsByStudentId(@PathVariable @Valid Long studentId) throws CustomNotFoundException {
        List<CommentResponseDto> comments = commentService.getCommentsByStudentId(studentId);
        return ResponseEntity.ok(comments);
    }

}

