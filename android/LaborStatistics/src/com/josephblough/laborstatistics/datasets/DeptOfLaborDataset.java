package com.josephblough.laborstatistics.datasets;

import java.util.Map;

public interface DeptOfLaborDataset {

    public String getMethod();
    public String getFields();
    public String toString(final Map<String, String> results);
}
