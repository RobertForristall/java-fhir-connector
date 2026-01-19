package com.rforristall.fhir.spec;

public enum FhirDialect {
  
  HAPI("Hapi on FHIR"),
  EPIC("Epic on FHIR"),
  CERNER("Cerner Millenium");
  
  private String name;
  
  private FhirDialect(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

}
