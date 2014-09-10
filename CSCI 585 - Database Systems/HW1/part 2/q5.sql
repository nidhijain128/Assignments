select avg(age)
from users 
where userId in
(select userId
from users_follows
where follows in 
(select userID
from users 
where fullName like 'Lady Gaga'))