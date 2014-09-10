select distinct f.flight_num flightnumber, TO_CHAR(f.depart_time,'hh24:mi:ss') departtime, f.duration, to_char(x.travelDate,'dd-MON-rr') traveldate, x.NumberOfSeats
from fltresv f,
XMLTABLE('for $i in /requests/request
          return $i'
          passing f.request
          columns reqDate timestamp PATH '.',
          travelDate timestamp PATH 'reservation/travelDate',
          NumberOfSeats number PATH 'reservation/noOfSeats',
          flightNo number PATH 'reservation/flight') x,
XMLTABLE('for $j in /requests/request
          where $j/requestDate/text() < xs:date("2013-10-10")
          return $j'
          passing f.request
          columns flightNo number PATH 'reservation/flight') y
where x.flightNo = y.flightNo;
