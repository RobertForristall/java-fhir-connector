package com.rforristall.fhir.spec;

import java.util.List;

import com.rforristall.fhir.auth.FhirAuthentication;

/**
 * Interface for all FHIR specifications; implemented by {@link AbstractFhirSpecification} and its sub-classes
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public interface FhirSpecification {
  
  /**
   * Getter function for the hostname of the FHIR server that the specification is connecting to
   * @return {@link String} hostname
   */
  String getHostname();
  
  /**
   * Getter function for the list of scopes that the FHIR specification will request access to
   * @return {@link List}<{@link String}> scopes
   */
  List<String> getScopes();
  
  /**
   * Getter function for the FHIR authentication scheme that is configured for this specification
   * @return {@link FhirAuthentication} fhirAuth
   */
  FhirAuthentication getFhirAuth();
  
  /**
   * Getter function for the FHIR version of the specification
   * @return {@link FhirVersion} fhirVersion
   */
  FhirVersion getFhirVersion();

  /**
   * Getter function for the FHIR dialect of the specification
   * @return {@link FhirDialect} fhirDialect
   */
  FhirDialect getFhirDialect();
  
  /**
   * Getter function for if the specification should limit that rate of requests to the FHIR server
   * @return boolean limitRate
   */
  boolean isLimitRate();
  
  /**
   * Getter function for the duration in seconds that the specification should configure the connection to wait between requests
   * @return long rateLimitWaitDuration (In Seconds)
   */
  long getRateLimitWaitDuration();
}
