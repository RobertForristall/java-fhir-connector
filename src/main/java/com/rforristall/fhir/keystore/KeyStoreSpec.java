package com.rforristall.fhir.keystore;

import java.nio.file.Path;
import java.util.Objects;

public class KeyStoreSpec {
  
  private Path storeDir;
  private String storeFileName;
  private String storePassword;
  private String keyAlias;
  private String keyPassword;
  private String keyId;
  
  public KeyStoreSpec(
          Path storeDir,
          String storeFileName,
          String storePassword,
          String keyAlias,
          String keyPassword,
          String keyId) {
    super();
    this.storeDir = storeDir;
    this.storeFileName = storeFileName;
    this.storePassword = storePassword;
    this.keyAlias = keyAlias;
    this.keyPassword = keyPassword;
    this.keyId = keyId;
  }
  
  public Path getStoreDir() {
    return storeDir;
  }

  public String getStoreFileName() {
    return storeFileName;
  }

  public String getStorePassword() {
    return storePassword;
  }

  public String getKeyAlias() {
    return keyAlias;
  }

  public String getKeyPassword() {
    return keyPassword;
  }

  public String getKeyId() {
    return keyId;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(keyAlias, keyId, keyPassword, storeDir, storeFileName, storePassword);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    KeyStoreSpec other = (KeyStoreSpec) obj;
    return Objects.equals(keyAlias, other.keyAlias) && Objects.equals(keyId, other.keyId)
            && Objects.equals(keyPassword, other.keyPassword)
            && Objects.equals(storeDir, other.storeDir)
            && Objects.equals(storeFileName, other.storeFileName)
            && Objects.equals(storePassword, other.storePassword);
  }

  @Override
  public String toString() {
    return "KeyStoreSpec [storeDir=" + storeDir + ", storeFileName=" + storeFileName
            + ", storePassword=" + isPasswordPresent(storePassword) + ", keyAlias=" + keyAlias + ", keyPassword="
            + isPasswordPresent(keyPassword) + ", keyId=" + keyId + "]";
  }

  private String isPasswordPresent(String value) {
    return value != null && !value.isBlank() ? "<Present>" : "<Hidden>";
  }

}
