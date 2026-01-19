package com.rforristall.fhir.keystore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rforristall.fhir.util.CertificateInfo;

public class KeyStoreAccessor {
  
  private static final String KEY_STORE_TYPE = "PKCS12";
  
  private KeyStoreSpec keyStoreSpec;

  public KeyStoreAccessor(KeyStoreSpec keyStoreSpec) {
    super();
    this.keyStoreSpec = keyStoreSpec;
  }

  public static String getKeyStoreType() {
    return KEY_STORE_TYPE;
  }

  public KeyStoreSpec getKeyStoreSpec() {
    return keyStoreSpec;
  }
  
  public void createKeyStoreAndKeySet(JWSAlgorithm securityAlg, CertificateInfo certificateInfo) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, JOSEException, OperatorCreationException {
    KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
    try(FileInputStream fis = new FileInputStream(keyStoreSpec.getKeyStoreFilePath().toFile())) {
      ks.load(fis, keyStoreSpec.getStorePassword().toCharArray());
    } catch (FileNotFoundException ex) {
      ks.load(null, keyStoreSpec.getStorePassword().toCharArray());
    }
    KeyStore.ProtectionParameter keyProtParam = new KeyStore.PasswordProtection(keyStoreSpec.getKeyPassword().toCharArray());
    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyStoreSpec.getKeyAlias(), keyProtParam);
    JwkAlgBuildParams buildParams = JwkAlgBuildParams.fromJwsAlg(securityAlg);
    if (pkEntry == null) {
      JWK key = generateKeySet(securityAlg);
      PublicKey publicKey = castPublicKey(key, buildParams);
      PrivateKey privateKey = castPrivateKey(key, buildParams);
      X500Name certName = new X500Name("CN=%s, O=%s, C=%s".formatted(certificateInfo.getOwner(), certificateInfo.getOrg(), certificateInfo.getCountry()));
      SubjectPublicKeyInfo pubKeyInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
      Date start = new Date();
      Date until = Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC));
      X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(
              certName,
              new BigInteger(10, new SecureRandom()),
              start,
              until,
              certName,
              pubKeyInfo);
      ContentSigner contentSigner = new JcaContentSignerBuilder(buildParams.getContentSigner()).build(privateKey);
      X509Certificate certificate = new JcaX509CertificateConverter().setProvider(new BouncyCastleProvider()).getCertificate(certBuilder.build(contentSigner));
      Certificate[] certChain = new Certificate[1];
      certChain[0] = certificate;
      pkEntry = new KeyStore.PrivateKeyEntry(privateKey, certChain);
      ks.setEntry(keyStoreSpec.getKeyAlias(), pkEntry, keyProtParam);
      Files.createDirectories(keyStoreSpec.getStoreDir());
      try (FileOutputStream fos = new FileOutputStream(keyStoreSpec.getKeyStoreFilePath().toFile())) {
        ks.store(fos, keyStoreSpec.getStorePassword().toCharArray());
      }
    }
  }
  
  public SignedJWT getSignedJwtToken(JWSAlgorithm securityAlg, String clientId, String oauthEndpoint, long tokenTtl) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, JOSEException {
    JwkAlgBuildParams buildParams = JwkAlgBuildParams.fromJwsAlg(securityAlg);
    JWK key = fetchKeyFromStore(securityAlg);
    Date now = new Date();
    JWSHeader header = new JWSHeader.Builder((JWSAlgorithm) key.getAlgorithm())
            .keyID(key.getKeyID())
            .type(JOSEObjectType.JWT)
            .build();
    JWTClaimsSet jwt = new JWTClaimsSet.Builder().issuer(clientId).subject(clientId).audience(oauthEndpoint).claim("kid", key.getKeyID()).notBeforeTime(Date.from(now.toInstant().minusSeconds(60))).expirationTime(Date.from(now.toInstant().plusSeconds(tokenTtl))).jwtID(UUID.randomUUID().toString()).build();
    SignedJWT signedJwt = new SignedJWT(header, jwt);
    JWSSigner jwsSigner = getSignerForAlg(key, buildParams);
    signedJwt.sign(jwsSigner);
    return signedJwt;
  }
  
  private JWK generateKey(JWSAlgorithm securityAlg) throws NoSuchAlgorithmException, UnsupportedOperationException {
    JwkAlgBuildParams buildParams = JwkAlgBuildParams.fromJwsAlg(securityAlg);
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(buildParams.getInstanceStr());
    keyGen.initialize(buildParams.getKeySize(), SecureRandom.getInstanceStrong());
    KeyPair pair = keyGen.generateKeyPair();
    switch(buildParams) {
      case EC:
        return newEclipticCurveKey(pair.getPublic(), pair.getPrivate(), securityAlg, keyStoreSpec.getKeyId());
      case RSA:
        return newRsaKey(pair.getPublic(), pair.getPrivate(), securityAlg, keyStoreSpec.getKeyId());
      default:
        // TODO: Handle additional securityAlgs and throw exception for invalid ones
        throw new UnsupportedOperationException("The provided security algorithm: " + securityAlg.getName() + " has not been implemented, please choose either RSA or EC.");
    }
  }
  
  private JWK generateKeySet(JWSAlgorithm securityAlg) throws NoSuchAlgorithmException, IOException {
    JWK key = generateKey(securityAlg);
    JWKSet jwkSet = new JWKSet(key);
    Files.createDirectories(keyStoreSpec.getStoreDir());
    File keySetFile = keyStoreSpec.getKeySetFilePath().toFile();
    try (FileOutputStream fos = new FileOutputStream(keySetFile)) {
      fos.write(jwkSet.toString().getBytes());
      fos.flush();
    }
    return key;
  }
  
  private JWK fetchKeyFromStore(JWSAlgorithm securityAlg) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
    KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);
    try(FileInputStream fis = new FileInputStream(keyStoreSpec.getKeyStoreFilePath().toFile())) {
      ks.load(fis, keyStoreSpec.getStorePassword().toCharArray());
    } catch (FileNotFoundException ex) {
      ks.load(null, keyStoreSpec.getStorePassword().toCharArray());
    }
    KeyStore.ProtectionParameter keyProtParam = new KeyStore.PasswordProtection(keyStoreSpec.getKeyPassword().toCharArray());
    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(keyStoreSpec.getKeyAlias(), keyProtParam);
    JwkAlgBuildParams buildParams = JwkAlgBuildParams.fromJwsAlg(securityAlg);
    switch(buildParams) {
      case EC:
        return newEclipticCurveKey(pkEntry.getCertificate().getPublicKey(), pkEntry.getPrivateKey(), securityAlg, keyStoreSpec.getKeyId());
      case RSA:
        return newRsaKey(pkEntry.getCertificate().getPublicKey(), pkEntry.getPrivateKey(), securityAlg, keyStoreSpec.getKeyId());
      default:
        // TODO: Handle additional securityAlgs and throw exception for invalid ones
        throw new UnsupportedOperationException("The provided security algorithm: " + securityAlg.getName() + " has not been implemented, please choose either RSA or EC.");
    }
  }
  
  private JWK newEclipticCurveKey(PublicKey publicKey, PrivateKey privateKey, JWSAlgorithm securityAlg, String keyId) {
    return new ECKey.Builder(Curve.P_256, (ECPublicKey) publicKey)
            .privateKey((ECPrivateKey) privateKey)
            .algorithm(securityAlg)
            .keyID(keyId)
            .keyUse(KeyUse.SIGNATURE)
            .build();
  }
  
  private JWK newRsaKey(PublicKey publicKey, PrivateKey privateKey, JWSAlgorithm securityAlg, String keyId) {
    return new RSAKey.Builder((RSAPublicKey) publicKey)
            .privateKey((RSAPrivateKey) privateKey)
            .algorithm(securityAlg)
            .keyID(keyId)
            .keyUse(KeyUse.SIGNATURE)
            .build();
  }
  
  private PublicKey castPublicKey(JWK key, JwkAlgBuildParams buildParams) throws JOSEException {
    switch(buildParams) {
      case EC:
        return key.toECKey().toPublicKey();
      case RSA:
        return key.toRSAKey().toPublicKey();
      default:
        throw new UnsupportedOperationException("The provided security algorithm: " + buildParams.getSecurityAlg().getName() + " has not been implemented, please choose either RSA or EC.");
      
    }
  }
  
  private PrivateKey castPrivateKey(JWK key, JwkAlgBuildParams buildParams) throws JOSEException {
    switch(buildParams) {
      case EC:
        return key.toECKey().toPrivateKey();
      case RSA:
        return key.toRSAKey().toPrivateKey();
      default:
        throw new UnsupportedOperationException("The provided security algorithm: " + buildParams.getSecurityAlg().getName() + " has not been implemented, please choose either RSA or EC.");
      
    }
  }
  
  private JWSSigner getSignerForAlg(JWK key, JwkAlgBuildParams buildParams) throws JOSEException {
    switch(buildParams) {
      case EC:
        return new ECDSASigner(key.toECKey().toECPrivateKey());
      case RSA:
        return new RSASSASigner(key.toRSAKey().toRSAPrivateKey());
      default:
        throw new UnsupportedOperationException("The provided security algorithm: " + buildParams.getSecurityAlg().getName() + " has not been implemented, please choose either RSA or EC.");
    }
  }

}
