import sqlite3
import re

con = sqlite3.connect("data.db")

# Android metadata table
con.execute("CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US');")
con.execute("INSERT INTO android_metadata VALUES ('en_US');")

# area
con.execute("CREATE TABLE area (area_code VARCHAR(7), areatype_code VARCHAR(1), areaname VARCHAR(100));")

regex = re.compile("^(.......)\t(.)\t(.*?)\s*$")
f = open("oe.area")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      area = matcher.group(1).strip()
      area_type_code = matcher.group(2).strip()
      area_name = matcher.group(3).strip()
      con.execute("INSERT INTO area VALUES (?, ?, ?)", (area, area_type_code, area_name))
      con.commit()
   else:
      print line

   line = f.readline()

# areatype
con.execute("CREATE TABLE areatype (areatype_code VARCHAR(1), areatype_name VARCHAR(100));")

regex = re.compile("^(.)\t(.*?)\s*$")
f = open("oe.areatype")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      area_type_code = matcher.group(1).strip()
      area_type_name = matcher.group(2).strip()
      con.execute("INSERT INTO areatype VALUES (?, ?)", (area_type_code, area_type_name))
      con.commit()
   else:
      print line

   line = f.readline()

# datatype
con.execute("CREATE TABLE datatype (datatype_code VARCHAR(2), datatype_name VARCHAR(100), footnote_code VARCHAR(1));")

regex = re.compile("^(..)\t(.*)\t(..)\s*$")
f = open("oe.datatype")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      data_type_code = matcher.group(1).strip()
      data_type_name = matcher.group(2).strip()
      footnote_code = matcher.group(3).strip()
      con.execute("INSERT INTO datatype VALUES (?, ?, ?)", (data_type_code, data_type_name, footnote_code))
      con.commit()
   else:
      print line

   line = f.readline()

# footnote
con.execute("CREATE TABLE footnote (footnote_code VARCHAR(1), footnote_text VARCHAR(250));")

regex = re.compile("^(.)\t(.*?)\s*$")
f = open("oe.footnote")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      footnote_code = matcher.group(1).strip()
      footnote_text = matcher.group(2).strip()
      con.execute("INSERT INTO footnote VALUES (?, ?)", (footnote_code, footnote_text))
      con.commit()
   else:
      print line

   line = f.readline()

# industry
con.execute("CREATE TABLE industry (industry_code VARCHAR(6), industry_name VARCHAR(100), display_level VARCHAR(2), selectable VARCHAR(1), sort_sequence VARCHAR(5));")

regex = re.compile("^(......)\t(.+?)\t(..?)\t(.)\t(.*?)")
f = open("oe.industry")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      industry_code = matcher.group(1).strip()
      industry_name = matcher.group(2).strip()
      display_level = matcher.group(3).strip()
      selectable = matcher.group(4).strip()
      sort_sequence = matcher.group(5).strip()
      con.execute("INSERT INTO industry VALUES (?, ?, ?, ?, ?)", (industry_code, industry_name, display_level, selectable, sort_sequence))
      con.commit()
   else:
      print line

   line = f.readline()

# occugroup
con.execute("CREATE TABLE occugroup (occugroup_code VARCHAR(6), occugroup_name VARCHAR(100));")

regex = re.compile("^(......)\t(.*?)\s*$")
f = open("oe.occugroup")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      occugroup_code = matcher.group(1).strip()
      occugroup_name = matcher.group(2).strip()
      con.execute("INSERT INTO occugroup VALUES (?, ?)", (occugroup_code, occugroup_name))
      con.commit()
   else:
      print line

   line = f.readline()

# occupation
con.execute("CREATE TABLE occupation (occupation_code VARCHAR(6), occupation_name VARCHAR(100), display_level VARCHAR(1), selectable VARCHAR(1), sort_sequence VARCHAR(5));")

regex = re.compile("^(......)\t(.+?)\t(.)\t(.)\t(.*?)\s*$")
f = open("oe.occupation")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      occupation_code = matcher.group(1).strip()
      occupation_name = matcher.group(2).strip()
      display_level = matcher.group(3).strip()
      selectable = matcher.group(4).strip()
      sort_sequence = matcher.group(5).strip()
      con.execute("INSERT INTO occupation VALUES (?, ?, ?, ?, ?)", (occupation_code, occupation_name, display_level, selectable, sort_sequence))
      con.commit()
   else:
      print line

   line = f.readline()

# release
con.execute("CREATE TABLE release (release_date VARCHAR(7), description VARCHAR(50));")

regex = re.compile("^(.......)\t(.*?)\s*$")
f = open("oe.release")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      release_date = matcher.group(1).strip()
      description = matcher.group(2).strip()
      con.execute("INSERT INTO release VALUES (?, ?)", (release_date, description))
      con.commit()
   else:
      print line

   line = f.readline()

