package com.nalyvaiko.controller;

import com.nalyvaiko.security.JwtRequest;
import com.nalyvaiko.security.JwtResponse;
import com.nalyvaiko.util.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class LoginController {

  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  private static Logger logger = LogManager.getLogger();

  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticateUser(
      @RequestBody JwtRequest authenticationRequest) {
    logger.trace("Request is come with username = " + authenticationRequest
        .getUsername());
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authenticationRequest.getUsername(),
            authenticationRequest.getPassword()));
    final String token = jwtTokenUtil
        .generateToken((UserDetails) authentication.getPrincipal());
    logger.info("Token is created = " + token);
    return ResponseEntity.ok(new JwtResponse(token));
  }
}
