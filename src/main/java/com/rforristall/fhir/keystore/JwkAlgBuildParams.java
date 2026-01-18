package com.rforristall.fhir.keystore;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;

public enum JwkAlgBuildParams {
  
  RSA(JWSAlgorithm.RS384, "RSA", 2048),
  EC(JWSAlgorithm.ES256, "EC", 256);
  
  private static final Map<JWSAlgorithm, JwkAlgBuildParams> JABP_BY_MATCHING_STRING =
          Arrays.stream(values()).collect(ImmutableMap.toImmutableMap(JwkAlgBuildParams::securityAlg, jabp -> jabp));
  
  private JWSAlgorithm securityAlg;
  private String instanceStr;
  private int keySize;
  
  private JwkAlgBuildParams(JWSAlgorithm securityAlg, String instanceStr, int keySize) {
    this.securityAlg = securityAlg;
    this.instanceStr = instanceStr;
    this.keySize = keySize;
  }
  
  private JwkAlgBuildParams(JWSAlgorithm securityAlg, String instanceStr, int keySize, JWK key) {
    this.securityAlg = securityAlg;
    this.instanceStr = instanceStr;
    this.keySize = keySize;
  }
  
  public JWSAlgorithm securityAlg() {
    return securityAlg;
  }
  
  public String getInstanceStr() {
    return instanceStr;
  }

  public int getKeySize() {
    return keySize;
  }

  public static JwkAlgBuildParams fromJwsAlg(JWSAlgorithm securityAlg) {
    return JABP_BY_MATCHING_STRING.get(securityAlg);
  }

}
