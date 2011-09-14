package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class DataTypeDataset implements DeptOfLaborDataset {

    public String datatypeCode;
    public String datatypeName;
    public String footnoteCode;
    
    public String getMethod() {
	return "Statistics/OES2010/datatype";
    }

    public String getFields() {
	return "datatype_code,datatype_name,footnote_code";
    }

    public String toString() {
	return datatypeName;
    }
    
    public String toString(Map<String, String> results) {
	return results.get("datatype_code") + ", " + results.get("datatype_name") + 
		", " + results.get("footnote_code");
    }

}
