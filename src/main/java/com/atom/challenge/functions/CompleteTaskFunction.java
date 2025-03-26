package com.atom.challenge.functions;

import com.atom.challenge.domain.Task;
import com.atom.challenge.dto.response.ErrorResponse;
import com.atom.challenge.exception.DataNotFoundException;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.atom.challenge.helper.TaskHelper.mapToResponse;

public class CompleteTaskFunction extends Function implements HttpFunction {

    public CompleteTaskFunction() throws IOException {
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
            String taskId = extractTaskId(request.getPath());
            Task task = firestore.collection("tasks").document(taskId).get().get().toObject(Task.class);
            if (task == null || !task.getUserId().equals(userId)) {
                throw new DataNotFoundException("Task not found or not authorized");
            }

            task.setCompleted(true);
            firestore.collection("tasks").document(taskId).set(task).get();

            response(response, mapToResponse(task), 200);
        } catch (IllegalArgumentException e) {
            response(response, new ErrorResponse(401, e.getMessage()), 401);
        } catch (DataNotFoundException e) {
            response(response, new ErrorResponse(404, e.getMessage()), 404);
        } catch (ExecutionException | InterruptedException e) {
            response(response, new ErrorResponse(500, "Internal server error"), 500);
        }
    }
}