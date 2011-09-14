package com.josephblough.laborstatistics.datasets.geography;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class GeographyStateDataset implements DeptOfLaborDataset {

    public int stateCode;
    public String stateName;

    public String getMethod() {
	return "Geography/State";
    }

    public String getFields() {
	return "StateCode,StateName";
    }

    public String toString(Map<String, String> results) {
	return results.get("StateCode") + ", " + results.get("StateName");
    }

}
