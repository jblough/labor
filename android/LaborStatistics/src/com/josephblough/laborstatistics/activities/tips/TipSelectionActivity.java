package com.josephblough.laborstatistics.activities.tips;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.andpdf.pdfviewer.PdfViewerActivity;

import com.josephblough.laborstatistics.R;
import com.josephblough.laborstatistics.adapters.TipAdapter;
import com.josephblough.laborstatistics.data.Tip;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class TipSelectionActivity extends ListActivity {
    
    private static final String TAG = "TipSelectionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	TipAdapter adapter = new TipAdapter(this, R.layout.tip_row, R.id.tip_row_label, getTips());
	setListAdapter(adapter);
	
	getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    public void onItemClick(AdapterView<?> adapter, View parent, int position, long id) {
		Tip tip = ((TipAdapter)getListAdapter()).getItem(position);
		openWithInternalViewer(tip);
		//openWithExternalViewer(tip);
	    }
	});
    }
    
    private List<Tip> getTips() {
	List<Tip> tips = new ArrayList<Tip>();
	
	tips.add(new Tip("Where are the jobs in my field?", "jobs.pdf", R.raw.jobs));
	tips.add(new Tip("How much could I be earning?", "earnings.pdf", R.raw.earnings));
	tips.add(new Tip("Occupational emloyment and wages report summary", "ocwage.pdf", R.raw.ocwage));
	tips.add(new Tip("Employment and Wages by Major Occupational Group and Industry", "major.pdf", R.raw.major));

	return tips;
    }
    
    private void openWithInternalViewer(final Tip tip) {
	Intent intent = new Intent(TipSelectionActivity.this, TipPdfViewerActivity.class);
	intent.putExtra(PdfViewerActivity.EXTRA_PDFFILENAME, getPdfPath(tip));
	startActivity(intent);
    }
    
    public void openWithExternalViewer(final Tip tip) {
	try {
	    Intent intent = new Intent();
	    //intent.setPackage("com.adobe.reader");
	    intent.setAction(Intent.ACTION_VIEW);
	    final String path = "file://" + getPdfPath(tip);
	    Log.i(TAG, path);
	    intent.setDataAndType(Uri.parse(path), "application/pdf");
	    intent.setFlags(0x7800000);
	    List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(intent, 0);
	    if (resolved != null && resolved.size() > 0)
		startActivity(intent);
	    else
		Toast.makeText(this, "No external viewers found", Toast.LENGTH_LONG).show();
	}
	catch (ActivityNotFoundException e) {
	    
	}
    }
    
    private String getPdfPath(final Tip tip) {
	File destination = new File(getFilesDir(), tip.filename);
	//File destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), tip.filename);
	if (!destination.exists()) {
	    try {
		//Log.d(TAG, "opening output stream for " + destination);
		OutputStream output = new FileOutputStream(destination);

		int[] resources = new int[] { tip.resource };
		for (int resource : resources) {
		    InputStream input = getResources().openRawResource(resource);

		    byte[] buffer = new byte[1024];
		    int length;
		    while ((length = input.read(buffer)) > 0) {
			output.write(buffer, 0, length);
		    }

		    output.flush();
		    input.close();
		}
		output.close();
	    }
	    catch (Exception e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	}

	return destination.getAbsolutePath();
    }
}
