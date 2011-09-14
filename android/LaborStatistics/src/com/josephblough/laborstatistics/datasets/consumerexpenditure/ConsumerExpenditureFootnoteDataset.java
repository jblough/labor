package com.josephblough.laborstatistics.datasets.consumerexpenditure;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class ConsumerExpenditureFootnoteDataset implements DeptOfLaborDataset {

    public String footnoteCode;
    public String footnoteText;
    
    public String getMethod() {
	return "Statistics/ConsumerExpenditure/footnote";
    }

    public String getFields() {
	return "footnote_code,footnote_text";
    }

    public String toString(Map<String, String> results) {
	return results.get("footnote_code") + ", " + results.get("footnote_text");
    }

}
