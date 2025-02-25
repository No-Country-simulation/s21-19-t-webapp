package com.backend.urbia.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String email;

    public JwtResponse() {}

    public JwtResponse(String token, Long id, String email) {
        this.token = token;
        this.id = id;
        this.email = email;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
