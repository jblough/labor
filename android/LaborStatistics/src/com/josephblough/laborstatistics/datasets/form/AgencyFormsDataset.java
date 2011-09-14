package com.josephblough.laborstatistics.datasets.form;

import java.util.Date;
import java.util.Map;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class AgencyFormsDataset implements DeptOfLaborDataset {

    public int formNumber;
    public Date ombExpirationDate;
    public String agencyID;
    public String title;
    public String ombNumber;
    public String agencyFormNumber;
    public String formDesc;
    public String agencyName;
    public String formURL;
    public String fileURL;
    public String fileName;
    public String fileExt;
    public String fileSize;
    public String keywords;
    public Date dateLastModified;
    public int lastChangedBy;
    public String formRevisionNo;
    public boolean approved;
    public boolean publish;
    public int createdBy;
    public Date createdOn;
    public boolean deleted;
    public boolean hasUrl;
    
    public String getMethod() {
	return "FORMS/AgencyForms";
    }

    public String getFields() {
	return "FormNumber,OMBExpirationDate,AgencyID,Title,OMBNumber,AgencyFormNumber,FormDesc," +
			"AgencyName,FormURL,FileURL,FileName,FileExt,FileSize,Keywords,DateLastMod," +
			"LastChangedBy,FormRevisionNo,Approved,Publish,CreatedBy,CreatedOn,Deleted,URL";
    }

    public String toString(Map<String, String> results) {
	return results.get("FormNumber") + ", " + results.get("OMBExpirationDate") + ", " + results.get("AgencyID")
		+ ", " + results.get("Title") + ", " + results.get("OMBNumber") + ", " + results.get("AgencyFormNumber")
		+ ", " + results.get("FormDesc") + ", " + results.get("AgencyName") + ", " + results.get("FormURL")
		+ ", " + results.get("FileURL") + ", " + results.get("FileName") + ", " + results.get("FileExt")
		+ ", " + results.get("FileSize") + ", " + results.get("Keywords") + ", " + results.get("DateLastMod")
		+ ", " + results.get("LastChangedBy") + ", " + results.get("FormRevisionNo") + ", " + results.get("Approved")
		+ ", " + results.get("Publish") + ", " + results.get("CreatedBy") + ", " + results.get("CreatedOn")
		+ ", " + results.get("Deleted") + ", " + results.get("URL");
    }

}
