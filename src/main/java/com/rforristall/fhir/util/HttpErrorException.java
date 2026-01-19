package com.rforristall.fhir.util;

import java.util.Objects;

public class HttpErrorException extends Exception {
  
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  
  private int statusCode;
  private String msg;

  //TODO handle different msgs based on provided status code
  public static HttpErrorException createExceptionFromStatusCode(int statusCode, String responseBody) {
    return new HttpErrorException(statusCode, responseBody);
  }
  
  private HttpErrorException(int statusCode, String msg) {
    this.statusCode = statusCode;
    this.msg = msg;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getMsg() {
    return msg;
  }

  @Override
  public int hashCode() {
    return Objects.hash(msg, statusCode);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    HttpErrorException other = (HttpErrorException) obj;
    return Objects.equals(msg, other.msg) && statusCode == other.statusCode;
  }

  @Override
  public String toString() {
    return "HttpErrorException [statusCode=" + statusCode + ", msg=" + msg + "]";
  }
  
  
  
  

}
