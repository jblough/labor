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
   
   function get_locations($sql, $array) {
      $conn = init_database();
      $locations = pg_query_params($conn, $sql, $array);
      $json = "{\"locations\" : [";
      $first = 1;
      while ($location = pg_fetch_array($locations)) {
         if ($first != 1) {
            $json .= ",";
         }
         else {
            $first = 0;
         }
         
         if ($location["footnote_codes"] != "") {
            $json .= "{ \"series_id\" : \"" . $location["series_id"] . "\", \"value\" : \"" . $location["value"] . "\", \"footnote\" : \"" . $location["footnote_codes"] . "\" }";
         }
         else {
            $json .= "{ \"series_id\" : \"" . $location["series_id"] . "\", \"value\" : \"" . $location["value"] . "\" }";
         }
      }
      $json .= "]}";
      echo $json;
   }
   
   function get_occupation_locations_by_salary($occupation, $areatype) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM Data d JOIN series s ON d.series_id = s.series_id JOIN industry i ON i.industry_code = s.industry_code WHERE s.occupation_code = $1 AND s.datatype_code = '04' AND s.areatype_code = $2 AND d.value != '-' AND i.industry_code = '000000' AND i.industry_code NOT LIKE '%-%' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_locations($sql, array($occupation, $areatype));
   }
   
   function get_occupation_locations_by_employment($occupation, $areatype) {
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM Data d JOIN series s ON d.series_id = s.series_id JOIN industry i ON i.industry_code = s.industry_code WHERE s.occupation_code = $1 AND s.datatype_code = '01' AND s.areatype_code = $2 AND d.value != '-' AND i.industry_code = '000000' AND i.industry_code NOT LIKE '%-%' ORDER BY CAST(d.value AS NUMERIC) DESC LIMIT 50";
      get_locations($sql, array($occupation, $areatype));
   }
   
   header("Content-type: application/json");
   
   $occupation = filter_input(INPUT_GET, "occupation", FILTER_SANITIZE_STRING);
   $areatype = filter_input(INPUT_GET, "areatype", FILTER_SANITIZE_STRING);
   $areatype = ($areatype && $areatype == 'M') ? "M" : "S";
   $employment = filter_input(INPUT_GET, "employment", FILTER_SANITIZE_STRING);
   if ($employment) {
      get_occupation_locations_by_employment($occupation, $areatype);
   }
   else {
      get_occupation_locations_by_salary($occupation, $areatype);
   }
?>
