Besides just being able to compile the LaborStatistics project, some API keys and (optionally) a server host location need to be set up

Department of Labor API key and shared secret:
  Update the member variables API_KEY and SHARED_SECRET with your key and secret (which can be obtained at https://webapps.dol.gov/developer)
  File to update:
    src/com/josephblough/laborstatistics/ApplicationController.java

Google Maps API keys:
  Update the field YOUR_API_KEY_HERE with your Google Maps API key (which can be obtained at http://code.google.com/android/add-ons/google-apis/mapkey.html)
  Files to update:
    res/layout/map_layout.xml
    res/layout/map_with_button_layout.xml

Server script locations: (optional)
  Update the field HOST_NAME with the location where the server side php scripts reside
  File to update:
     src/com/josephblough/laborstatistics/data/DataTransport.java
