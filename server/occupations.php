<?php
   // The purpose of this script is to retrieve all mean annual salaries for an occupation for
   //    all municipalities for a given state.  The state provided is in the form of the state abbreviation.
   
   function init_database() {
      $host = "";
      $user = "";
      $pass = "";
      $database = "";

      $conn = pg_connect("host=$host user=$user password=$pass dbname=$database") or die("Could not connect to the database");
      return $conn;
   }
   
   function get_occupations($sql, $array) {
      $conn = init_database();
      $industries = pg_query_params($conn, $sql, $array);
      $json = "{\"occupations\" : [";
      $first = 1;
      while ($industry = pg_fetch_array($industries)) {
         if ($first != 1) {
            $json .= ",";
         }
         else {
            $first = 0;
         }
         
         if ($industry["footnote_codes"] != "") {
            $json .= "{ \"series_id\" : \"" . $industry["series_id"] . "\", \"value\" : \"" . $industry["value"] . "\", \"footnote\" : \"" . $industry["footnote_codes"] . "\" }";
         }
         else {
            $json .= "{ \"series_id\" : \"" . $industry["series_id"] . "\", \"value\" : \"" . $industry["value"] . "\" }";
         }
      }
      $json .= "]}";
      echo $json;
   }
   
   function get_industry_occupations_by_salary($industry) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM data d JOIN series s ON d.series_id = s.series_id JOIN occupation o ON o.occupation_code = s.occupation_code JOIN industry i ON i.industry_code = s.industry_code WHERE s.industry_code = $1 AND s.datatype_code = '04' AND s.areatype_code = 'N' AND s.area_code = '0000000' AND o.occupation_code != '000000' AND d.value != '-' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_occupations($sql, array($industry));
   }
   
   function get_industry_occupations_by_employment($industry) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM data d JOIN series s ON d.series_id = s.series_id JOIN occupation o ON o.occupation_code = s.occupation_code JOIN industry i ON i.industry_code = s.industry_code WHERE s.industry_code = $1 AND s.datatype_code = '01' AND s.areatype_code = 'N' AND s.area_code = '0000000' AND o.occupation_code != '000000' AND d.value != '-' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_occupations($sql, array($industry));
   }
   
   function get_location_occupations_by_salary($area) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM occupation o JOIN series s ON o.occupation_code = s.occupation_code JOIN data d ON d.series_id = s.series_id WHERE s.datatype_code = '04' AND s.area_code = $1 AND s.occupation_code != '000000' AND s.industry_code = '000000' AND d.value != '-' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_occupations($sql, array($area));
   }
   
   function get_location_occupations_by_employment($area) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM occupation o JOIN series s ON o.occupation_code = s.occupation_code JOIN data d ON d.series_id = s.series_id WHERE s.datatype_code = '01' AND s.area_code = $1 AND s.occupation_code != '000000' AND s.industry_code = '000000' AND d.value != '-' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_occupations($sql, array($area));
   }
   
   header("Content-type: application/json");
   
   $industry = filter_input(INPUT_GET, "industry", FILTER_SANITIZE_STRING);
   $area = filter_input(INPUT_GET, "area", FILTER_SANITIZE_STRING);
   $employment = filter_input(INPUT_GET, "employment", FILTER_SANITIZE_STRING);
   if ($employment) {
      if ($area) {
         get_location_occupations_by_employment($area);
      }
      else {
         get_industry_occupations_by_employment($industry);
      }
   }
   else {
      if ($area) {
         get_location_occupations_by_salary($area);
      }
      else {
         get_industry_occupations_by_salary($industry);
      }
   }
?>
