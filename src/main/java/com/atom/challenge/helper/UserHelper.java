package com.atom.challenge.helper;

import com.atom.challenge.domain.User;
import com.atom.challenge.dto.request.UserRequest;
import com.atom.challenge.dto.response.UserResponse;

public class UserHelper {

    public static User mapToDocument(UserRequest userRequest) {
        User user = new User();
        user.setEmail(userRequest.getUsername());
        return user;
    }

    public static UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}
