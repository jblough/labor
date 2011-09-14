package com.josephblough.laborstatistics.datasets.employmentstatistics;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

import java.util.Map;

public class SeriesDataset implements DeptOfLaborDataset {

    public String seriesId;
    public String seasonal;
    public String areatypeCode;
    public String areaCode;
    public String industryCode;
    public String occupationCode;
    public String datatypeCode;
    public String footnoteCodes;
    public int beginYear;
    public int beginPeriod;
    public int endYear;
    public int endPeriod;

    public String getMethod() {
	return "Statistics/OES2010/series";
    }

    public String getFields() {
	return "series_id,seasonal,areatype_code,area_code,industry_code," +
		"occupation_code,datatype_code,footnote_codes,begin_year," +
		"begin_period,end_year,end_period";
    }

    public String toString(Map<String, String> results) {
	return results.get("series_id") + ", " + results.get("seasonal") + 
		", " + results.get("areatype_code") + ", " + results.get("area_code") + 
		", " + results.get("industry_code") + ", " + results.get("occupation_code") + 
		", " + results.get("datatype_code") + ", " + results.get("footnote_codes") + 
		", " + results.get("begin_year") + ", " + results.get("begin_period") + 
		", " + results.get("end_year") + ", " + results.get("end_period");
    }
}
