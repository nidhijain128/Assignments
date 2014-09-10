select fullName
from users
where userId in
(select userId
from users_follows
where follows in
(select userId 
from users
where nickName like 'mickey'));