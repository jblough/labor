package com.josephblough.laborstatistics.datasets.osha;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class OshaFullDataset extends OshaDataset implements DeptOfLaborDataset {

    public String getMethod() {
	return "Compliance/OSHA/full";
    }

}
