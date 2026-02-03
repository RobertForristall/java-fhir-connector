package com.rforristall.fhir.conn;

import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Map;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Resource;

import com.google.common.net.HttpHeaders;
import com.nimbusds.jose.JOSEException;
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.spec.FhirSpecification;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class AbstractFhirConnection implements FhirConnection {
  
  private static final String ACCEPT_HEADER_VALUE = "application/fhir+json";
  private static final String HEADER_VALUE_NEW_ID = "Location";
  
  private FhirSpecification fhirSpec;
  private FhirContext fhirContext;
  private IParser parser;
  
  protected AbstractFhirConnection(FhirSpecification fhirSpec) {
    this.fhirSpec = fhirSpec;
    this.fhirContext = fhirSpec.getFhirVersion().getFhirContext();
    this.parser = this.fhirContext.newJsonParser();
  }
  
  @Override
  public CapabilityStatement metadata() throws IOException, InterruptedException, HttpErrorException {
    HttpRequest.newBuilder(URI.create(fhirSpec.getHostname() + (fhirSpec.getHostname().endsWith("/") ? "" : "/") + "metadata")).GET().header(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE).build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create(fhirSpec.getHostname() + (fhirSpec.getHostname().endsWith("/") ? "" : "/") + "metadata")).GET().header(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE).build()
            , BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return parser.parseResource(CapabilityStatement.class, response.body());
    } else {
      throw HttpErrorException.createExceptionFromStatusCode(response.statusCode(), response.body());
    }
    
  }

  @Override
  public <T extends Resource> T read(String resource, String id, Class<T> clazz) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(
            createBasicRequest(createFhirRoute(resource, id)).GET().build(), 
            BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return parser.parseResource(clazz, response.body());
    } else {
      throw HttpErrorException.createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }

  @Override
  public Bundle search(String resource, Map<String, String> params) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(
            createBasicRequest(createFhirRoute(resource, params)).GET().build(), 
            BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return parser.parseResource(Bundle.class, response.body());
    } else {
      throw HttpErrorException.createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }

  @Override
  public String create(
          String resource,
          String body) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient()
            .send(
                    createBasicRequest(createFhirRoute(resource))
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build(),
                    BodyHandlers.ofString());
    if (response.statusCode() == 201) {
      return response.headers().firstValue(HEADER_VALUE_NEW_ID).isPresent() ? response.headers().firstValue(HEADER_VALUE_NEW_ID).get().split("/")[1] : null;
    } else {
      throw HttpErrorException
              .createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }

  @Override
  public Boolean update(String resource, String id, String body) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(createBasicRequest(createFhirRoute(resource, id)).PUT(HttpRequest.BodyPublishers.ofString(body)).build(), BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return true;
    } else {
      throw HttpErrorException
      .createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }

  @Override
  public Boolean delete(String resource, String id) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(createBasicRequest(createFhirRoute(resource, id)).DELETE().build(), BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return true;
    } else {
      throw HttpErrorException
      .createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }
  
  @Override
  public Boolean patch(
          String resource,
          String id,
          String body) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    HttpResponse<String> response = HttpClient.newHttpClient().send(createBasicRequest(createFhirRoute(resource, id)).method("PATCH", HttpRequest.BodyPublishers.ofString(body)).build(), BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return true;
    } else {
      throw HttpErrorException
      .createExceptionFromStatusCode(response.statusCode(), response.body());
    }
  }
  
  private Builder createBasicRequest(String route) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, ParseException, IOException, JOSEException, InterruptedException, HttpErrorException {
    Builder requestBuilder = HttpRequest.newBuilder(URI.create(route)).header(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE);
    return fhirSpec.getFhirAuth().appendAuthentication(requestBuilder);
  }
  
  private String createFhirRoute(String resource, String id) {
    return appendRouteToHostname(resource + "/" + id);
  }
  
  private String createFhirRoute(String resource, Map<String, String> params) {
    return appendRouteToHostname(resource + "?" + toUrlParams(params));
  }
  
  private String createFhirRoute(String resource) {
    return appendRouteToHostname(resource);
  }
  
  private String appendRouteToHostname(String route) {
    return fhirSpec.getHostname()
            + (fhirSpec.getHostname().endsWith("/") ? "" : "/")
            + route;
  }
  
  private String toUrlParams(Map<String, String> params) {
    return params.entrySet().stream().map(e -> e.getKey()+"="+URLEncoder.encode(e.getValue(), Charset.defaultCharset())).collect(joining("&"));
  }

}
