package com.josephblough.laborstatistics.datasets.whd;

import com.josephblough.laborstatistics.datasets.DeptOfLaborDataset;

public class WhdRetailDataset extends WhdDataset implements
	DeptOfLaborDataset {

    public String getMethod() {
	return "Compliance/WHD/retail";
    }

}
