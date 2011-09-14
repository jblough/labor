package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class StateMsaDataset implements DeptOfLaborDataset {

    public String stateCode;
    public String msaCode;
    public String msaName;
    
    public String getMethod() {
	return "Statistics/OES2010/statemsa";
    }

    public String getFields() {
	return "state_code,msa_code,msa_name";
    }

    public String toString(Map<String, String> results) {
	return results.get("state_code") + ", " + results.get("msa_code") + ", " + results.get("msa_name");
    }

}
