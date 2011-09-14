package com.josephblough.laborstatistics.adapters;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.activities.MainGridActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainGridAdapter extends ArrayAdapter<String> {

    private String[] items;
    private MainGridActivity context;
    
    public MainGridAdapter(MainGridActivity context, String[] items) {
	super(context, R.layout.main_grid_cell, R.id.grid_cell_title, items);
	this.context = context;
	this.items = items;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = super.getView(position, convertView, parent);
	
	final String title = items[position];
	ImageView image = (ImageView)row.findViewById(R.id.grid_cell_image);
	image.setImageResource(context.getMenuImage(title));
	((TextView)row.findViewById(R.id.grid_cell_title)).setText(title);
	
        return row;
    }
}
