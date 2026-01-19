package com.rforristall.fhir.spec;

/**
 * Enumeration for different FHIR dialects that can be connected to.
 * 
 * Currently supports:
 * <ul>
 * <li>Hapi on FHIR (<a>https://hapifhir.io/</a>)</li>
 * <li>Epic on FHIR (<a>https://fhir.epic.com/</a>)</li>
 * <li>Cerner Millenium (<a>https://docs.oracle.com/en/industries/health/millennium-platform-apis/mfrap/r4_overview.html</a>)</li>
 * </ul>
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public enum FhirDialect {
  
  HAPI("Hapi on FHIR"),
  EPIC("Epic on FHIR"),
  CERNER("Cerner Millenium");
  
  /**
   * The human readable name of the FHIR dialect for use in messages
   */
  private String name;
  
  /**
   * Private constructor for creating the enumerations with the dialect's name
   * @param name {@link String} Human readable name of the dialect
   */
  private FhirDialect(String name) {
    this.name = name;
  }
  
  /**
   * Getter function for the human readable name of the dialect
   * @return {@link String} name: Human readable name of the dialect
   */
  public String getName() {
    return name;
  }

}
