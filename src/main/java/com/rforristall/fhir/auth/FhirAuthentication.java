package com.rforristall.fhir.auth;

import java.io.IOException;
import java.net.http.HttpRequest.Builder;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.rforristall.fhir.util.HttpErrorException;

public interface FhirAuthentication {
  
  Builder appendAuthentication(Builder requestBuilder) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, ParseException, IOException, JOSEException, InterruptedException, HttpErrorException;
  
}
