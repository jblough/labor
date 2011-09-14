<?php

   $host = "localhost";
   $user = "";
   $pass = "";
   $database = "";
   $pgconn = pg_connect("host=$host user=$user password=$pass dbname=$database") or die("Could not connect to the database");

   $db = new PDO('sqlite:/path/to/data.db');

   // Areatype
   $sql = "INSERT INTO areatype (areatype_code, areatype_name) VALUES ($1, $2)";
   foreach ($db->query('select areatype_code, areatype_name from areatype;') as $areatype) {
      $sql_params = array($areatype[0], $areatype[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Area
   $sql = "INSERT INTO area (area_code, areatype_code, areaname) VALUES ($1, $2, $3)";
   foreach ($db->query('select area_code, areatype_code, areaname from area;') as $area) {
      $sql_params = array($area[0], $area[1], $area[2]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Datatype
   $sql = "INSERT INTO datatype (datatype_code, datatype_name, footnote_code) VALUES ($1, $2, $3)";
   foreach ($db->query('select datatype_code, datatype_name, footnote_code from datatype;') as $datatype) {
      $sql_params = array($datatype[0], $datatype[1], $datetype[2]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Footnote
   $sql = "INSERT INTO footnote (footnote_code, footnote_text) VALUES ($1, $2)";
   foreach ($db->query('select footnote_code, footnote_text from footnote;') as $footnote) {
      $sql_params = array($footnote[0], $footnote[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Industry
   $sql = "INSERT INTO industry (industry_code, industry_name, display_level, selectable, sort_sequence) VALUES ($1, $2, $3, $4, $5)";
   foreach ($db->query('select industry_code, industry_name, display_level, selectable, sort_sequence from industry;') as $industry) {
      $sql_params = array($industry[0], $industry[1], $industry[2], $industry[3], $industry[4]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Occugroup
   $sql = "INSERT INTO occugroup (occugroup_code, occugroup_name) VALUES ($1, $2)";
   foreach ($db->query('select occugroup_code, occugroup_name from occugroup;') as $occugroup) {
      $sql_params = array($occugroup[0], $occugroup[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Occupation
   $sql = "INSERT INTO occupation (occupation_code, occupation_name, display_level, selectable, sort_sequence) VALUES ($1, $2, $3, $4, $5)";
   foreach ($db->query('select occupation_code, occupation_name, display_level, selectable, sort_sequence from occupation;') as $occupation) {
      $sql_params = array($occupation[0], $occupation[1], $occupation[2], $occupation[3], $occupation[4]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Occupation Definitions
   $sql = "INSERT INTO occupation_definitions (OCC_CODE, OCC_TITL, DEF) VALUES ($1, $2, $3)";
   foreach ($db->query('select OCC_CODE, OCC_TITL, DEF from occupation_definitions;') as $occupation_definitions) {
      $sql_params = array($occupation_definitions[0], $occupation_definitions[1], $occupation_definitions[2]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Release
   $sql = "INSERT INTO release (release_date, description) VALUES ($1, $2)";
   foreach ($db->query('select release_date, description from release;') as $release) {
      $sql_params = array($release[0], $release[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Seasonal
   $sql = "INSERT INTO seasonal (seasonal, seasonal_text) VALUES ($1, $2)";
   foreach ($db->query('select seasonal, seasonal_text from seasonal;') as $seasonal) {
      $sql_params = array($seasonal[0], $seasonal[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Sector
   $sql = "INSERT INTO sector (sector_code, sector_name) VALUES ($1, $2)";
   foreach ($db->query('select sector_code, sector_name from sector;') as $sector) {
      $sql_params = array($sector[0], $sector[1]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   // Statemsa
   $sql = "INSERT INTO statemsa (state_code, msa_code, msa_name) VALUES ($1, $2, $3)";
   foreach ($db->query('select state_code, msa_code, msa_name from statemsa;') as $statemsa) {
      $sql_params = array($statemsa[0], $statemsa[1], $statemsa[2]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   $db = new PDO('sqlite:/path/to/just_series.db');

   // Series
   $sql = "INSERT INTO series (series_id, seasonal, areatype_code, area_code, industry_code, occupation_code, datatype_code, footnote_codes, begin_year, begin_period, end_year, end_period) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12)";
   foreach ($db->query('select series_id, seasonal, areatype_code, area_code, industry_code, occupation_code, datatype_code, footnote_codes, begin_year, begin_period, end_year, end_period from series;') as $series) {
      $sql_params = array($series[0], $series[1], $series[2], $series[3], $series[4], $series[5], $series[6], $series[7], $series[8], $series[9], $series[10], $series[11]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

   $db = new PDO('sqlite:/path/to/just_data.db');

   // Data
   $sql = "INSERT INTO data (series_id, year, period, value, footnote_codes) VALUES ($1, $2, $3, $4, $5)";
   foreach ($db->query('select series_id, year, period, value, footnote_codes from data;') as $data) {
      $sql_params = array($data[0], $data[1], $data[2], $data[3], $data[4]);
      pg_query_params($pgconn, $sql, $sql_params);
   }

?>
