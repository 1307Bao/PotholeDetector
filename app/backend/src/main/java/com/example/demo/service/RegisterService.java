package com.example.demo.service;

import com.example.demo.dto.request.RegisterByFacebookRequest;
import com.example.demo.dto.request.RegisterByGmailRequest;
import com.example.demo.dto.request.RegisterByUserRequest;
import com.example.demo.dto.response.RegisterResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppRunTimeException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import static java.lang.Math.*;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RegisterService {
    long RANDOM = 1000000;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @NonFinal
    @Value("${jwt.clientKey}")
    protected String clientKey;

    @NonFinal
    @Value("${jwt.using-time}")
    protected long USING_TIME;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;

    private String createUsername() {
        String username;
        do {
            username = "USER" + round(random() * RANDOM);
        } while (userRepository.existsByUsername(username));

        return username;
    }

    public RegisterResponse registerByUser(RegisterByUserRequest request) throws AppRunTimeException {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppRunTimeException(ErrorCode.USER_EXISTS);
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppRunTimeException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(createUsername());

        userRepository.save(user);
        String token = generateToken(user);

        return RegisterResponse.builder().token(token)
                .username(user.getUsername())
                .name(user.getName()).build();
    }

    public RegisterResponse registerByGmail(RegisterByGmailRequest request) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(clientKey))
                .build();

        User user = null;
        String token = "";

        try {
            GoogleIdToken idToken = verifier.verify(request.getToken());
            GoogleIdToken.Payload payload = idToken.getPayload();
            if (userRepository.existsByEmail(payload.getEmail())) {
                user = userRepository.findByEmail(payload.getEmail())
                        .orElseThrow(() -> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));

                token = generateToken(user);
            } else {
                String username = createUsername();

                String password = passwordEncoder.encode(String.valueOf(round(random() * RANDOM)));
                user = User.builder().email(payload.getEmail())
                        .name(username)
                        .username(username)
                        .password(password)
                        .build();

                userRepository.save(user);
                token = generateToken(user);
            }

            return RegisterResponse.builder().token(token)
                    .name(user.getName())
                    .username(user.getUsername()).build();

        } catch (GeneralSecurityException | IOException | AppRunTimeException e) {
            throw new RuntimeException(e);
        }
    }

    public RegisterResponse registerByFacebook(RegisterByFacebookRequest request) throws AppRunTimeException {
        String email = request.getEmail();
        User user = null;
        String username = createUsername();
        String password = String.valueOf(round(random() * RANDOM));
        if (userRepository.existsByEmail(email)) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new AppRunTimeException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        } else {
            user = User.builder().username(username).password(passwordEncoder.encode(password))
                    .email(email).name(request.getName()).build();

            userRepository.save(user);
        }

        String token = generateToken(user);
        return RegisterResponse.builder().token(token)
                .username(username)
                .name(user.getName()).build();
    }

    private String generateToken(User user) {
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
