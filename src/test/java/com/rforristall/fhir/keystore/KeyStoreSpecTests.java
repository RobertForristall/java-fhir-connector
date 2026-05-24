package com.rforristall.fhir.keystore;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.io.TempDir;

import com.rforristall.fhir.test.util.StaticTestStructures;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class KeyStoreSpecTests {

  @TempDir
  private static Path tempDir;

  @Test
  public void createKeyStoreSpec() {
    KeyStoreSpec spec = new KeyStoreSpec(
            tempDir,
            StaticTestStructures.TEST_STORE_NAME,
            StaticTestStructures.TEST_STORE_PASS,
            StaticTestStructures.TEST_STORE_KEY_ALIAS,
            StaticTestStructures.TEST_STORE_KEY_PASS,
            StaticTestStructures.TEST_STORE_KEY_ID);
    Assertions.assertEquals(tempDir, spec.getStoreDir());
    Assertions.assertEquals(StaticTestStructures.TEST_STORE_NAME, spec.getStoreFileName());
    Assertions.assertEquals(StaticTestStructures.TEST_STORE_PASS, spec.getStorePassword());
    Assertions.assertEquals(StaticTestStructures.TEST_STORE_KEY_ALIAS, spec.getKeyAlias());
    Assertions.assertEquals(StaticTestStructures.TEST_STORE_KEY_PASS, spec.getKeyPassword());
    Assertions.assertEquals(StaticTestStructures.TEST_STORE_KEY_ID, spec.getKeyId());
    Assertions.assertEquals(
            Paths.get(tempDir.toString(), StaticTestStructures.TEST_STORE_NAME),
            spec.getKeyStoreFilePath());
  }

}
