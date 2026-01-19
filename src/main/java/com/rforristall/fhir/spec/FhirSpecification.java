package com.rforristall.fhir.spec;

import java.util.List;

import com.rforristall.fhir.auth.FhirAuthentication;

public interface FhirSpecification {
  
  String getHostname();
  
  List<String> getScopes();
  
  FhirAuthentication getFhirAuth();
  
  FhirVersion getFhirVersion();

  FhirDialect getFhirDialect();
  
  boolean isLimitRate();
  
  long getRateLimitWaitDuration();
}
