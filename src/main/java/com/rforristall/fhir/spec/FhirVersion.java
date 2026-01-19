package com.rforristall.fhir.spec;

/**
 * Enumeration for different FHIR version that can be connected to
 * 
 * Currently supports:
 * <ul>
 * <li>DSTU2 <a>https://hl7.org/fhir/DSTU2/</a>
 * <li>STU3 <a>https://hl7.org/fhir/STU3/</a>
 * <li>R4 <a>https://hl7.org/fhir/R4/</a>
 * <li>R4B <a>https://hl7.org/fhir/R4B/</a>
 * <li>R5 <a>https://hl7.org/fhir/R5/</a>
 * </ul>
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public enum FhirVersion {
  
  DSTU2("DSTU2"),
  STU3("STU3"),
  R4("R4"),
  R4B("R4B"),
  R5("R5");

  /**
   * The human readable name of the FHIR version for use in messages
   */
  private String name;
  
  /**
   * Private constructor for creating the enumerations with the version's name
   * @param name {@link String} Human readable name of the dialect
   */
  private FhirVersion(String name) {
    this.name = name;
  }
  
  /**
   * Getter function for the human readable name of the version
   * @return {@link String} name: Human readable name of the version
   */
  public String getName() {
    return name;
  }
}
