package com.josephblough.laborstatistics.data;

public class StatisticalSummary {

/*
 * 01 - Employment (1)
 * 02 - Employment percent relative standard error (3)
 * 03 - Hourly mean wage
 * 04 - Annual mean wage (2)
 * 05 - Wage percent relative standard error (3)
 * 06 - Hourly 10th percentile wage
 * 07 - Hourly 25th percentile wage
 * 08 - Hourly median wage
 * 09 - Hourly 75th percentile wage
 * 10 - Hourly 90th percentile wage
 * 11 - Annual 10th percentile wage (2)
 * 12 - Annual 25th percentile wage (2)
 * 13 - Annual median wage (2)
 * 14 - Annual 75th percentile wage (2)
 * 15 - Annual 90th percentile wage (2)
 * 
 *     (1) - 	Estimates for detailed occupations do not sum to the totals because the totals include occupations not shown separately. 
 *     		Estimates do not include self-employed workers.
 *     
 *     (2) - 	Annual wages have been calculated by multiplying the hourly mean wage by 2,080 hours; where an hourly mean wage is not published, 
 *     		the annual wage has been directly calculated from the reported survey data.
 *     
 *     (3) - 	The relative standard error (RSE) is a measure of the reliability of a survey statistic. 
 *     		The smaller the relative standard error, the more precise the estimate. 
 */
    
    // Values
    public String employment;
    public String employmentPercentRelativeStandardError;
    public String hourlyMeanWage;
    public String annualMeanWage;
    public String wagePercentRelativeStandardError;
    public String hourly10thPercentileWage;
    public String hourly25thPercentileWage;
    public String hourlyMedianWage;
    public String hourly75thPercentileWage;
    public String hourly90thPercentileWage;
    public String annual10thPercentileWage;
    public String annual25thPercentileWage;
    public String annualMedianWage;
    public String annual75thPercentileWage;
    public String annual90thPercentileWage;

    // Footnotes
    public String employmentFootnoteCodes;
    public String employmentPercentRelativeStandardErrorFootnoteCodes;
    public String hourlyMeanWageFootnoteCodes;
    public String annualMeanWageFootnoteCodes;
    public String wagePercentRelativeStandardErrorFootnoteCodes;
    public String hourly10thPercentileWageFootnoteCodes;
    public String hourly25thPercentileWageFootnoteCodes;
    public String hourlyMedianWageFootnoteCodes;
    public String hourly75thPercentileWageFootnoteCodes;
    public String hourly90thPercentileWageFootnoteCodes;
    public String annual10thPercentileWageFootnoteCodes;
    public String annual25thPercentileWageFootnoteCodes;
    public String annualMedianWageFootnoteCodes;
    public String annual75thPercentileWageFootnoteCodes;
    public String annual90thPercentileWageFootnoteCodes;
}
