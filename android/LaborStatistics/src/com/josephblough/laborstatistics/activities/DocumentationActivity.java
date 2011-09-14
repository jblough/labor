package com.josephblough.laborstatistics.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.josephblough.laborstatistics.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class DocumentationActivity extends Activity implements OnCheckedChangeListener  {

    private static final String TAG = "DocumentationActivity";
    
    public static final String DEFAULT_PAGE = "DocumentationActivity.default_page";
    
    public static final int OCCUPATIONS_PAGE = 0;
    public static final int INDUSTRIES_PAGE = 1;
    public static final int LOCATIONS_PAGE = 2;
    public static final int COMPARE_PAGE = 3;

    private TextView pageHeader = null;
    private ImageView headerImage = null;
    private WebView pageWebview = null;
    private ToggleButton occupationsPageButton;
    private ToggleButton industriesPageButton;
    private ToggleButton locationsPageButton;
    private ToggleButton comparePageButton;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documentation);

        pageHeader = (TextView)findViewById(R.id.documentation_header);
        headerImage = (ImageView)findViewById(R.id.documentation_header_image);
        pageWebview = (WebView)findViewById(R.id.documentation_webview);
        occupationsPageButton = (ToggleButton)findViewById(R.id.documentation_occupation_button);
        industriesPageButton = (ToggleButton)findViewById(R.id.documentation_industries_button);
        locationsPageButton = (ToggleButton)findViewById(R.id.documentation_locations_button);
        comparePageButton = (ToggleButton)findViewById(R.id.documentation_compare_button);
        
        occupationsPageButton.setOnCheckedChangeListener(this);
        industriesPageButton.setOnCheckedChangeListener(this);
        locationsPageButton.setOnCheckedChangeListener(this);
        comparePageButton.setOnCheckedChangeListener(this);
        
        int defaultPage = getIntent().getIntExtra(DEFAULT_PAGE, OCCUPATIONS_PAGE);
        //occupationsPageButton.setChecked(true);
        switch (defaultPage) {
        case OCCUPATIONS_PAGE:
            occupationsPageButton.setChecked(true);
            break;
        case INDUSTRIES_PAGE:
            industriesPageButton.setChecked(true);
            break;
        case LOCATIONS_PAGE:
            locationsPageButton.setChecked(true);
            break;
        case COMPARE_PAGE:
            comparePageButton.setChecked(true);
            break;
        default:
            occupationsPageButton.setChecked(true);
        }
        
        /*pageWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "onPageStarted: " + url);
                if (url.startsWith("data:text/html") || "about:blank".equals(url)) {
                    super.onPageStarted(view, url, favicon);
                }
                else {
                    final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                
                // Move the page to the top after load (unless anchors are present in the url)
                if (url != null && !url.contains("#")) {
                    view.scrollTo(0, 0);
                }
            }
        });*/
    }

    private void loadPage(final int page) {
	switch (page) {
	case OCCUPATIONS_PAGE:
	    pageWebview.loadData(rawResourceToHtmlString(R.raw.occupations),
		    "text/html", "utf-8");
	    break;
	case INDUSTRIES_PAGE:
	    pageWebview.loadDataWithBaseURL(null, rawResourceToHtmlString(R.raw.industries),
		    "text/html", "utf-8", null);
	    break;
	case LOCATIONS_PAGE:
	    pageWebview.loadDataWithBaseURL(null, rawResourceToHtmlString(R.raw.locations),
		    "text/html", "utf-8", null);
	    break;
	case COMPARE_PAGE:
	    pageWebview.loadDataWithBaseURL(null, rawResourceToHtmlString(R.raw.compare),
		    "text/html", "utf-8", null);
	    break;
	default:
	    pageWebview.loadDataWithBaseURL(null, rawResourceToHtmlString(R.raw.occupations),
		    "text/html", "utf-8", null);
	}
	pageWebview.pageUp(true);
    }
    
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	if (!isChecked) {
	    // We need to make sure that at least one button is always checked
	    int numberOfButtonsChecked = 0;
	    if (occupationsPageButton.isChecked())
		numberOfButtonsChecked++;
	    if (industriesPageButton.isChecked())
		numberOfButtonsChecked++;
	    if (locationsPageButton.isChecked())
		numberOfButtonsChecked++;
	    if (comparePageButton.isChecked())
		numberOfButtonsChecked++;

	    if (numberOfButtonsChecked == 0) {
		// Don't allow the unchecking
		buttonView.setChecked(true);
	    }

	    return;
	}
	
	// Otherwise uncheck the other buttons
	if (buttonView.getId() == R.id.documentation_occupation_button) {
	    pageHeader.setText("Know your occupation?");
	    headerImage.setImageResource(R.drawable.occupation2);
	    industriesPageButton.setChecked(false);
	    locationsPageButton.setChecked(false);
	    comparePageButton.setChecked(false);
	    loadPage(OCCUPATIONS_PAGE);
	}
	else if (buttonView.getId() == R.id.documentation_industries_button) {
	    pageHeader.setText("Know your preferred industry?");
	    headerImage.setImageResource(R.drawable.industry2);
	    occupationsPageButton.setChecked(false);
	    locationsPageButton.setChecked(false);
	    comparePageButton.setChecked(false);
	    loadPage(INDUSTRIES_PAGE);
	}
	else if (buttonView.getId() == R.id.documentation_locations_button) {
	    pageHeader.setText("Know where you want to work?");
	    headerImage.setImageResource(R.drawable.location3);
	    occupationsPageButton.setChecked(false);
	    industriesPageButton.setChecked(false);
	    comparePageButton.setChecked(false);
	    loadPage(LOCATIONS_PAGE);
	}
	else if (buttonView.getId() == R.id.documentation_compare_button) {
	    pageHeader.setText("Compare you salary");
	    headerImage.setImageResource(R.drawable.compare2);
	    occupationsPageButton.setChecked(false);
	    industriesPageButton.setChecked(false);
	    locationsPageButton.setChecked(false);
	    loadPage(COMPARE_PAGE);
	}
    }

    private String rawResourceToHtmlString(final int id) {
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	try {
	    try {
		InputStream input = getResources().openRawResource(id);
		byte[] buffer = new byte[1024];
		while (true) {
		    int read = input.read(buffer);
		    if (read < 0) {
			break;
		    }

		    output.write(buffer, 0, read);
		}
		
	    } finally {
		if (output != null) {
		    output.flush();
		    output.close();
		}
	    }

	    String html = output.toString();
	    StringBuilder buf = new StringBuilder(html.length());
	    for (char c : html.toCharArray()) {
		switch (c) {
		case '#':
		    buf.append("%23");
		    break;
		case '%':
		    buf.append("%25");
		    break;
		case '\'':
		    buf.append("%27");
		    break;
		/*case '?':
		    buf.append("%3f");
		    break;*/
		default:
		    buf.append(c);
		    break;
		}
	    }
	    return buf.toString();
	} catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	
	return "";
    }
}
