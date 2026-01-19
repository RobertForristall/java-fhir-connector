package com.rforristall.fhir.spec;

import java.util.List;

import com.rforristall.fhir.auth.FhirAuthentication;

public abstract class AbstractFhirSpecification implements FhirSpecification {
  
  protected static final long TOKEN_TTL = 4 * 60;

  private String hostname;
  private List<String> scopes;

  private FhirAuthentication fhirAuth;

  private FhirVersion fhirVersion;
  private FhirDialect fhirDialect;

  private boolean limitRate;
  private long rateLimitWaitDuration;

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
