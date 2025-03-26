package com.atom.challenge.helper;

import com.atom.challenge.domain.Task;
import com.atom.challenge.dto.request.TaskRequest;
import com.atom.challenge.dto.response.TaskResponse;

public class TaskHelper {

    public static Task mapToDocument(TaskRequest request, String userId) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setUserId(userId);
        return task;
    }

    public static TaskResponse mapToResponse(Task task) {
        return new TaskResponse(task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt());
    }
}