package com.josephblough.laborstatistics.datasets.whd;

import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public abstract class WhdDataset implements DeptOfLaborDataset {

    public String tradeName;
    public String employeeStreetAddress;
    public String city;
    public String state;
    public String zipCode;
    public String naicCode;
    public String naicsCodeDescription;
    public String findingsStartDate;
    public String findingEndDate;
    public String flsaViolationCount;
    public String flsaRepeatViolator;
    public String flsaBwAtpAmt;
    public String flsaEeAtpCnt;
    public String flsaMinimumWagesBwAtpAmt;
    public String flsaOvertimeBwAtpAmt;
    public String flsaBw15a3AtpAmt;
    public String flsaCmpAssessedAmt;
    public String flsaClVioltionCount;
    public String flsaClMinorCount;
    public String flsaClCmpAssessedAmt;
    
    public String getFields() {
	return "trade_nm,street_addr_1_txt,city_nm,st_cd,zip_cd,naic_cd," +
			"naics_code_description,findings_start_date,findings_end_date," +
			"flsa_violtn_cnt,flsa_repeat_violator,flsa_bw_atp_amt," +
			"flsa_ee_atp_cnt,flsa_mw_bw_atp_amt,flsa_ot_bw_atp_amt," +
			"flsa_15a3_bw_atp_amt,flsa_cmp_assd_amt,flsa_cl_violtn_cnt," +
			"flsa_cl_minor_cnt,flsa_cl_cmp_assd_amt";
    }

    public String toString(Map<String, String> results) {
	return results.get("trade_nm") + ", " + results.get("street_addr_1_txt") + 
		", " + results.get("city_nm") + ", " + results.get("st_cd") + 
		", " + results.get("zip_cd") + ", " + results.get("naic_cd") + 
		", " + results.get("naics_code_description") + ", " + results.get("findings_start_date") + 
		", " + results.get("findings_end_date") + ", " + results.get("flsa_violtn_cnt") + 
		", " + results.get("flsa_repeat_violator") + ", " + results.get("flsa_bw_atp_amt") + 
		", " + results.get("flsa_ee_atp_cnt") + ", " + results.get("flsa_mw_bw_atp_amt") + 
		", " + results.get("flsa_ot_bw_atp_amt") + ", " + results.get("flsa_15a3_bw_atp_amt") + 
		", " + results.get("flsa_cmp_assd_amt") + ", " + results.get("flsa_cl_violtn_cnt") + 
		", " + results.get("flsa_cl_minor_cnt") + ", " + results.get("flsa_cl_cmp_assd_amt");
    }

}
