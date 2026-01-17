package com.rforristall.fhir.auth;

import java.net.http.HttpRequest.Builder;
import java.util.Base64;

public class BasicAuthentication extends AbstractFhirAuthentication{

  private String token;
  private String username;
  private String clientId;
  private UserType userType;
  
  public static BasicAuthentication createInternalUserBasicAuthentication(String username, String password, String clientId) {
    return new BasicAuthentication(username, password, clientId, UserType.INTERNAL);
  }
  
  public static BasicAuthentication createExternalUserBasicAuthentication(String username, String password, String clientId) {
    return new BasicAuthentication(username, password, clientId, UserType.EXTERNAL);
  }
  
  private BasicAuthentication(String username, String password, String clientId, UserType userType) {
    if (username == "" || username == null) {
      
    }
    if (password == "" || password == null) {
      
    }
    if (clientId == "" || clientId == null) {
      
    }
    this.token = "Basic " + Base64.getEncoder().encodeToString(String.format("emp$%s:%s", username, password).getBytes());
    this.username = username;
    this.clientId = clientId;
    this.userType = userType;
  }
  
  @Override
  public Builder appendAuthentication(Builder requestBuilder) {
    requestBuilder.header(AUTH_HEADER, token);
    requestBuilder.header("Epic-Client-ID", clientId);
    requestBuilder.header("Epic-User-ID", username);
    requestBuilder.header("Epic-User-IDType", userType.value());
    return requestBuilder;
  }
  
  private enum UserType {
    INTERNAL("INTERNAL"),
    EXTERNAL("EXTERNAL");
    
    private String value;
    
    private UserType(String value) {
      this.value = value;
    }
    
    public String value() {
      return value;
    }
  }

}
