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
   
   function get_summary_values($occupation, $area) {
      $conn = init_database();
      $sql = "SELECT d.series_id, d.value, d.footnote_codes FROM data d JOIN series s ON s.series_id = d.series_id WHERE s.occupation_code = $1 AND s.industry_code = '000000' AND s.area_code = $2";
      if ($area) {
         $area_param = $area;
      }
      else {
         $area_param = '0000000';
      }
      $values = pg_query_params($conn, $sql, array($occupation, $area_param));
   
      $json = "{\"values\" : [";
      $first = 1;
      while ($value = pg_fetch_array($values)) {
         if ($first != 1) {
            $json .= ",";
         }
         else {
            $first = 0;
         }
         
         if ($value["footnote_codes"] != "") {
            $json .= "{ \"series_id\" : \"" . $value["series_id"] . "\", \"value\" : \"" . $value["value"] . "\", \"footnote\" : \"" . $value["footnote_codes"] . "\" }";
         }
         else {
            $json .= "{ \"series_id\" : \"" . $value["series_id"] . "\", \"value\" : \"" . $value["value"] . "\" }";
         }
      }
      $json .= "]}";
      echo $json;
   }
   
   header("Content-type: application/json");
   
   $occupation = filter_input(INPUT_GET, "occupation", FILTER_SANITIZE_STRING);
   $area = filter_input(INPUT_GET, "area", FILTER_SANITIZE_STRING);
   get_summary_values($occupation, $area);

?>
