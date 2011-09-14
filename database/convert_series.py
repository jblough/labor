import sqlite3
import re

con = sqlite3.connect("just_series.db")


# series
#con.execute("CREATE TABLE series (series_id VARCHAR(30), seasonal VARCHAR(1), areatype_code VARCHAR(1), area_code VARCHAR(7), industry_code VARCHAR(6), occupation_code VARCHAR(6), datatype_code VARCHAR(2), footnote_codes VARCHAR(10), begin_year VARCHAR(4), begin_period VARCHAR(3), end_year VARCHAR(4), end_period VARCHAR(3));")

regex = re.compile("^(OE.......................)     \t(.)\t(.)\t(.......)\t(......)\t(......)\t(..)\t(..........)?\t(....)\t(...)\t(....)\t(...)\s*$")
f = open("oe.series.shorter")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      series_id = matcher.group(1).strip()
      seasonal = matcher.group(2).strip()
      areatype_code = matcher.group(3).strip()
      area_code = matcher.group(4).strip()
      industry_code = matcher.group(5).strip()
      occupation_code = matcher.group(6).strip()
      datatype_code = matcher.group(7).strip()
      footnote_codes = matcher.group(8).strip() if matcher.group(8) else ""
      begin_year = matcher.group(9).strip()
      begin_period = matcher.group(10).strip()
      end_year = matcher.group(11).strip()
      end_period = matcher.group(12).strip()
      con.execute("INSERT INTO series VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", (series_id, seasonal, areatype_code, area_code, industry_code, occupation_code, datatype_code, footnote_codes, begin_year, begin_period, end_year, end_period))
      con.commit()
   else:
      print line

   line = f.readline()


