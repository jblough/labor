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
   
   function get_municipalities($state, $occupation) {
      $conn = init_database();
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM Data d JOIN series s ON d.series_id = s.series_id JOIN area a ON s.area_code = a.area_code WHERE a.areatype_code = 'M' AND a.areaname ILIKE $1 AND s.industry_code = '000000' AND s.datatype_code = '04' AND s.occupation_code = $2";
      $state_param = "%, " . $state . "%";
      $municipalities = pg_query_params($conn, $sql, array($state_param , $occupation));
   
      $json = "{\"municipalities\" : [";
      $first = 1;
      while ($municipality = pg_fetch_array($municipalities)) {
         if ($first != 1) {
            $json .= ",";
         }
         else {
            $first = 0;
         }
         
         if ($municipality["footnote_codes"] != "") {
            $json .= "{ \"series_id\" : \"" . $municipality["series_id"] . "\", \"value\" : \"" . $municipality["value"] . "\", \"footnote\" : \"" . $municipality["footnote_codes"] . "\" }";
         }
         else {
            $json .= "{ \"series_id\" : \"" . $municipality["series_id"] . "\", \"value\" : \"" . $municipality["value"] . "\" }";
         }
      }
      $json .= "]}";
      echo $json;
   }
   
   header("Content-type: application/json");
   
   $state = filter_input(INPUT_GET, "state", FILTER_SANITIZE_STRING);
   $occupation = filter_input(INPUT_GET, "occupation", FILTER_SANITIZE_STRING);
   get_municipalities($state, $occupation);

?>
