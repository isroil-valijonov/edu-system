package com.example.edusystem.config;

import com.example.edusystem.dto.SignUpRequest;
import com.example.edusystem.exceptions.CustomException;
import com.example.edusystem.repository.ManagementRepository;
import com.example.edusystem.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final ManagementService managementService;
    private final ManagementRepository managementRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (managementRepository.count() == 0) {
            SignUpRequest manager = new SignUpRequest("Manager", "944906677", "Salom@123");
            try {
                managementService.createManagement(manager);
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
