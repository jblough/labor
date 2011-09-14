package com.josephblough.laborstatistics.adapters;

import java.util.List;

import com.josephblough.laborstatistics.data.Tip;

import android.content.Context;
import android.widget.ArrayAdapter;

public class TipAdapter extends ArrayAdapter<Tip> {

    public TipAdapter(Context context, int resource, int textViewResourceId,
	    List<Tip> objects) {
	super(context, resource, textViewResourceId, objects);
    }

}
