select f.flight_num FlightNumber, TO_CHAR(f.depart_time,'hh24:mi:ss') departTime, f.duration
from fltresv f,
XMLTABLE('for $i in /requests/request
          where $i/agentName/text() = xs:string("Tee Adventures")
          return $i'
          passing f.request
          columns agentName VARCHAR2(30) PATH '.') x
where f.seasonal = 0;