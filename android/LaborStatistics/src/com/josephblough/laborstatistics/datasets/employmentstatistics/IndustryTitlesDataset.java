package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class IndustryTitlesDataset implements DeptOfLaborDataset {

    public String industryCode;
    public String industryTitle;
    
    public String getMethod() {
	return "Statistics/OES2010/industry_titles";
    }

    public String getFields() {
	return "Industry_Code,Industry_Title";
    }

    public String toString(Map<String, String> results) {
	return results.get("Industry_Code") + ", " + results.get("Industry_Title");
    }

}
