package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureSeriesDataset implements DeptOfLaborDataset {

    public String seriesId;
    public String seasonal;
    public String itemCode;
    public String tableCode;
    public String columnCode;
    public String periodicityCode;
    public String footnoteCodes;
    public int beginYear;
    public int beginPeriod;
    public int endYear;
    public int endPeriod;
	
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/series";
    }

    public String getFields() {
	return "series_id,seasonal,item_code,table_code,column_code," +
			"periodicity_code,footnote_codes,begin_year," +
			"begin_period,end_year,end_period";
    }

    public String toString(Map<String, String> results) {
	return results.get("series_id") + ", " + results.get("seasonal") + 
		", " + results.get("item_code") + ", " + results.get("table_code") + 
		", " + results.get("column_code") + ", " + results.get("periodicity_code") + 
		", " + results.get("footnote_codes") + ", " + results.get("begin_year") + 
		", " + results.get("begin_period") + ", " + results.get("end_year") + 
		", " + results.get("end_period");
    }

}
