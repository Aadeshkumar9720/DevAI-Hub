package com.devaihub.backend.service.impl;

import com.devaihub.backend.dto.CreateCommentRequest;
import com.devaihub.backend.entity.Comment;
import com.devaihub.backend.entity.Task;
import com.devaihub.backend.entity.User;
import com.devaihub.backend.mapper.CommentMapper;
import com.devaihub.backend.repository.CommentRepository;
import com.devaihub.backend.repository.TaskRepository;
import com.devaihub.backend.repository.UserRepository;
import com.devaihub.backend.response.CommentResponse;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.service.interfaces.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import com.devaihub.backend.enums.ActivityType;
import com.devaihub.backend.service.interfaces.ActivityService;
import com.devaihub.backend.response.NotificationResponse;
import com.devaihub.backend.service.interfaces.NotificationService;
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final ActivityService activityService;
    private final NotificationService notificationService;
    public CommentServiceImpl(
            CommentRepository commentRepository,
            TaskRepository taskRepository,
            UserRepository userRepository,
            CommentMapper commentMapper,
             ActivityService activityService,
            NotificationService notificationService
    ) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.activityService = activityService;
        this.notificationService=notificationService;
    }

    @Override
    public CommentResponse createComment(
            Long taskId,
            CreateCommentRequest request,
            String username
    ) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new RuntimeException("Task not found"));

        User author = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = new Comment();

        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);
        activityService.logActivity(
                task.getProject(),
                author,
                ActivityType.COMMENT_ADDED,
                "Comment added to task '" + task.getTitle() + "'."
        );
        notificationService.sendNotification(
                new NotificationResponse(
                        "New Comment",
                        "A new comment was added to task '" + task.getTitle() + "'.",
                        "COMMENT_ADDED"
                )
        );
        return commentMapper.toResponse(savedComment);
    }

    @Override
    public List<CommentResponse> getCommentsByTask(Long taskId) {

        return commentRepository.findByTaskId(taskId)
                .stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
