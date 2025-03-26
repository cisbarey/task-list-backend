package com.atom.challenge.functions;

import com.atom.challenge.domain.User;
import com.atom.challenge.dto.request.UserRequest;
import com.atom.challenge.dto.response.ErrorResponse;
import com.atom.challenge.exception.DataNotFoundException;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static com.atom.challenge.helper.TokenHelper.buildTokenResponse;

public class LoginFunction extends Function implements HttpFunction {

    public LoginFunction() throws IOException {
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

            User user = firestore.collection("users")
                    .whereEqualTo("email", userRequest.getUsername())
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .map(doc -> {
                        User u = doc.toObject(User.class);
                        u.setId(doc.getId());
                        return u;
                    })
                    .findFirst()
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user.getId());
            response(response, buildTokenResponse(token), 200);
        } catch (DataNotFoundException e) {
            response(response, new ErrorResponse(404, e.getMessage()), 404);
        } catch (ExecutionException | InterruptedException e) {
            response(response, new ErrorResponse(500, "Internal server error"), 500);
        }
    }
}