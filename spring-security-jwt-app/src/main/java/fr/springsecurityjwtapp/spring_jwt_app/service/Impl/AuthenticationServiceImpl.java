package fr.springsecurityjwtapp.spring_jwt_app.service.Impl;

import fr.springsecurityjwtapp.spring_jwt_app.dto.request.AuthenticationRequest;
import fr.springsecurityjwtapp.spring_jwt_app.dto.request.RegisterRequest;
import fr.springsecurityjwtapp.spring_jwt_app.dto.response.AuthenticationResponse;
import fr.springsecurityjwtapp.spring_jwt_app.entity.UserEntity;
import fr.springsecurityjwtapp.spring_jwt_app.enums.TokenType;
import fr.springsecurityjwtapp.spring_jwt_app.repository.UserRepository;
import fr.springsecurityjwtapp.spring_jwt_app.service.AuthenticationService;
import fr.springsecurityjwtapp.spring_jwt_app.service.JwtService;
import fr.springsecurityjwtapp.spring_jwt_app.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    /**
     * Enregistre un nouvel utilisateur dans l'application.
     *
     * @param request Les informations de l'utilisateur à enregistrer
     * @return Une réponse d'authentification contenant les informations de l'utilisateur enregistré
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Tentative d'enregistrement d'un nouvel utilisateur avec l'email : {}", request.getEmail());
        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            log.warn("Enregistrement échoué : un utilisateur avec l'email {} existe déjà.", request.getEmail());
            throw new IllegalArgumentException("Un utilisateur avec cet e-mail existe déjà.");
        }

        UserEntity userEntity = UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userEntity = userRepository.save(userEntity);
        log.info("Utilisateur {} enregistré avec succès.", userEntity.getEmail());

        var jwt = jwtService.generateToken(userEntity);
        var refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
        var roles = userEntity.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .email(userEntity.getEmail())
                .id(Long.valueOf(userEntity.getId()))
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    /**
     * Authentifie un utilisateur dans l'application.
     *
     * @param request Les informations d'authentification de l'utilisateur
     * @return Une réponse d'authentification contenant les informations de l'utilisateur authentifié
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentative d'authentification pour l'email : {}", request.getEmail());
        Optional<UserEntity> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            log.warn("Authentification échouée : adresse e-mail {} invalide.", request.getEmail());
            throw new IllegalArgumentException("Adresse e-mail invalide.");
        }

        UserEntity userEntity = optionalUser.get();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        log.info("Authentification réussie pour l'utilisateur : {}", userEntity.getEmail());

        var jwt = jwtService.generateToken(userEntity);
        var refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
        var roles = userEntity.getRole().getAuthorities()
                .stream()
                .map(SimpleGrantedAuthority::getAuthority)
                .toList();

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .email(userEntity.getEmail())
                .id(Long.valueOf(userEntity.getId()))
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }
}