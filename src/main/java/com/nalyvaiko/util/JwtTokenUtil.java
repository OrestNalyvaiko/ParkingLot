package com.nalyvaiko.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil implements Serializable {

  @Value("${jwt.secret}")
  private String secret;
  @Value("${jwt.tokenValidity}")
  private long tokenValidity;
  private static Logger logger = LogManager.getLogger();

  public String getUsernameFromToken(String token) {
    logger.info("Get username from token");
    return getClaimFromToken(token, Claims::getSubject);
  }

  private Date getExpirationDateFromToken(String token) {
    logger.info("Get expiration date from token");
    return getClaimFromToken(token, Claims::getExpiration);
  }

  private <T> T getClaimFromToken(String token,
      Function<Claims, T> claimsResolver) {
    logger.info("Get claim from token");
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    logger.info("Get claims from token");
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    logger.info("Check if token expired");
    return expiration.before(new Date());
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    logger.info("Generate token on userDetails object");
    return doGenerateToken(claims, userDetails);
  }

  private String doGenerateToken(Map<String, Object> claims,
      UserDetails userDetails) {
    return Jwts.builder().setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            new Date(System.currentTimeMillis() + tokenValidity * 1000))
        .claim("role",
            userDetails.getAuthorities().iterator().next().getAuthority())
        .signWith(SignatureAlgorithm.HS512, secret).compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String username = getUsernameFromToken(token);
    logger.info("Validate token on userDetails object");
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(
        token));
  }
}
