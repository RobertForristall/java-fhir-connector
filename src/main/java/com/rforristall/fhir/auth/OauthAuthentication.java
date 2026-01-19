package com.rforristall.fhir.auth;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.nimbusds.jose.shaded.gson.JsonParser;
import com.nimbusds.jwt.SignedJWT;
import com.rforristall.fhir.keystore.KeyStoreAccessor;
import com.rforristall.fhir.util.HttpErrorException;

public class OauthAuthentication extends AbstractFhirAuthentication{
  
  private KeyStoreAccessor keyStoreAccessor;
  private JWSAlgorithm securityAlg;
  private String clientId;
  private String oauthEndpoint;
  private String scopes;
  private String accessToken;
  private long tokenTtl;
  

  public OauthAuthentication(
          KeyStoreAccessor keyStoreAccessor,
          JWSAlgorithm securityAlg,
          String clientId,
          String oauthEndpoint,
          String scopes,
          long tokenTtl) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, JOSEException, InterruptedException, HttpErrorException {
    super();
    this.keyStoreAccessor = keyStoreAccessor;
    this.securityAlg = securityAlg;
    this.clientId = clientId;
    this.oauthEndpoint = oauthEndpoint;
    this.scopes = scopes;
    this.accessToken = getAccessToken();
    this.tokenTtl = tokenTtl;
  }

  @Override
  public Builder appendAuthentication(Builder requestBuilder) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, ParseException, IOException, JOSEException, InterruptedException, HttpErrorException {
    if (this.isTokenExpired()) accessToken = getAccessToken();
    requestBuilder.header(AUTH_HEADER, accessToken);
    return requestBuilder;
  }
  
  private String getAccessToken() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, JOSEException, InterruptedException, HttpErrorException {
    Map<String, String> params = Maps.newHashMap();
    params.put("grant_type", "client_credentials");
    params.put("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer");
    params.put("scope", String.join(" ", scopes));
    params.put("client_assertion", keyStoreAccessor.getSignedJwtToken(securityAlg, clientId, oauthEndpoint, tokenTtl).serialize());
    String form = params.entrySet().stream().map((e) -> {
      return e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8);
    }).collect(Collectors.joining("&"));
    URI endpointUri = URI.create(oauthEndpoint);
    HttpRequest request = HttpRequest.newBuilder().uri(endpointUri).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString(form)).build();
    HttpResponse<String> response = null;
    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() == 200) return "Bearer " + getAccessTokenFromResponse(response.body());
    else throw HttpErrorException.createExceptionFromStatusCode(response.statusCode(), response.body());
  }
  
  private String getAccessTokenFromResponse(String responseBody) {
    JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
    if (jsonObject.has("access_token")) return jsonObject.get("access_token").getAsString();
    else return "";
  }
  
  private boolean isTokenExpired() throws ParseException {
    SignedJWT jwt = SignedJWT.parse(accessToken.replace("Bearer ", ""));
    long exp = (long) jwt.getPayload().toJSONObject().get("exp");
    return Date.from(Instant.ofEpochSecond(exp)).before(new Date());
  }
  
}
