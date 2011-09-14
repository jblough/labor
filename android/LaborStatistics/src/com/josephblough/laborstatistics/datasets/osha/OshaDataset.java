package com.josephblough.laborstatistics.datasets.osha;

import java.util.Date;
import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public abstract class OshaDataset implements DeptOfLaborDataset {

    public int activityNumber;
    public String establishmentName;
    public String siteAddress;
    public String siteCity;
    public String siteState;
    public String siteZip;
    public String naicsCode;
    public String inspectionType;
    public Date openDate;
    public String totalCurrentPenalty;
    public boolean oshaViolationIndicator;
    public int seriousViolations;
    public int totalViolations;
    public Date loadDate;
    
    public String getFields() {
	return "activity_nr,estab_name,site_address,site_city,site_state,site_zip," +
		"naics_code,insp_type,open_date,total_current_penalty," +
		"osha_violation_indicator,serious_violations,total_violations,load_dt";
    }

    public String toString(Map<String, String> results) {
	return results.get("activity_nr") + ", " + results.get("estab_name") + 
		", " + results.get("site_address") + ", " + results.get("site_city") + 
		", " + results.get("site_state") + ", " + results.get("site_zip") + 
		", " + results.get("naics_code") + ", " + results.get("insp_type") + 
		", " + results.get("open_date") + ", " + results.get("total_current_penalty") + 
		", " + results.get("osha_violation_indicator") + ", " + results.get("serious_violations") + 
		", " + results.get("total_violations") + ", " + results.get("load_dt");
    }

}
