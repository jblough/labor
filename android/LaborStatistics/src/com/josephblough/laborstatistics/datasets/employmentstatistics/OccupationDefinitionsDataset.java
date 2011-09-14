package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class OccupationDefinitionsDataset implements DeptOfLaborDataset {

    public String occupationCode;
    public String occupationTitle;
    public String definition;
    
    public String getMethod() {
	return "Statistics/OES2010/occupation_definitions";
    }

    public String getFields() {
	return "OCC_CODE,OCC_TITL,DEF";
    }

    public String toString(Map<String, String> results) {
	return results.get("OCC_CODE") + ", " + results.get("OCC_TITL") + ", " + results.get("DEF");
    }

}
