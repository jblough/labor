	package com.josephblough.laborstatistics.datasets.form;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class AgenciesDataset implements DeptOfLaborDataset {

    public String agencyId;
    public String agencyName;
    
    public String getMethod() {
	return "FORMS/Agencies";
    }

    public String getFields() {
	return "AgencyID,AgencyName";
    }

    public String toString(Map<String, String> results) {
	return results.get("AgencyID") + ", " + results.get("AgencyName");
    }

}
