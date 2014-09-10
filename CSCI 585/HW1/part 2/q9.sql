select distinct userId, nickName, emailId
from users 
where userId in
(select userId
from public_message
where dateTime between '01-JAN-2012 00:00:00' and '11-FEB-2013 23:59:59')