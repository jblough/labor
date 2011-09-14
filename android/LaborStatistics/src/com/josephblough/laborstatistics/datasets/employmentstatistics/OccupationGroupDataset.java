package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class OccupationGroupDataset implements DeptOfLaborDataset {

    public String occupationGroupCode;
    public String occupationGroupName;
    
    public String getMethod() {
	return "Statistics/OES2010/occugroup";
    }

    public String getFields() {
	return "occugroup_code,occugroup_name";
    }

    public String toString() {
	return occupationGroupName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("occugroup_code") + " - " + results.get("occugroup_name");
    }

}
