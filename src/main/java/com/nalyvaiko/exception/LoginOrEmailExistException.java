package com.nalyvaiko.exception;

public class LoginOrEmailExistException extends Exception {

  private String message = "Login or email is already exist";

  public LoginOrEmailExistException() {
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
