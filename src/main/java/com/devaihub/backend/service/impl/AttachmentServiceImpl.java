package com.devaihub.backend.service.impl;

import com.devaihub.backend.entity.Attachment;
import com.devaihub.backend.entity.Task;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.mapper.AttachmentMapper;
import com.devaihub.backend.repository.AttachmentRepository;
import com.devaihub.backend.repository.TaskRepository;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.response.AttachmentResponse;
import com.devaihub.backend.service.interfaces.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private static final String UPLOAD_DIR = "uploads";

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AttachmentMapper attachmentMapper;
    private final ActivityService activityService;
    private final NotificationService notificationService;
    public AttachmentServiceImpl(
            AttachmentRepository attachmentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            AttachmentMapper attachmentMapper,ActivityService activityService,
            NotificationService notificationService
    ) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.attachmentMapper = attachmentMapper;
        this.activityService = activityService;
        this.notificationService=notificationService;
    }
    @Transactional
    @Override
    public AttachmentResponse uploadFile(
            Long taskId,
            MultipartFile file,
            String username
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        try {

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path path = Paths.get(UPLOAD_DIR, fileName);

            Files.copy(file.getInputStream(), path);

            Attachment attachment = new Attachment();

            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileType(file.getContentType());
            attachment.setFileSize(file.getSize());
            attachment.setFilePath(path.toString());
            attachment.setTask(task);
            attachment.setUploadedBy(user);

            Attachment saved = attachmentRepository.save(attachment);
            activityService.logActivity(
                    task.getProject(),
                    user,
                    ActivityType.ATTACHMENT_UPLOADED,
                    "Attachment '" + saved.getFileName() + "' uploaded."
            );
            notificationService.sendNotification(
                    new NotificationResponse(
                            "Attachment Uploaded",
                            "File '" + saved.getFileName() + "' uploaded.",
                            "ATTACHMENT_UPLOADED"
                    ),
                    task.getProject().getOwner().getUsername()
            );
            return attachmentMapper.toResponse(saved);

        } catch (IOException e) {
            throw new RuntimeException("File upload failed");
        }
    }
    @Transactional
    @Override
    public List<AttachmentResponse> getAttachments(Long taskId) {

        return attachmentRepository.findByTaskId(taskId)
                .stream()
                .map(attachmentMapper::toResponse)
                .toList();
    }
    @Transactional
    @Override
    public void deleteAttachment(Long attachmentId, String username) {

        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() ->
                        new RuntimeException("Attachment not found"));

        if (!attachment.getUploadedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this attachment");
        }

        try {
            Files.deleteIfExists(Paths.get(attachment.getFilePath()));
        } catch (IOException ignored) {
        }

        attachmentRepository.delete(attachment);
    }
    @Override
    @Transactional(readOnly = true)
    public Resource getAttachmentFile(Long attachmentId) {

        Attachment attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() ->
                        new RuntimeException("Attachment not found"));

        Path path = Path.of(
                attachment.getFilePath()
        ).toAbsolutePath().normalize();

        if (!Files.exists(path)) {
            throw new RuntimeException(
                    "Attachment file not found"
            );
        }

        if (!Files.isRegularFile(path)) {
            throw new RuntimeException(
                    "Attachment path is not a file"
            );
        }

        return new FileSystemResource(path);
    }
    @Override
    @Transactional(readOnly = true)
    public AttachmentResponse getAttachment(
            Long attachmentId
    ) {

        Attachment attachment = attachmentRepository
                .findById(attachmentId)
                .orElseThrow(() ->
                        new RuntimeException("Attachment not found"));

        return attachmentMapper.toResponse(attachment);
    }
}
