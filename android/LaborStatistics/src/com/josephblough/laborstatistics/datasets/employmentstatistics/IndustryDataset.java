package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class IndustryDataset implements DeptOfLaborDataset {

    public String industryCode;
    public String industryName;
    public String displayLevel;
    public String selectable;
    public String sortSequence;
    
    public String getMethod() {
	return "Statistics/OES2010/industry";
    }

    public String getFields() {
	return "industry_code,industry_name,display_level,selectable,sort_sequence";
    }

    public String toString() {
	return industryName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("industry_code") + ", " + results.get("industry_name") + ", " + results.get("display_level")
		+ ", " + results.get("selectable") + ", " + results.get("sort_sequence");
    }

}
