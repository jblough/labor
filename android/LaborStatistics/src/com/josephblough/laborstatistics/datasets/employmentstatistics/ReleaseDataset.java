package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class ReleaseDataset implements DeptOfLaborDataset {

    public String releaseDate;
    public String description;
    
    public String getMethod() {
	return "Statistics/OES2010/release";
    }

    public String getFields() {
	return "release_date,description";
    }

    public String toString(Map<String, String> results) {
	return results.get("release_date") + ", " + results.get("description");
    }

}
