package com.rforristall.fhir.cerner;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.List;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.rforristall.fhir.auth.FhirAuthentication;
import com.rforristall.fhir.auth.OauthAuthentication;
import com.rforristall.fhir.epic.EpicFhirSpecification;
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.keystore.KeyStoreAccessor;
import com.rforristall.fhir.keystore.KeyStoreSpec;
import com.rforristall.fhir.spec.AbstractFhirSpecification;
import com.rforristall.fhir.spec.FhirDialect;
import com.rforristall.fhir.spec.FhirVersion;

/**
 * FHIR specification for a Cerner Millennium server, extends {@link AbstractFhirSpecification}
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public class CernerFhirSpecification extends AbstractFhirSpecification {

  /**
   * Static builder function for a Cerner Millennium specification using OAuth authentication
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param oauthEndpoint {@link String}: The endpoint for OAuth pipeline for the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   * @param keyStoreSpec {@link KeyStoreSpec}: The specification of the keystore that will be used for OAuth
   * @param securityAlg {@link JWSAlgorithm}: The JWS algorithm that is used with OAuth
   * @param clientId {@link String}: clientId for the OAuth authentication 
   * @return {@link EpicFhirSpecification} using the {@link OauthAuthentication} schema
   * @throws KeyStoreException
   * @throws NoSuchAlgorithmException
   * @throws CertificateException
   * @throws UnrecoverableEntryException
   * @throws IOException
   * @throws JOSEException
   * @throws InterruptedException
   * @throws HttpErrorException
   */
  public static CernerFhirSpecification createSpecWithOauth(
          String hostname,
          String oauthEndpoint,
          List<String> scopes,
          FhirVersion fhirVersion,
          boolean limitRate,
          long rateLimitWaitDuration,
          KeyStoreSpec keyStoreSpec,
          JWSAlgorithm securityAlg,
          String clientId) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, JOSEException, InterruptedException, HttpErrorException {
    KeyStoreAccessor keyStoreAccessor = new KeyStoreAccessor(keyStoreSpec);
    return new CernerFhirSpecification(
            hostname,
            scopes,
            new OauthAuthentication(
                    keyStoreAccessor,
                    securityAlg,
                    clientId,
                    oauthEndpoint,
                    String.join("", scopes),
                    TOKEN_TTL),
            fhirVersion,
            limitRate,
            rateLimitWaitDuration);
  }

  /**
   * Private constructor used by static builders to make a Cerner Millennium specification
   * @param hostname {@link String}: The hostname of the FHIR server
   * @param scopes {@link List}<{@link String}>: The list of resources/actions that connections should request access to
   * @param fhirAuth {@link FhirAuthentication}: The FHIR authentication schema connections should use
   * @param fhirVersion {@link FhirVersion}: The version of the FHIR server that this specification references
   * @param limitRate boolean: If connections made using this specification should limit requests to the FHIR server
   * @param rateLimitWaitDuration long: The amount of time in seconds that connections made using this specification should wait between requests, only applied if limitRate is true
   */
  private CernerFhirSpecification(
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
            FhirDialect.CERNER,
            limitRate,
            rateLimitWaitDuration);
  }

}
