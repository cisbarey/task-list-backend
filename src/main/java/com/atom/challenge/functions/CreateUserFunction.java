package com.atom.challenge.functions;

import com.atom.challenge.domain.User;
import com.atom.challenge.dto.request.UserRequest;
import com.atom.challenge.dto.response.ErrorResponse;
import com.atom.challenge.exception.UserException;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.atom.challenge.helper.UserHelper.mapToDocument;
import static com.atom.challenge.helper.UserHelper.mapToResponse;

public class CreateUserFunction extends Function implements HttpFunction {

    public CreateUserFunction() throws IOException {
        super();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            handleOptions(request, response);
            return;
        }
        try {
            UserRequest userRequest = bodyFromRequest(request, UserRequest.class);

            if (firestore.collection("users")
                    .whereEqualTo("email", userRequest.getUsername())
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .findFirst()
                    .isPresent()) {
                throw new UserException("User already exists");
            }

            User user = mapToDocument(userRequest);
            String id = firestore.collection("users").document().getId();
            user.setId(id);
            firestore.collection("users").document(id).set(user).get();

            response(response, mapToResponse(user), 202);
        } catch (UserException e) {
            response(response, new ErrorResponse(400, e.getMessage()), 400);
        } catch (ExecutionException | InterruptedException e) {
            response(response, new ErrorResponse(500, "Internal server error"), 500);
        }
    }
}