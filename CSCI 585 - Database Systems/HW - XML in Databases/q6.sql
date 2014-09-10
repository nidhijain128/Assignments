select to_char(x.travelDate,'dd-MON-rr') traveldate, sum(x.NumberOfSeats) NumberOfSeats
from fltresv f,
XMLTABLE('/requests/request/reservation'
          passing f.request
          columns travelDate timestamp PATH 'travelDate',
          NumberOfSeats number PATH 'noOfSeats') x
where f.flight_num = '215'
group by x.travelDate