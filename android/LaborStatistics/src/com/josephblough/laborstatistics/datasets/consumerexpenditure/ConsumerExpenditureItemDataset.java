package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureItemDataset implements DeptOfLaborDataset {

    public String itemCode;
    public String itemText;
    
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/item";
    }

    public String getFields() {
	return "item_code,item_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("item_code") + ", " + results.get("item_text");
    }

}
