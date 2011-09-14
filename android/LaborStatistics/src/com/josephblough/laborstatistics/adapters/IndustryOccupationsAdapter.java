package com.josephblough.laborstatistics.adapters;

import java.text.NumberFormat;
import java.util.List;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.data.DataLibrary;
import com.josephblough.laborstatistics.data.DataRetriever;
import com.josephblough.laborstatistics.data.DataTransport;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IndustryOccupationsAdapter extends ArrayAdapter<DataDataset> {

    private NumberFormat moneyFormatter = NumberFormat.getCurrencyInstance();
    private NumberFormat formatter = NumberFormat.getIntegerInstance();
    
    private Context context;
    private int sortorder;
    
    public IndustryOccupationsAdapter(Context context, List<DataDataset> items, int sortorder) {
	super(context, R.layout.industry_occupation_row, R.id.comparison_row_occupation_name, items);
	this.context = context;
	this.sortorder = sortorder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = super.getView(position, convertView, parent);
	
	final String occupationCode = 
		DataRetriever.extractOccupationFromSeriesId(getItem(position).seriesId);
	final OccupationDataset occupation = DataLibrary.getOccupation(
		context, occupationCode);
	
	final String value = (sortorder == DataTransport.SALARY_CRITERIA) ?
		moneyFormatter.format(Double.valueOf(getItem(position).value)) :
		    formatter.format(Integer.valueOf(getItem(position).value));
	
	((TextView)row.findViewById(R.id.comparison_row_occupation_name)).setText(occupation.occupationName);
	((TextView)row.findViewById(R.id.comparison_row_occupation_value)).setText(value);
	
	return row;
    }
}
