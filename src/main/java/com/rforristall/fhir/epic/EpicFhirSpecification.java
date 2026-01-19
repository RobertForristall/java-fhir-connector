package com.rforristall.fhir.epic;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.List;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.rforristall.fhir.auth.BasicAuthentication;
import com.rforristall.fhir.auth.FhirAuthentication;
import com.rforristall.fhir.auth.OauthAuthentication;
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.keystore.KeyStoreAccessor;
import com.rforristall.fhir.keystore.KeyStoreSpec;
import com.rforristall.fhir.spec.AbstractFhirSpecification;
import com.rforristall.fhir.spec.FhirDialect;
import com.rforristall.fhir.spec.FhirVersion;

public class EpicFhirSpecification extends AbstractFhirSpecification {

  public static EpicFhirSpecification createSpecWithBasicAuth(
          String hostname,
          List<String> scopes,
          FhirVersion fhirVersion,
          boolean limitRate,
          long rateLimitWaitDuration,
          String username,
          String password,
          String clientId) {
    return new EpicFhirSpecification(
            hostname,
            scopes,
            BasicAuthentication.createExternalUserBasicAuthentication(username, password, clientId),
            fhirVersion,
            limitRate,
            rateLimitWaitDuration);
  }

  public static EpicFhirSpecification createSpecWithOauth(
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
    return new EpicFhirSpecification(
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

  private EpicFhirSpecification(
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
            FhirDialect.EPIC,
            limitRate,
            rateLimitWaitDuration);
  }

}
