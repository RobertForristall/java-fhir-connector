package com.rforristall.fhir.test.util;

import java.nio.file.Path;

import com.rforristall.fhir.keystore.KeyStoreSpec;

public class StaticTestStructures {
  
  // Key Store Spec
  public static final String TEST_STORE_NAME = "testStore";
  public static final String TEST_STORE_PASS = "testStorePass";
  public static final String TEST_STORE_KEY_ALIAS = "testKey";
  public static final String TEST_STORE_KEY_PASS = "testKeyPass";
  public static final String TEST_STORE_KEY_ID = "testKeyId";
  
  public static KeyStoreSpec createKeyStoreSpec(Path tempDir) {
    return new KeyStoreSpec(
            tempDir,
            StaticTestStructures.TEST_STORE_NAME,
            StaticTestStructures.TEST_STORE_PASS,
            StaticTestStructures.TEST_STORE_KEY_ALIAS,
            StaticTestStructures.TEST_STORE_KEY_PASS,
            StaticTestStructures.TEST_STORE_KEY_ID);
  }

}
