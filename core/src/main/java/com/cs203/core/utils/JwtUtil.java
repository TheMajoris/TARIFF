package com.cs203.core.utils;

import com.cs203.core.exception.JwtCreationException;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Component
public class JwtUtil {
  @Value("${jwt.access.duration}")
  private long expirationTime;

  @Value("${jwt.issuer}")
  private String issuer;

  private final RSAKey rsaKey;

  private final JwtEncoder jwtEncoder;

  @Autowired
  public JwtUtil(JwtEncoder jwtEncoder, RSAKey rsaKey) {
    this.jwtEncoder = jwtEncoder;
    this.rsaKey = rsaKey;
  }

  public String generateToken(UserDetails userDetails) {
    try {
      JwtClaimsSet claimsSet =
          JwtClaimsSet.builder()
              .issuer(issuer)
              .id(UUID.randomUUID().toString())
              .audience(
                  List.of(
                      // TODO: chg to wtv admin svc is
                      "user-service", "admin-service"))
              .issuedAt(Instant.now())
              .expiresAt(Instant.now().plusSeconds(expirationTime))
              .subject(userDetails.getUsername())
              .claim(
                  "scope",
                  userDetails.getAuthorities().stream()
                      .map(GrantedAuthority::getAuthority)
                      .collect(Collectors.joining(" ")))
              .build();

      return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    } catch (Exception e) {
      throw new JwtCreationException("Wthelly sumn wrong!", e);
    }
  }
}
