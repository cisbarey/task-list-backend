package com.atom.challenge.functions;

import com.atom.challenge.domain.Task;
import com.atom.challenge.dto.request.TaskRequest;
import com.atom.challenge.dto.response.ErrorResponse;
import com.atom.challenge.dto.response.TaskResponse;
import com.atom.challenge.exception.DataNotFoundException;
import com.google.cloud.Timestamp;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.atom.challenge.helper.TaskHelper.mapToDocument;
import static com.atom.challenge.helper.TaskHelper.mapToResponse;

public class CreateTaskFunction extends Function implements HttpFunction {

    public CreateTaskFunction() throws IOException {
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
            TaskRequest taskRequest = bodyFromRequest(request, TaskRequest.class);

            if (!firestore.collection("users").document(userId).get().get().exists()) {
                throw new DataNotFoundException("User not found");
            }

            Task task = mapToDocument(taskRequest, userId);
            task.setCreatedAt(Timestamp.now());
            task.setCompleted(false);
            String id = firestore.collection("tasks").document().getId();
            task.setId(id);
            firestore.collection("tasks").document(id).set(task).get();

            TaskResponse taskResponse = mapToResponse(task);
            response(response, taskResponse, 201);
        } catch (IllegalArgumentException e) {
            response(response, new ErrorResponse(401, e.getMessage()), 401);
        } catch (DataNotFoundException e) {
            response(response, new ErrorResponse(400, e.getMessage()), 400);
        } catch (ExecutionException | InterruptedException e) {
            response(response, new ErrorResponse(500, "Internal server error"), 500);
        }
    }
}