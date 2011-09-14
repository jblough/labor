package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureTableDataset implements DeptOfLaborDataset {

    public String tableCode;
    public String tableText;
    
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/table";
    }

    public String getFields() {
	return "table_code,table_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("table_code") + ", " + results.get("table_text");
    }

}
