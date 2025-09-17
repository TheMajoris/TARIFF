package com.cs203.core.config;

import com.cs203.core.exception.JwtCreationException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JwtConfig {
    private Logger logger = LoggerFactory.getLogger(JwtConfig.class);

    @Value("${jwt.id}")
    private String keyId;

    @Bean
    public RSAPublicKey rsaPublicKey(
            @Value("${jwt.public.key}") String publicKeyString
    ) throws InvalidKeySpecException, NoSuchAlgorithmException {
        logger.info("Generating RSA Public Key");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(
                new X509EncodedKeySpec(publicKeyBytes)
        );
    }

    @Bean
    RSAPrivateKey rsaPrivateKey(@Value("${jwt.private.key}") String privateKeyStr) {
        try {
            logger.info("Generating RSA Private Key");

            if (privateKeyStr == null || privateKeyStr.isEmpty()) {
                throw new InvalidKeySpecException("JWT Private key not found");
            }

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(spec);
            logger.info("RSA Private Key created");
            return (RSAPrivateKey) privateKey;
        } catch (InvalidKeySpecException e) {
            throw new JwtCreationException("Invalid JWT Private Key", e);
        } catch (NoSuchAlgorithmException e) {
            throw new JwtCreationException("RSA algo got problem", e);
        }
    }

    @Bean
    public RSAKey rsaKey(RSAPrivateKey rsaPrivateKey, RSAPublicKey rsaPublicKey) {
        return new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(keyId)
                .build();
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey)
                .build();
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}
