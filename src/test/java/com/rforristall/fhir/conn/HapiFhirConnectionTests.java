package com.rforristall.fhir.conn;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.Map;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.nimbusds.jose.JOSEException;
import com.rforristall.fhir.exception.HttpErrorException;
import com.rforristall.fhir.hapi.HapiFhirConnection;
import com.rforristall.fhir.spec.HapiTestSpecification;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class HapiFhirConnectionTests {
  
  private HapiFhirConnection conn;
  
  @BeforeAll
  public void setup() {
    conn = new HapiFhirConnection(HapiTestSpecification.createHapiSpec());
  }
  
  @AfterAll
  public void cleanup() {
    
  }
  
  @Test
  public void getMetadata() throws IOException, InterruptedException, HttpErrorException {
    CapabilityStatement capabilityStatement = conn.metadata();
    Assertions.assertNotNull(capabilityStatement);
  }
  
  @Test
  public void getResource() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    Patient patient = conn.read("Patient", "1000", Patient.class);
    Assertions.assertNotNull(patient);
    Assertions.assertEquals("1000", patient.getIdPart());
  }
  
  @Test
  public void getBundleOfPatients() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException, IOException, InterruptedException, ParseException, JOSEException, HttpErrorException {
    Bundle bundle = conn.search("Patient", Map.of("_id", "1000"));
    Assertions.assertNotNull(bundle);
    Assertions.assertEquals(1, bundle.getEntry().size());
    for (BundleEntryComponent entry: bundle.getEntry()) {
      Resource resource = entry.getResource();
      Assertions.assertInstanceOf(Patient.class, resource);
    }
  }

}