# seasonal
con.execute("CREATE TABLE seasonal (seasonal VARCHAR(1), seasonal_text VARCHAR(30));")

regex = re.compile("^(.)\t(.*?)\s*$")
f = open("oe.seasonal")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      seasonal = matcher.group(1).strip()
      seasonal_text = matcher.group(2).strip()
      con.execute("INSERT INTO seasonal VALUES (?, ?)", (seasonal, seasonal_text))
      con.commit()
   else:
      print line

   line = f.readline()

# sector
con.execute("CREATE TABLE sector (sector_code VARCHAR(6), sector_name VARCHAR(100));")

regex = re.compile("^(......)\t(.*?)\s*$")
f = open("oe.sector")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      sector_code = matcher.group(1).strip()
      sector_name = matcher.group(2).strip()
      con.execute("INSERT INTO sector VALUES (?, ?)", (sector_code, sector_name))
      con.commit()
   else:
      print line

   line = f.readline()

# statemsa
con.execute("CREATE TABLE statemsa (state_code VARCHAR(2), msa_code VARCHAR(7), msa_name VARCHAR(100));")

regex = re.compile("^(..)\t(.......)\t(.*?)\s*$")
f = open("oe.statemsa")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      state_code = matcher.group(1).strip()
      msa_code = matcher.group(2).strip()
      msa_name = matcher.group(3).strip()
      con.execute("INSERT INTO statemsa VALUES (?, ?, ?)", (state_code, msa_code, msa_name))
      con.commit()
   else:
      print line

   line = f.readline()

# occupation definitions
con.execute("CREATE TABLE occupation_definitions (OCC_CODE VARCHAR(10), OCC_TITL VARCHAR(255), DEF TEXT);")

regex = re.compile("^\"(.+?-.+?)\"\t\"(.+?)\"\t\"(.*)\"$")
f = open("occupation_definitions.csv")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      occ_code = unicode(matcher.group(1).strip())
      occ_title = unicode(matcher.group(2).strip())
      occ_def = unicode(matcher.group(3).strip())
      con.execute(u"INSERT INTO occupation_definitions VALUES (?, ?, ?)", (occ_code, occ_title, occ_def))
      con.commit()
   else:
      print line

   line = f.readline()

# data (only grabbing mean salaries for all occupations at a state level)
con.execute("CREATE TABLE data (series_id VARCHAR(30), year VARCHAR(4), period VARCHAR(3), value VARCHAR(12), footnote_codes VARCHAR(10));")

regex = re.compile("^(OEUS.......000000......04)     \t(....)\t(...)\t(............)\t([0123456789]*)\s*$")
#regex = re.compile("^(OE.......................)     \t(....)\t(...)\t(............)\t([0123456789]*)\s*$")

f = open("oe.data.1.AllData")
line = f.readline()
while (line != ""):
   if (regex.match(line)):
      matcher = regex.match(line)
      series_id = matcher.group(1).strip()
      year = matcher.group(2).strip()
      period = matcher.group(3).strip()
      value = matcher.group(4).strip()
      footnote_code = matcher.group(5).strip()
      con.execute("INSERT INTO data VALUES (?, ?, ?, ?, ?)", (series_id, year, period, value, footnote_code))
      con.commit()
#   else:
#      print line

   line = f.readline()


# series
#con.execute("CREATE TABLE series (series_id VARCHAR(30), seasonal VARCHAR(1), areatype_code VARCHAR(1), area_code VARCHAR(7), industry_code VARCHAR(6), occupation_code VARCHAR(6), datatype_code VARCHAR(2), footnote_codes VARCHAR(10), begin_year VARCHAR(4), begin_period VARCHAR(3), end_year VARCHAR(4), end_period VARCHAR(3));")

#regex = re.compile("^(OE.......................)     \t(.)\t(.)\t(.......)\t(......)\t(......)\t(..)\t(..........)?\t(....)\t(...)\t(....)\t(...)\s*$")

#f = open("oe.series")
#line = f.readline()
#while (line != ""):
#   if (regex.match(line)):
#      matcher = regex.match(line)
#      series_id = matcher.group(1).strip()
#      seasonal = matcher.group(2).strip()
#      areatype_code = matcher.group(3).strip()
#      area_code = matcher.group(4).strip()
#      industry_code = matcher.group(5).strip()
#      occupation_code = matcher.group(6).strip()
#      datatype_code = matcher.group(7).strip()
#      footnote_codes = matcher.group(8).strip() if matcher.group(8) else ""
#      begin_year = matcher.group(9).strip()
#      begin_period = matcher.group(10).strip()
#      end_year = matcher.group(11).strip()
#      end_period = matcher.group(12).strip()
#      con.execute("INSERT INTO series VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", (series_id, seasonal, areatype_code, area_code, industry_code, occupation_code, datatype_code, footnote_codes, begin_year, begin_period, end_year, end_period))
#      con.commit()
#   else:
#      print line
#
#   line = f.readline()
#

