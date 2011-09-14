package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class OccupationDataset implements DeptOfLaborDataset {

    public String occupationCode;
    public String occupationName;
    public String displayLevel;
    public String selectable;
    public String sortSequence;
    
    public String getMethod() {
	return "Statistics/OES2010/occupation";
    }

    public String getFields() {
	return "occupation_code,occupation_name,display_level,selectable,sort_sequence";
    }

    public String toString() {
	return occupationName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("occupation_code") + ", " + results.get("occupation_name") + ", " + 
		results.get("display_level") + ", " + results.get("selectable") + ", " + results.get("sort_sequence");
    }

}
