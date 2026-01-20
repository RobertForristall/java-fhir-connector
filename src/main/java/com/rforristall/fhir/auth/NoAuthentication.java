package com.rforristall.fhir.auth;

import java.net.http.HttpRequest.Builder;

public class NoAuthentication extends AbstractFhirAuthentication{
  
  public NoAuthentication() {
    
  }

  @Override
  public Builder appendAuthentication(Builder requestBuilder) {
    // TODO Auto-generated method stub
    return requestBuilder;
  }
  
}
