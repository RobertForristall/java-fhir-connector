package com.rforristall.fhir.conn;

import java.util.Map;

/**
 * FHIR Connection interface outlining the basic methods that FHIR connections should allow users to use, implemented by {@link AbstractFhirConnection}
 *
 * @author Robert Forristall (robert.s.forristall@gmail.com)
 */
public interface FhirConnection {
  
  /**
   * Execute read requests to the FHIR server to get information on a single FHIR resource
   * @param resource {@link String}: Name of the FHIR resource to read from
   * @param id {@link String}: Unique ID of the FHIR resource to read
   * @return {@link String} JSON representation of the FHIR resource
   */
  String read(String resource, String id);
  
  /**
   * Execute search requests to the FHIR server to get information on multiple FHIR resources
   * @param resource {@link String}: Name of the FHIR resource to read from
   * @param params {@link Map}<{@link String}, {@link String}>: Map of search params where the key is the name and value is the value
   * @return {@link String} JSON representation of the FHIR resource bundle
   */
  String search(String resource, Map<String, String> params);
  
  /**
   * Execute create requests to the FHIR server to create new FHIR resources
   * @param resource {@link String}: name of the FHIR resource to create
   * @param body {@link String}: JSON representation of the FHIR resource to create
   * @return {@link String} ID of the newly created resource
   */
  String create(String resource, String body);
  
  /**
   * Execute update requests to the FHIR server to modify existing FHIR resources
   * @param resource {@link String}: Name of the FHIR resource to update
   * @param id {@link String}: Unique ID of the FHIR resource to update
   * @param body {@link String}: JSON representation of the FHIR resource to update
   * @return {@link Boolean} true if the update succeeds and false otherwise
   */
  Boolean update(String resource, String id, String body); 
  
  /**
   * Execute delete requests to the FHIR server to remove existing FHIR resources
   * @param resource {@link String}: Name of the FHIR resource to delete
   * @param id {@link String}: Unique ID of the FHIR resource to delete
   * @return {@link Boolean} true if the delete succeeds and false otherwise
   */
  Boolean delete(String resouce, String id);

}
