package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class FootnoteDataset implements DeptOfLaborDataset {

    public String footnoteCode;
    public String footnoteText;
    
    public String getMethod() {
	return "Statistics/OES2010/footnote";
    }

    public String getFields() {
	return "footnote_code,footnote_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("footnote_code") + ", " + results.get("footnote_text");
    }

}
