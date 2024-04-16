package fr.springsecurityjwtapp.spring_jwt_app.service;

import fr.springsecurityjwtapp.spring_jwt_app.dto.request.AuthenticationRequest;
import fr.springsecurityjwtapp.spring_jwt_app.dto.request.RegisterRequest;
import fr.springsecurityjwtapp.spring_jwt_app.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

}
