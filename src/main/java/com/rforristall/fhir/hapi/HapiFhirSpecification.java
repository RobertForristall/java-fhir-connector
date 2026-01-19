package com.rforristall.fhir.hapi;

import java.util.List;

import com.rforristall.fhir.auth.BasicAuthentication;
import com.rforristall.fhir.auth.FhirAuthentication;
import com.rforristall.fhir.auth.NoAuthentication;
import com.rforristall.fhir.spec.AbstractFhirSpecification;
import com.rforristall.fhir.spec.FhirDialect;
import com.rforristall.fhir.spec.FhirVersion;

public class HapiFhirSpecification extends AbstractFhirSpecification {

  public static HapiFhirSpecification createSpecWithNoAuth(
          String hostname,
          List<String> scopes,
          FhirVersion fhirVersion,
          boolean limitRate,
          long rateLimitWaitDuration) {
    return new HapiFhirSpecification(
            hostname,
            scopes,
            new NoAuthentication(),
            fhirVersion,
            limitRate,
            rateLimitWaitDuration);
  }

  public static HapiFhirSpecification createSpecWithBasicAuth(
          String hostname,
          List<String> scopes,
          FhirVersion fhirVersion,
          boolean limitRate,
          long rateLimitWaitDuration,
          String username,
          String password,
          String clientId) {
    return new HapiFhirSpecification(
            hostname,
            scopes,
            BasicAuthentication.createInternalUserBasicAuthentication(username, password, clientId),
            fhirVersion,
            limitRate,
            rateLimitWaitDuration);
  }

  private HapiFhirSpecification(
          String hostname,
          List<String> scopes,
          FhirAuthentication fhirAuth,
          FhirVersion fhirVersion,
          boolean limitRate,
          long rateLimitWaitDuration) {
    super(
            hostname,
            scopes,
            fhirAuth,
            fhirVersion,
            FhirDialect.HAPI,
            limitRate,
            rateLimitWaitDuration);
  }

}
