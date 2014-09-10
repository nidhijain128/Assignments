select userId, fullName
from users
where userId not in
(select distinct userId
from public_message)