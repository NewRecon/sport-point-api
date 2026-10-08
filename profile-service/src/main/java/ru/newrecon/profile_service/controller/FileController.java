package ru.newrecon.profile_service.controller;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import ru.newrecon.profile_service.dto.UploadFileRs;
import ru.newrecon.profile_service.service.FileService;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public UploadFileRs upload(@AuthenticationPrincipal UUID userId, @RequestParam MultipartFile file){
        return new UploadFileRs(
            fileService.upload(userId, file)
        );
    }
}
