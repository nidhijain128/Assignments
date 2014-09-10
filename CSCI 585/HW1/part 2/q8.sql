select emailID
from users
where userId = (select userId
from public_message
where tweetId = (select originalTweetId
from reposts
where repostTweetId = '128953'));