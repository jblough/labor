package com.josephblough.laborstatistics.datasets.geography;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class GeographyCityDataset implements DeptOfLaborDataset {

    public static final String CITY_TYPE_BOROUGH = "B";
    public static final String CITY_TYPE_CITY = "C";
    public static final String CITY_TYPE_CDP = "D";
    public static final String CITY_TYPE_COMUNIDAD = "O";
    public static final String CITY_TYPE_TOWN = "T";
    public static final String CITY_TYPE_VILLAGE = "V";
    public static final String CITY_TYPE_OTHER = "X";
    
    public int cityCode;
    public int countyCode;
    public int stateCode;
    public String cityName;
    public String type;
    
    public String getMethod() {
	return "Geography/City";
    }

    public String getFields() {
	return "CityCode,CountyCode,StateCode,CityName,Type";
    }

    public String toString(Map<String, String> results) {
	return results.get("CityCode") + ", " + results.get("CountyCode") + 
		", " + results.get("StateCode") + ", " + results.get("CityName") + 
		", " + results.get("Type");
    }

}
