package com.rforristall.fhir.auth;

import java.net.http.HttpRequest.Builder;

public interface FhirAuthentication {
  
  Builder appendAuthentication(Builder requestBuilder);
  
}
