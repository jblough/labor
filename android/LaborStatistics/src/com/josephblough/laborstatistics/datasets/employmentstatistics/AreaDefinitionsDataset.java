package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class AreaDefinitionsDataset implements DeptOfLaborDataset {

    public String fips;
    public String state;
    public String msaCode;
    public String msaName;
    public String aggregateMsaCode;
    public String aggregateMsaName;
    public String countyCode;
    public String townshipCode;
    public String countyName;
    public String townshipName;
    
    public String getMethod() {
	return "Statistics/OES2010/area_definitions";
    }

    public String getFields() {
	return "FIPS,State,MSA_code,MSA_name,Aggregate_MSA_code,Aggregate_MSA_name,County_code,Township_code,County_name,Township_name";
    }

    public String toString(Map<String, String> results) {
	return results.get("FIPS") + ", " + 
		results.get("State") + ", " +
		results.get("MSA_code") + ", " +
		results.get("MSA_name") + ", " +
		results.get("Aggregate_MSA_code") + ", " +
		results.get("Aggregate_MSA_name") + ", " +
		results.get("County_code") + ", " +
		results.get("Township_code") + ", " +
		results.get("County_name") + ", " +
		results.get("Township_name");
    }

}
