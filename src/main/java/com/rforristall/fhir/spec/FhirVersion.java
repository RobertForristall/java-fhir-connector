package com.rforristall.fhir.spec;

public enum FhirVersion {
  
  DSTU2("DSTU2"),
  STU3("STU3"),
  R4("R4"),
  R4B("R4B"),
  R5("R5");

  private String name;
  
  private FhirVersion(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }
}
