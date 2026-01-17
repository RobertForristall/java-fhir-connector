package com.rforristall.fhir.auth;

import java.net.http.HttpRequest.Builder;

public class NoAuthentication extends AbstractFhirAuthentication{

  @Override
  public Builder appendAuthentication(Builder requestBuilder) {
    // TODO Auto-generated method stub
    return requestBuilder;
  }
  
}
