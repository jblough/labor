package com.josephblough.laborstatistics.datasets.safetyandhealth;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class SafetyHealthDataset implements DeptOfLaborDataset {

    public int inspectionNumber;
    public String establishmentName;
    public String city;
    public String state;
    public String zipCode;
    public int sicCode;
    public int naicsCode;
    public String samplingNumber;
    public int officeId;
    public String dateSampled;
    public String dateReported;
    public String instrumentType;
    public String labNumber;
    public String fieldNumber;
    public String sampleType;
    public String blankUsed;
    public double timeSampled;
    public String airVolumeSampled;
    public double sampleWeight;
    public String imisSubstanceCode;
    public String substance;
    public double sampleResult;
    public String qualifier;
    public String unitOfMeasurement;
    public int rowId;
    
    public String getMethod() {
	return "SafetyHealth/ChemicalExposureInspections";
    }

    public String getFields() {
	return "inspection_number,establishment_name,city,state,zip_code,sic_code,naics_code," +
		"sampling_number,office_id,date_sampled,date_reported,instrument_type," +
		"lab_number,field_number,sample_type,blank_used,time_sampled,air_volume_sampled," +
		"sample_weight,imis_substance_code,substance,sample_result,qualifier," +
		"unit_of_measurement,RowId";
    }

    public String toString(Map<String, String> results) {
	return results.get("inspection_number") + ", " + results.get("establishment_name") + 
		", " + results.get("city") + ", " + results.get("state") + 
		", " + results.get("zip_code") + ", " + results.get("sic_code") + 
		", " + results.get("naics_code") + ", " + results.get("sampling_number") + 
		", " + results.get("office_id") + ", " + results.get("date_sampled") + 
		", " + results.get("date_reported") + ", " + results.get("instrument_type") + 
		", " + results.get("lab_number") + ", " + results.get("field_number") + 
		", " + results.get("sample_type") + ", " + results.get("blank_used") + 
		", " + results.get("time_sampled") + ", " + results.get("air_volume_sampled") + 
		", " + results.get("sample_weight") + ", " + results.get("imis_substance_code") + 
		", " + results.get("substance") + ", " + results.get("sample_result") + 
		", " + results.get("qualifier") + ", " + results.get("unit_of_measurement") + 
		", " + results.get("RowId"); 
    }

}
