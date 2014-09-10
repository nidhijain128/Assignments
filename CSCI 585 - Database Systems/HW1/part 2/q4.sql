select nickName
from users u, private_message pm
where u.userId = pm.userIdS and userIdR in
(select userId
from users
where nickName like 'mickey')
group by nickName
having count(*)>2;
