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

import com.google.common.net.HttpHeaders;
import com.nimbusds.jose.JOSEException;
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.spec.FhirSpecification;

public class AbstractFhirConnection implements FhirConnection {
  
  private static final String ACCEPT_HEADER_VALUE = "application/json";
  
  private FhirSpecification fhirSpec;
  
  protected AbstractFhirConnection(FhirSpecification fhirSpec) {
    this.fhirSpec = fhirSpec;
  }
  
  @Override
  public String metadata() throws IOException, InterruptedException {
    HttpRequest.newBuilder(URI.create(fhirSpec.getHostname() + (fhirSpec.getHostname().endsWith("/") ? "" : "/") + "metadata")).GET().header(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE).build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create(fhirSpec.getHostname() + (fhirSpec.getHostname().endsWith("/") ? "" : "/") + "metadata")).GET().header(HttpHeaders.ACCEPT, ACCEPT_HEADER_VALUE).build()
            , BodyHandlers.ofString());
    if (response.statusCode() == 200) {
      return response.body();
    } else {
      // Handle failed response code
      return null;
    }
    
  }

  @Override
  public String read(String resource, String id) {
    
    return null;
  }

  @Override
  public String search(String resource, Map<String, String> params) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String create(String resource, String body) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Boolean update(String resource, String id, String body) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Boolean delete(String resouce, String id) {
    // TODO Auto-generated method stub
    return null;
  }
  
  private Builder createBasicRequest(String route) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, ParseException, IOException, JOSEException, InterruptedException, HttpErrorException {
    Builder requestBuilder = HttpRequest.newBuilder(URI.create(fhirSpec.getHostname()));
    return fhirSpec.getFhirAuth().appendAuthentication(requestBuilder);
  }
  
  private String createFhirRoute(String resource, String id) {
    return resource + "/" + id;
  }
  
  private String createFhirRoute(String resource, Map<String, String> params) {
    return resource + "?" + toUrlParams(params);
  }
  
  private String toUrlParams(Map<String, String> params) {
    return params.entrySet().stream().map(e -> e.getKey()+"="+URLEncoder.encode(e.getValue(), Charset.defaultCharset())).collect(joining("&"));
  }

}
