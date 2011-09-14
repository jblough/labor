import sqlite3
import re

con = sqlite3.connect("just_data.db")


# data
con.execute("CREATE TABLE data (series_id VARCHAR(30), year VARCHAR(4), period VARCHAR(3), value VARCHAR(12), footnote_codes VARCHAR(10));")

#regex = re.compile("^(OEUS...................04)     \t(.+?)\t(.+?)\t(.+?)\t(..........)\s*$")
#regex = re.compile("^(OE.......................)     \t(....)\t(...)\t(............)\t(..........)?\s*$")
regex = re.compile("^(OE.......................)     \t(....)\t(...)\t(............)\t([0123456789]*)\s*$")
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
   else:
      print line

   line = f.readline()



