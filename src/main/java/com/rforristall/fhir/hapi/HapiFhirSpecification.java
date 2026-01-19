package com.rforristall.fhir.hapi;

import java.util.List;

import com.rforristall.fhir.auth.BasicAuthentication;
import com.rforristall.fhir.auth.FhirAuthentication;
import com.rforristall.fhir.auth.NoAuthentication;
import com.rforristall.fhir.spec.AbstractFhirSpecification;
import com.rforristall.fhir.spec.FhirDialect;
import com.rforristall.fhir.spec.FhirVersion;

/**
 * FHIR specification for a Hapi on FHIR server, extends {@link AbstractFhirSpecification}
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public class HapiFhirSpecification extends AbstractFhirSpecification {

  /**
   * Static builder function for a Hapi on FHIR specification using no authentication
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   * @return {@link HapiFhirSpecification} using the {@link NoAuthentication} schema
   */
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

  /**
   * Static builder function for a Hapi on FHIR specification using basic authentication
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   * @param username {@link String}: username for the basic authentication
   * @param password {@link String}: password for the basic authentication
   * @param clientId {@link String}: clientId for the basic authentication
   * @return {@link HapiFhirSpecification} using the {@link BasicAuthentication} schema
   */
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

  /**
   * Private constructor used by static builders to make a Hapi on FHIR specification
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirAuth {@link FhirAuthentication}: The FHIR authentication schema connections should use
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   */
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
