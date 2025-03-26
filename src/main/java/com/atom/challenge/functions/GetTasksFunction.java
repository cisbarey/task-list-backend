package com.atom.challenge.functions;

import com.atom.challenge.domain.Task;
import com.atom.challenge.dto.response.ErrorResponse;
import com.atom.challenge.dto.response.TaskResponse;
import com.atom.challenge.helper.TaskHelper;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GetTasksFunction extends Function implements HttpFunction {

    public GetTasksFunction() throws IOException {
        super();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            handleOptions(request, response);
            return;
        }
        try {
            String userId = getUserIdFromRequest(request);

            List<Task> tasks = firestore.collection("tasks")
                    .whereEqualTo("userId", userId)
                    .orderBy("createdAt")
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .map(doc -> {
                        Task task = doc.toObject(Task.class);
                        task.setId(doc.getId());
                        return task;
                    })
                    .toList();

            List<TaskResponse> taskResponses = tasks.stream()
                    .map(TaskHelper::mapToResponse)
                    .collect(Collectors.toList());
            response(response, taskResponses, 200);
        } catch (IllegalArgumentException e) {
            response(response, new ErrorResponse(401, e.getMessage()), 401);
        } catch (ExecutionException | InterruptedException e) {
            response(response, new ErrorResponse(500, "Internal server error"), 500);
        }
    }
}