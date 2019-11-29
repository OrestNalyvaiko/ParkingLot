package com.nalyvaiko.security;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint,
    Serializable {

  private static Logger logger = LogManager.getLogger();

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
    logger.warn(
        authException.getClass().getName() + " with message " + authException
            .getMessage());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
  }
}