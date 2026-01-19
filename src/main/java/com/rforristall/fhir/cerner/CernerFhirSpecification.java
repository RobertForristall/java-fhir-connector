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
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.keystore.KeyStoreAccessor;
import com.rforristall.fhir.keystore.KeyStoreSpec;
import com.rforristall.fhir.spec.AbstractFhirSpecification;
import com.rforristall.fhir.spec.FhirDialect;
import com.rforristall.fhir.spec.FhirVersion;

public class CernerFhirSpecification extends AbstractFhirSpecification {

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
