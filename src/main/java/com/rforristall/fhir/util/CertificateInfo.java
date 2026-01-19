package com.rforristall.fhir.util;

import java.util.Objects;

public class CertificateInfo {
  
  private String owner;
  private String org;
  private String country;
  public CertificateInfo(String owner, String org, String country) {
    super();
    this.owner = owner;
    this.org = org;
    this.country = country;
  }
  public String getOwner() {
    return owner;
  }
  public String getOrg() {
    return org;
  }
  public String getCountry() {
    return country;
  }
  @Override
  public int hashCode() {
    return Objects.hash(country, org, owner);
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CertificateInfo other = (CertificateInfo) obj;
    return Objects.equals(country, other.country) && Objects.equals(org, other.org)
            && Objects.equals(owner, other.owner);
  }
  @Override
  public String toString() {
    return "CertificateInfo [owner=" + owner + ", org=" + org + ", country=" + country + "]";
  }
  
  

}
