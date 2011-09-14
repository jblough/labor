package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class SectorDataset implements DeptOfLaborDataset {

    public String sectorCode;
    public String sectorName;
    
    public String getMethod() {
	return "Statistics/OES2010/sector";
    }

    public String getFields() {
	return "sector_code,sector_name";
    }

    public String toString(Map<String, String> results) {
	return results.get("sector_code") + ", " + results.get("sector_name");
    }

}
