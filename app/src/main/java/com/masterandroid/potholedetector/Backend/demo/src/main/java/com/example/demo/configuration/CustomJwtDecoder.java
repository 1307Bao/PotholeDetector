package com.example.demo.configuration;

import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.service.AuthenticatedService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticatedService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        log.error("TOKEN: {}", token);
        var response = authenticationService.introspect(IntrospectRequest.builder()
                .token(token)
                .build());

        if (!response.isValid()) {
            throw new JwtException("Token Invalid");
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec spec = new SecretKeySpec(signerKey.getBytes(), "HS512");

            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(spec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }


        return nimbusJwtDecoder.decode(token);
    }
}
