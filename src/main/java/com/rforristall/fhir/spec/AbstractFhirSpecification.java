package com.rforristall.fhir.spec;

import java.util.List;

import com.rforristall.fhir.auth.FhirAuthentication;

/**
 * Abstract class for FHIR specifications, implements {@link FhirSpecification} and extended by the following:
 * 
 * <ul>
 * <li>{@link HapiFhirSpecification}</li>
 * <li>{@link EpicFhirSpecification}</li>
 * <li>{@link CernerFhirSpecification}</li>
 * </ul>
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public abstract class AbstractFhirSpecification implements FhirSpecification {
  
  /**
   * The standard amount of time, in seconds, that Oauth tokens can be valid for FHIR servers
   */
  protected static final long TOKEN_TTL = 4 * 60;

  /**
   * The hostname of the FHIR server
   */
  private String hostname;
  
  /**
   * The scopes of the FHIR server which determine which resources/actions can be taken
   */
  private List<String> scopes;

  /**
   * The FHIR authentication schema that will handle authn/authz pipelines for FHIR connections
   */
  private FhirAuthentication fhirAuth;

  /**
   * The FHIR version of the server that the specification references
   */
  private FhirVersion fhirVersion;
  
  /**
   * The FHIR dialect of the server that the specification references
   */
  private FhirDialect fhirDialect;

  /**
   * If connections made using this specification should limit the rate of requests
   */
  private boolean limitRate;
  
  /**
   * The amount of time in seconds that connections made using this specification should wait between requests if limitRate is true
   */
  private long rateLimitWaitDuration;

  /**
   * Constructor for sub-classes to use when extending this class
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirAuth {@link FhirAuthentication}: The FHIR authentication schema connections should use
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param fhirDialect {@link FhirDialect}: The dialect of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   */
  protected AbstractFhirSpecification(
          String hostname,
          List<String> scopes,
          FhirAuthentication fhirAuth,
          FhirVersion fhirVersion,
          FhirDialect fhirDialect,
          boolean limitRate,
          long rateLimitWaitDuration) {
    super();
    if (!checkScopes(scopes))
      throw new IllegalArgumentException("The provided scope list is not valid!");
    this.hostname = hostname;
    this.scopes = scopes;
    this.fhirAuth = fhirAuth;
    this.fhirVersion = fhirVersion;
    this.fhirDialect = fhirDialect;
    this.limitRate = limitRate;
    this.rateLimitWaitDuration = rateLimitWaitDuration;
  }

  /**
   * Helper function for checking that the provided list of scopes are valid given the provided FHIR dialect and version
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @return true if the scopes are valid and false otherwise
   */
  protected boolean checkScopes(List<String> scopes) {
    // TODO Handle checking scopes for all different dialects
    return true;
  }

  @Override
  public String getHostname() {
    return hostname;
  }

  @Override
  public List<String> getScopes() {
    return scopes;
  }

  @Override
  public FhirAuthentication getFhirAuth() {
    return fhirAuth;
  }

  @Override
  public FhirVersion getFhirVersion() {
    return fhirVersion;
  }

  @Override
  public FhirDialect getFhirDialect() {
    return fhirDialect;
  }

  @Override
  public boolean isLimitRate() {
    return limitRate;
  }

  @Override
  public long getRateLimitWaitDuration() {
    return rateLimitWaitDuration;
  }

}
