package com.example.demo.service;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.RegisterResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginService {
    UserRepository userRepository;
    RegisterService registerService;

    @NonFinal
    @Value("${jwt.clientKey}")
    protected String clientKey;

    @NonFinal
    @Value("${jwt.using-time}")
    protected long USING_TIME;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;

    public LoginResponse loginByUser(LoginByUserRequest request) throws AppRunTimeException {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppRunTimeException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppRunTimeException(ErrorCode.WRONG_PASSWORD);
        }

        var token = generateToken(user);
        return LoginResponse.builder().token(token)
                .name(user.getName())
                .username(user.getUsername()).build();
    }

    public LoginResponse loginByGmail(LoginByGmailRequest request) throws AppRunTimeException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(clientKey))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(request.getToken());
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (!userRepository.existsByEmail(payload.getEmail())) {
                RegisterResponse registerResponse = registerService
                        .registerByGmail(RegisterByGmailRequest
                        .builder().token(request.getToken()).build());

                return LoginResponse.builder().token(registerResponse.getToken()).build();
            }
            var user = userRepository.findByEmail(payload.getEmail())
                    .orElseThrow(() -> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));

            String genToken = generateToken(user);

            return LoginResponse.builder().token(genToken)
                    .username(user.getUsername())
                    .name(user.getName()).build();

        } catch (GeneralSecurityException | IOException | AppRunTimeException e) {
            throw new AppRunTimeException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public LoginResponse loginByFacebook(LoginByFacebookRequest request) throws AppRunTimeException {
        String email = request.getEmail();
        if (!userRepository.existsByEmail(email)) {
            RegisterByFacebookRequest facebookRequest = RegisterByFacebookRequest.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .build();

            RegisterResponse registerResponse = registerService.registerByFacebook(facebookRequest);

            return LoginResponse.builder().token(registerResponse.getToken()).build();
        } else {
            User user = userRepository.findByEmail(email).orElseThrow(()->
                    new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));
            String token = generateToken(user);
            return LoginResponse.builder().token(token)
                    .name(user.getName())
                    .username(user.getUsername()).build();
        }
    }

    public String generateToken(User user) {
        JWSHeader header =  new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer("Moiiu.com")
                .claim("roles", "USER")
                .expirationTime(new Date(
                        Instant.now().plus(USING_TIME, ChronoUnit.HOURS).toEpochMilli()
                ))
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
