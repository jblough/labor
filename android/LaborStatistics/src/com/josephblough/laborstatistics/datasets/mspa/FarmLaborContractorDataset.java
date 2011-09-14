package com.josephblough.laborstatistics.datasets.mspa;

import java.util.Date;
import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class FarmLaborContractorDataset implements DeptOfLaborDataset {

    public String flcCertificateNumber;
    public String flcBusinessOrIndividual;
    public String flcAddress;
    public String flcBusinessType;
    public String drivingAuthorization;
    public String housingAuthorization;
    public String transportationAuthorization;
    public Date flcCertificateValidUntil;
    
    public String getMethod() {
	return "MSPA/FarmLaborContractors";
    }

    public String getFields() {
	return "FLC_Certificate_Number,FLC_Business_or_Individual,FLC_Address," +
			"FLC_Business_Type,Driving_Authorization,Housing_Authorization," +
			"Transportation_Authorization,FLC_Certificate_Valid_Until";
    }

    public String toString(Map<String, String> results) {
	return results.get("FLC_Certificate_Number") + ", " + results.get("FLC_Business_or_Individual") + 
		", " + results.get("FLC_Address") + ", " + results.get("FLC_Business_Type") + 
		", " + results.get("Driving_Authorization") + ", " + results.get("Housing_Authorization") + 
		", " + results.get("Transportation_Authorization") + ", " + results.get("FLC_Certificate_Valid_Until");
    }

}
