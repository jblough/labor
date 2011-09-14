package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureColumnDataset implements DeptOfLaborDataset {

    public String tableCode;
    public String columnCode;
    public String columnText;
    
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/column";
    }

    public String getFields() {
	return "table_code,column_code,column_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("table_code") + ", " + results.get("column_code") + ", " + results.get("column_text");
    }

}
