package com.josephblough.laborstatistics.datasets.agency;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class AgenciesDataset implements DeptOfLaborDataset {

    public String agency;
    public String agencyFullName;
    public String websiteURL;
    public String missionURL;
    public String organizationalURL;
    public String keyPersonnelURL;
    
    public String getMethod() {
	return "DOLAgency/Agencies";
    }

    public String getFields() {
	return "Agency,AgencyFullName,WebsiteURL,MissionURL,OrganizationalURL,KeyPersonnelURL";
    }

    public String toString(Map<String, String> results) {
	return results.get("Agency") + ", " + results.get("AgencyFullName") + ", " + results.get("WebsiteURL") + 
		", " + results.get("MissionURL") + ", " + results.get("OrganizationalURL") + 
		", " + results.get("KeyPersonnelURL");
    }

}
