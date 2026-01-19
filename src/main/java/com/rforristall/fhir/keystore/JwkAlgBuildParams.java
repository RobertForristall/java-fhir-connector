package com.rforristall.fhir.keystore;

import java.util.Arrays;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.nimbusds.jose.JWSAlgorithm;

public enum JwkAlgBuildParams {
  
  RSA(JWSAlgorithm.RS384, "RSA", 2048, "SHA256WithRSAEncryption"),
  EC(JWSAlgorithm.ES256, "EC", 256, "SHA256withECDSA");
  
  private static final Map<JWSAlgorithm, JwkAlgBuildParams> JABP_BY_MATCHING_STRING =
          Arrays.stream(values()).collect(ImmutableMap.toImmutableMap(JwkAlgBuildParams::getSecurityAlg, jabp -> jabp));
  
  private JWSAlgorithm securityAlg;
  private String instanceStr;
  private int keySize;
  private String contentSigner;
  
  private JwkAlgBuildParams(JWSAlgorithm securityAlg, String instanceStr, int keySize, String contentSigner) {
    this.securityAlg = securityAlg;
    this.instanceStr = instanceStr;
    this.keySize = keySize;
    this.contentSigner = contentSigner;
  }
  
  public JWSAlgorithm getSecurityAlg() {
    return securityAlg;
  }
  
  public String getInstanceStr() {
    return instanceStr;
  }

  public int getKeySize() {
    return keySize;
  }
  
  public String getContentSigner() {
    return contentSigner;
  }

  public static JwkAlgBuildParams fromJwsAlg(JWSAlgorithm securityAlg) {
    return JABP_BY_MATCHING_STRING.get(securityAlg);
  }

}
