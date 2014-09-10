select u.flight_num Flightnumber, x.agentName, x.phone
from fltresv u,
XMLTABLE('/requests/request'
          passing u.request
          columns agentName VARCHAR2(30) PATH 'agentName/text()',
                  phone VARCHAR2(15) Path 'contactPerson/phone/text()') x
where u.origin = 'Los Angeles';