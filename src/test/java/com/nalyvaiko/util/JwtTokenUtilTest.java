package com.nalyvaiko.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.ExpiredJwtException;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringRunner.class)
public class JwtTokenUtilTest {

  private JwtTokenUtil jwtTokenUtil;
  @Mock
  private User user;

  @Before
  public void setup() {
    when(user.getUsername()).thenReturn("uliana1");
    when(user.getAuthorities()).thenReturn(
        Collections.singletonList(() -> "ROLE_USER"));
    jwtTokenUtil = new JwtTokenUtil();
    ReflectionTestUtils.setField(jwtTokenUtil, "secret", "javainuse");
    ReflectionTestUtils.setField(jwtTokenUtil, "tokenValidity", 3600);
  }

  @Test
  public void shouldGenerateTokenOnUserDetailsInfo() {
    assertNotNull(jwtTokenUtil.generateToken(user));
    verify(user, times(1)).getUsername();
    verify(user, times(1)).getAuthorities();
  }

  @Test
  public void shouldBeEqualUsernameAndUsernameFromToken() {
    String token = jwtTokenUtil.generateToken(user);
    assertEquals("Expected and actual username`s are not equal ", "uliana1",
        jwtTokenUtil.getUsernameFromToken(token));
  }

  @Test
  public void shouldValidateToken() {
    String token = jwtTokenUtil.generateToken(user);
    assertTrue("Token validation failed",
        jwtTokenUtil.validateToken(token, user));
  }

  @Test
  public void shouldNotValidateTokenInvalidUsername() {
    String token = jwtTokenUtil.generateToken(user);
    when(user.getUsername()).thenReturn("uliana2");
    assertFalse("Validation correct even if username is changed ",
        jwtTokenUtil.validateToken(token, user));
  }

  @Test(expected = ExpiredJwtException.class)
  public void shouldThrowExpiredJwtExceptionCauseTimeExpired()
      throws Exception {
    ReflectionTestUtils.setField(jwtTokenUtil, "tokenValidity", 1);

    String token = jwtTokenUtil.generateToken(user);
    Thread.sleep(2000);
    jwtTokenUtil.validateToken(token, user);
  }
}
