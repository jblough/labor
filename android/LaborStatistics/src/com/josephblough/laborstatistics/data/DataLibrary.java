package com.josephblough.laborstatistics.data;

import com.google.android.maps.GeoPoint;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.AreaTypeDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.DataTypeDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.FootnoteDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.IndustryDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationDefinitionsDataset;
import com.josephblough.laborstatistics.datasets.employmentstatistics.OccupationGroupDataset;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

public class DataLibrary {

    public static final String TAG = "DataLibrary";
    
    public static List<AreaTypeDataset> getAreaTypes(final Context context) {
	List<AreaTypeDataset> areaTypes = new ArrayList<AreaTypeDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("areatype", new String[] {"areatype_code","areatype_name"}, 
		    null, null, null, null, "areatype_name");

	    while (cursor.moveToNext()) {
		AreaTypeDataset areaType = new AreaTypeDataset();
		areaType.areatypeCode = cursor.getString(cursor.getColumnIndex("areatype_code"));
		areaType.areatypeName = cursor.getString(cursor.getColumnIndex("areatype_name"));
		areaTypes.add(areaType);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return areaTypes;
    }
    
    public static List<AreaDataset> getAreas(final Context context, final String areatype) {
	List<AreaDataset> areas = new ArrayList<AreaDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code","areaname"}, 
		    "areatype_code = '" + areatype + "'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		AreaDataset area = new AreaDataset();
		area.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
		area.areatypeCode = areatype;
		area.areaName = cursor.getString(cursor.getColumnIndex("areaname"));
		areas.add(area);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return areas;
    }
    
    public static List<AreaDataset> getMunicipalities(final Context context, final String state) {
	List<AreaDataset> areas = new ArrayList<AreaDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code","areaname"}, 
		    "areatype_code = 'M' AND areaname LIKE '%, " + state + "%'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		AreaDataset area = new AreaDataset();
		area.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
		area.areatypeCode = "M";
		area.areaName = cursor.getString(cursor.getColumnIndex("areaname"));
		areas.add(area);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return areas;
    }
    
    public static AreaDataset getArea(final Context context, final String areaCode) {
	AreaDataset area = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code","areatype_code", "areaname"}, 
		    "area_code = '" + areaCode + "'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		area = new AreaDataset();
		area.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
		area.areatypeCode = cursor.getString(cursor.getColumnIndex("areatype_code"));
		area.areaName = cursor.getString(cursor.getColumnIndex("areaname"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return area;
    }
    
    public static AreaDataset getAreaByName(final Context context, final String areaName) {
	AreaDataset area = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code","areatype_code"}, 
		    "areaname = '" + areaName + "'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		area = new AreaDataset();
		area.areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
		area.areatypeCode = cursor.getString(cursor.getColumnIndex("areatype_code"));
		area.areaName = areaName;
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return area;
    }
    
    public static List<DataTypeDataset> getDataTypes(final Context context) {
	List<DataTypeDataset> dataTypes = new ArrayList<DataTypeDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("datatype", new String[] {"datatype_code","datatype_name"}, 
		    null, null, null, null, "datatype_code");

	    while (cursor.moveToNext()) {
		DataTypeDataset dataType = new DataTypeDataset();
		dataType.datatypeCode = cursor.getString(cursor.getColumnIndex("datatype_code"));
		dataType.datatypeName = cursor.getString(cursor.getColumnIndex("datatype_name"));
		dataTypes.add(dataType);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return dataTypes;
    }

    public static List<OccupationGroupDataset> getOccupationGroups(final Context context) {
	List<OccupationGroupDataset> occupationGroups = new ArrayList<OccupationGroupDataset>();
	OccupationGroupDataset anyOccupationGroup = new OccupationGroupDataset();
	anyOccupationGroup.occupationGroupCode = "000000";
	anyOccupationGroup.occupationGroupName = "All Occupations";
	occupationGroups.add(anyOccupationGroup);
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("occugroup", new String[] {"occugroup_code","occugroup_name"}, 
		    null, null, null, null, "occugroup_name");

	    while (cursor.moveToNext()) {
		OccupationGroupDataset occupationGroup = new OccupationGroupDataset();
		occupationGroup.occupationGroupCode = cursor.getString(cursor.getColumnIndex("occugroup_code"));
		occupationGroup.occupationGroupName = cursor.getString(cursor.getColumnIndex("occugroup_name"));
		occupationGroups.add(occupationGroup);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return occupationGroups;
    }

    public static List<OccupationDataset> getOccupations(final Context context, final OccupationGroupDataset group) {
	List<OccupationDataset> occupations = new ArrayList<OccupationDataset>();
	
	if ("000000".equals(group.occupationGroupCode)) {
	    return getOccupations(context);	// All occupations
	}
	else {
	    SQLiteDatabase db = null;
	    Cursor cursor = null;	
	    try {
		DatabaseHelper helper = new DatabaseHelper(context);
		helper.createDatabase();
		db = helper.openDatabase();
		final String prefix = group.occupationGroupCode.substring(0, 2);
		//Log.d(TAG, "occupation group prefix = '" + prefix + "'");
		cursor = db.query("occupation", new String[] {"occupation_code","occupation_name","display_level","selectable","sort_sequence"}, 
			"occupation_code LIKE '" + prefix + "%'", null, null, null, "occupation_name");

		while (cursor.moveToNext()) {
		    OccupationDataset occupation = new OccupationDataset();
		    occupation.occupationCode = cursor.getString(cursor.getColumnIndex("occupation_code"));
		    occupation.occupationName = cursor.getString(cursor.getColumnIndex("occupation_name"));
		    occupation.displayLevel = cursor.getString(cursor.getColumnIndex("display_level"));
		    occupation.selectable = cursor.getString(cursor.getColumnIndex("selectable"));
		    occupation.sortSequence = cursor.getString(cursor.getColumnIndex("sort_sequence"));
		    occupations.add(occupation);
		}
	    }
	    catch (IOException e) {
		Log.e(TAG, e.getMessage(), e);
	    }
	    finally {
		if (cursor != null)
		    cursor.close();
		if (db != null)
		    db.close();
	    }

	    return occupations;
	}
    }
    
    public static List<OccupationDataset> getOccupations(final Context context) {
	List<OccupationDataset> occupations = new ArrayList<OccupationDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("occupation", new String[] {"occupation_code","occupation_name","display_level","selectable","sort_sequence"}, 
		    null, null, null, null, "occupation_name");

	    while (cursor.moveToNext()) {
		OccupationDataset occupation = new OccupationDataset();
		occupation.occupationCode = cursor.getString(cursor.getColumnIndex("occupation_code"));
		occupation.occupationName = cursor.getString(cursor.getColumnIndex("occupation_name"));
		occupation.displayLevel = cursor.getString(cursor.getColumnIndex("display_level"));
		occupation.selectable = cursor.getString(cursor.getColumnIndex("selectable"));
		occupation.sortSequence = cursor.getString(cursor.getColumnIndex("sort_sequence"));
		occupations.add(occupation);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return occupations;
    }

    public static OccupationDataset getOccupation(final Context context, final String occupationCode) {
	OccupationDataset occupation = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("occupation", new String[] {"occupation_code","occupation_name","display_level","selectable","sort_sequence"}, 
		    "occupation_code = '" + occupationCode + "'", null, null, null, "occupation_code");

	    while (cursor.moveToNext()) {
		occupation = new OccupationDataset();
		occupation.occupationCode = cursor.getString(cursor.getColumnIndex("occupation_code"));
		occupation.occupationName = cursor.getString(cursor.getColumnIndex("occupation_name"));
		occupation.displayLevel = cursor.getString(cursor.getColumnIndex("display_level"));
		occupation.selectable = cursor.getString(cursor.getColumnIndex("selectable"));
		occupation.sortSequence = cursor.getString(cursor.getColumnIndex("sort_sequence"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return occupation;
    }

    public static List<IndustryDataset> getIndustries(final Context context) {
	List<IndustryDataset> industries = new ArrayList<IndustryDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("industry", new String[] {"industry_code","industry_name","display_level","selectable","sort_sequence"}, 
		    null, null, null, null, "industry_code");

	    while (cursor.moveToNext()) {
		IndustryDataset industry = new IndustryDataset();
		industry.industryCode = cursor.getString(cursor.getColumnIndex("industry_code"));
		industry.industryName = cursor.getString(cursor.getColumnIndex("industry_name"));
		industry.displayLevel = cursor.getString(cursor.getColumnIndex("display_level"));
		industry.selectable = cursor.getString(cursor.getColumnIndex("selectable"));
		industry.sortSequence = cursor.getString(cursor.getColumnIndex("sort_sequence"));
		industries.add(industry);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return industries;
    }

    public static IndustryDataset getIndustry(final Context context, final String industryCode) {
	IndustryDataset industry = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("industry", new String[] {"industry_code","industry_name","display_level","selectable","sort_sequence"}, 
		    "industry_code = '" + industryCode + "'", null, null, null, "industry_code");

	    while (cursor.moveToNext()) {
		industry = new IndustryDataset();
		industry.industryCode = cursor.getString(cursor.getColumnIndex("industry_code"));
		industry.industryName = cursor.getString(cursor.getColumnIndex("industry_name"));
		industry.displayLevel = cursor.getString(cursor.getColumnIndex("display_level"));
		industry.selectable = cursor.getString(cursor.getColumnIndex("selectable"));
		industry.sortSequence = cursor.getString(cursor.getColumnIndex("sort_sequence"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return industry;
    }
    
    public static List<OccupationDefinitionsDataset> getOccupationDefinitions(final Context context) {
	List<OccupationDefinitionsDataset> definitions = new ArrayList<OccupationDefinitionsDataset>();
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("occupation_definitions", new String[] {"OCC_CODE","OCC_TITL","DEF"}, 
		    null, null, null, null, "datatype_code");

	    while (cursor.moveToNext()) {
		OccupationDefinitionsDataset definition = new OccupationDefinitionsDataset();
		definition.occupationCode = cursor.getString(cursor.getColumnIndex("OCC_CODE"));
		definition.occupationTitle = cursor.getString(cursor.getColumnIndex("OCC_TITL"));
		definition.definition = cursor.getString(cursor.getColumnIndex("DEF"));
		definitions.add(definition);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return definitions;
    }
    
    public static OccupationDefinitionsDataset getOccupationDefinition(final Context context, final String occupationCode) {
	OccupationDefinitionsDataset definition = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    final String code = occupationCode.substring(0, 2) + "-" + occupationCode.substring(2);
	    cursor = db.query("occupation_definitions", new String[] {"OCC_CODE","OCC_TITL","DEF"}, 
		    "OCC_CODE = '" + code + "'", null, null, null, "OCC_CODE");

	    while (cursor.moveToNext()) {
		definition = new OccupationDefinitionsDataset();
		definition.occupationCode = occupationCode;//cursor.getString(cursor.getColumnIndex("OCC_CODE"));
		definition.occupationTitle = cursor.getString(cursor.getColumnIndex("OCC_TITL"));
		definition.definition = cursor.getString(cursor.getColumnIndex("DEF"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return definition;
    }
    
    public static List<DataDataset> getStateMeanSalaries(final Context context, final String occupationCode) {
	List<DataDataset> salaries = new ArrayList<DataDataset>();
	
	Log.i(TAG, "Searching on series_id LIKE 'OEUS%" + occupationCode + "04'");
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("data", new String[] {"series_id", "year", "period", "value", "footnote_codes"}, 
		    "series_id LIKE 'OEUS%" + occupationCode + "04'", null, null, null, "series_id");

	    Log.i(TAG, "Move to first: " + cursor.moveToFirst());
	    
	    while (cursor.moveToNext()) {
		DataDataset data = new DataDataset();
		data.seriesId = cursor.getString(cursor.getColumnIndex("series_id"));
		data.year = cursor.getString(cursor.getColumnIndex("year"));
		data.period = cursor.getString(cursor.getColumnIndex("period"));
		data.value = cursor.getString(cursor.getColumnIndex("value"));
		data.footnoteCodes = cursor.getString(cursor.getColumnIndex("footnote_codes"));
		salaries.add(data);
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	Log.d(TAG, "Returning " + salaries.size() + " salaries");
	return salaries;
    }
    
    public static FootnoteDataset getFootnote(final Context context, final String footnoteCode) {
	FootnoteDataset footnote = null;

	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("footnote", new String[] {"footnote_text"}, 
		    "footnote_code = '" + footnoteCode + "'", null, null, null, "footnote_text");

	    while (cursor.moveToNext()) {
		footnote = new FootnoteDataset();
		footnote.footnoteCode = footnoteCode;
		footnote.footnoteText = cursor.getString(cursor.getColumnIndex("footnote_text"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return footnote;
    }
    
    public static GeoPoint getGeoPoint(final Context context, final String areaCode) {
	GeoPoint point = null;
	AreaDataset area = getArea(context, areaCode);
	if (area != null) {
		SQLiteDatabase db = null;
		Cursor cursor = null;	
		try {
		    DatabaseHelper helper = new DatabaseHelper(context);
		    helper.createDatabase();
		    db = helper.openDatabase();
		    cursor = db.query("area_coordinates", new String[] {"latitude", "longitude"}, 
			    "areaname = " + DatabaseUtils.sqlEscapeString(area.areaName), null, null, null, "areaname");

		    while (cursor.moveToNext()) {
			double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
			double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
			point = StateLibrary.getPoint(latitude, longitude);
		    }
		}
		catch (IOException e) {
		    Log.e(TAG, e.getMessage(), e);
		}
		finally {
		    if (cursor != null)
			cursor.close();
		    if (db != null)
			db.close();
		}
	    
	}
	
	return point;
    }
    
    public static String getStateAreaCode(final Context context, final String stateName) {
	String areaCode = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code"}, 
		    "areaname = " + DatabaseUtils.sqlEscapeString(stateName) + " AND areatype_code = 'S'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return areaCode;
    }
    
    public static String getMunicipalityAreaCode(final Context context, final String cityName) {
	String areaCode = null;
	
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area", new String[] {"area_code"}, 
		    "areaname = " + DatabaseUtils.sqlEscapeString(cityName) + " AND areatype_code = 'M'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		areaCode = cursor.getString(cursor.getColumnIndex("area_code"));
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}
	
	return areaCode;
    }

    public static String getClosestMunicipality(final Context context, final Location location) {
	Location city = new Location(location.getProvider());
	String closestCity = null;
	float distanceToBeat = 100000f;
	SQLiteDatabase db = null;
	Cursor cursor = null;	
	try {
	    DatabaseHelper helper = new DatabaseHelper(context);
	    helper.createDatabase();
	    db = helper.openDatabase();
	    cursor = db.query("area_coordinates", new String[] {"areaname", "latitude", "longitude"}, 
		    "areaname LIKE '%, %'", null, null, null, "areaname");

	    while (cursor.moveToNext()) {
		final String areaname = cursor.getString(cursor.getColumnIndex("areaname"));
		final double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
		final double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
		city.setLatitude(latitude);
		city.setLongitude(longitude);
		float distance = location.distanceTo(city);
		if (distance < distanceToBeat) {
		    distanceToBeat = distance;
		    closestCity = areaname;
		}
	    }
	}
	catch (IOException e) {
	    Log.e(TAG, e.getMessage(), e);
	}
	finally {
	    if (cursor != null)
		cursor.close();
	    if (db != null)
		db.close();
	}

	return getMunicipalityAreaCode(context, closestCity);
    }
}
