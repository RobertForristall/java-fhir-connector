package com.rforristall.fhir.spec;

import java.util.List;

import com.rforristall.fhir.hapi.HapiFhirSpecification;

public class HapiTestSpecification {
  
  private static final String TEST_HOST = "http://localhost:8080/fhir/";
  
  public static HapiFhirSpecification createHapiSpec() {
    return HapiFhirSpecification.createSpecWithNoAuth(TEST_HOST, List.of(""), FhirVersion.R4, false, 0);
  }

}
