package com.example.edusystem.controller;

import com.example.edusystem.dto.GroupRequestDto;
import com.example.edusystem.exceptions.CustomNotFoundException;
import com.example.edusystem.response.CommonResponse;
import com.example.edusystem.response.GroupResponseDto;
import com.example.edusystem.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/create-group")
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody @Valid GroupRequestDto groupRequestDto) throws CustomNotFoundException, BadRequestException {
        GroupResponseDto group = groupService.createGroup(groupRequestDto);
        return ResponseEntity.ok(group);
    }

    @PutMapping("/update-group/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long id,
                                                        @RequestBody @Valid
                                                        GroupRequestDto groupRequestDto) throws CustomNotFoundException {
        GroupResponseDto groupUpdate = groupService.updateGroup(id, groupRequestDto);
        return ResponseEntity.ok(groupUpdate);
    }

    @GetMapping("/get-group/{id}")
    public ResponseEntity<GroupResponseDto> getGroup(@PathVariable Long id) throws CustomNotFoundException {
        GroupResponseDto group = groupService.getGroup(id);
        return ResponseEntity.ok(group);
    }
    @GetMapping("/get-all-group")
    public ResponseEntity<List<GroupResponseDto>> getAllGroups() {
        List<GroupResponseDto> allGroup = groupService.getAllGroup();
        return ResponseEntity.ok(allGroup);
    }

    @DeleteMapping("/delete-group/{id}")
    public ResponseEntity<CommonResponse> deleteGroup(@PathVariable @Valid Long id) throws CustomNotFoundException {
        CommonResponse commonResponse = groupService.deleteGroup(id);
        return ResponseEntity.ok(commonResponse);
    }
}
