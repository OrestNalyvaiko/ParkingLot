package com.nalyvaiko.exception;

public class SuchAddressExistException extends Exception {

  private String message = "Such address is already exist.";

  public SuchAddressExistException() {
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
