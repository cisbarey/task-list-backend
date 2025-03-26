package com.atom.challenge.helper;

import com.atom.challenge.dto.response.TokenResponse;

public class TokenHelper {

    public static TokenResponse buildTokenResponse(String token) {
        return new TokenResponse(token);
    }
}
