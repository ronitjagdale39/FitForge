package com.example.fitforge.data.remote.dto;

public class RegisterResponse {
    private String message;
    private UserData user;
    private Tokens tokens;

    public String getMessage() {
        return message;
    }

    public UserData getUser() {
        return user;
    }

    public Tokens getTokens() {
        return tokens;
    }

    public static class UserData {
        private int id;
        private String name;
        private String email;

        public int getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
    }

    public static class Tokens {
        private String access_token;
        private String refresh_token;
        private String token_type;

        public String getAccessToken() { return access_token; }
        public String getRefreshToken() { return refresh_token; }
        public String getTokenType() { return token_type; }
    }
}