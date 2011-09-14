package com.josephblough.laborstatistics.datasets.whd;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class WhdFullDataset extends WhdDataset implements
	DeptOfLaborDataset {

    public String getMethod() {
	return "Compliance/WHD/full";
    }

}
