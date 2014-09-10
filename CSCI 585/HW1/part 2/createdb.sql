create table users
(
userId varchar(12),
fullName varchar(100),
nickName varchar(50),
city varchar(80),
state varchar(80),
country varchar(80),
emailId varchar(50),
age number,
CONSTRAINT pk_users primary key(userId)
);

create table users_follows
(
userId varchar(12),
follows varchar(12),
constraint pk_users_follows primary key(userId, follows),
constraint fk_users_follows_userId foreign key(userID) references users(userId)
);

create table private_message
(
userIdS varchar(12),
userIdR varchar(12),
dateTime date,
message varchar(252),
constraint pk_private_message primary key(userIdS, userIdR, dateTime),
constraint fk_private_message_userIdS foreign key(userIdS) references users(userId),
constraint fk_private_message_userIdR foreign key(userIdR) references users(userId)
);

create table public_message
(
tweetId varchar(20),
tweetText varchar(252),
dateTime date,
latitude char(12),
longitude char(12),
userId varchar(12),
constraint pk_public_message primary key(tweetId),
constraint fk_public_message_userId foreign key(userId) references users(userId)
);

create table reposts
(
originalTweetId varchar(20),
repostTweetId varchar(20),
constraint pk_reposts primary key(originalTweetId, repostTweetId),
constraint fk_reposts_originalTweetId foreign key(originalTweetId) references public_message(tweetId),
constraint fk_reposts_repostTweetId foreign key(repostTweetId) references public_message(tweetId)
);

create table public_message_hashTags
(
tweetId varchar(20),
hashTag varchar(20),
constraint pk_public_message_hashTags primary key(tweetId, hashTag),
constraint fk_pm_hashTags_tweetId foreign key(tweetId) references public_message(tweetId)
);

alter session set nls_date_format='dd-mon-yyyy hh24:mi:ss';

insert into users values('1','Lady Gaga','ladygaga','New York City', 'NY','USA','ladygaga@xx.com',27);
insert into users values('2','Michael Smith','mickey','Los Angeles','CA','USA','mickey@xx.com',32);
insert into users values('3','Patricia Williams','patriciawilliams','Seattle','WA','USA','imwilliams@yy.com',18);
insert into users values('4','Linda Miller','lindamiller','Portland','OR','USA','millerl@xx.com',23);
insert into users values('5','Robert Moore','robertmoore','Huston', 'TX','USA','robert@xx.com',56);
insert into users values('6','David Taylor','davidtaylor','Lviv','','Ukraine','taylor@yy.com',45);
insert into users values('7','Maria Hall','mariahall','Hamburg','','Germany','mariahall@xx.com',37);
insert into users values('8','Jennifer Young','jenniferyoung','Porto','','Portugal','jyoung@xx.com',21);
insert into users values('9','Laura Martin','lauramartin','Zagreb','','Croatia','lauram@yy.com',40);
insert into users values('10','Susan Hill','susanhill','Budapest','','Hungary','susanhill@yy.com',29);
insert into users values('11','Sachin Tendulkar','srt','Mumbai','MH','India','srt@xy.com',42);
insert into users values('12','Rafael Nadal','rafaelnadal','Manacor','','Spain','rnadal@yy.com',28);
insert into users values('13','Tiger Woods','tigerwoods','Los Angeles','CA','USA','woods@xx.com',33);
insert into users values('14','Jackie Chan','jackiechan','HK','','China','jackiec@xx.com',45);
insert into users values('15','Chiling Lin','chilinlin','Beijing','','China','chilinglin@xx.com',24);

insert into users_follows values('1','2');
insert into users_follows values('1','3');
insert into users_follows values('1','5');
insert into users_follows values('1','15');
insert into users_follows values('2','3');
insert into users_follows values('2','8');
insert into users_follows values('2','15');
insert into users_follows values('3','2');
insert into users_follows values('4','7');
insert into users_follows values('4','10');
insert into users_follows values('5','3');
insert into users_follows values('6','2');
insert into users_follows values('7','5');
insert into users_follows values('7','9');
insert into users_follows values('7','12');
insert into users_follows values('8','7');
insert into users_follows values('8','10');
insert into users_follows values('9','2');
insert into users_follows values('10','5');
insert into users_follows values('11','2');
insert into users_follows values('12','7');
insert into users_follows values('12','5');
insert into users_follows values('14','2');
insert into users_follows values('14','4');
insert into users_follows values('14','9');
insert into users_follows values('15','4');
insert into users_follows values('15','2');
insert into users_follows values('15','6');
insert into users_follows values('15','8');
insert into users_follows values('15','10');
insert into users_follows values('15','11');
insert into users_follows values('15','12');
insert into users_follows values('15','13');

insert into private_message values('2','1','04-JAN-2012 12:08:45','I really love your music!');
insert into private_message values('3','2','05-JAN-2012 18:48:15','Hey! How have you been?');
insert into private_message values('3','2','15-FEB-2013 15:25:54','Do I know you?');
insert into private_message values('3','2','08-DEC-2012 6:54:32','I am sorry for what I said the other day.');
insert into private_message values('6','15','12-AUG-2012 19:45:00','Good night. Hope to see you next weekend.');
insert into private_message values('7','4','11-NOV-2013 3:45:12','Thank you so much for your help today!');
insert into private_message values('8','2','29-OCT-2012 8:15:02','I talked to Eric, he''s ready to meet us next week and seal the deal.');
insert into private_message values('9','7','07-JAN-2013 9:32:42','No, I don''t think so.');
insert into private_message values('10','15','15-JUL-2013 17:59:59','Aren''t you the famous model?');
insert into private_message values('11','15','01-APR-2012 00:00:00','Hahaha! That was a good one!');
insert into private_message values('12','7','25-MAR-2012 14:22:15','Go Trojan!');
insert into private_message values('13','15','04-DEC-2012 10:46:25','Fight On!');
insert into private_message values('15','1','23-MAY-2013 9:30:26','Yes, even I like CSCI 585.');

insert into public_message values('128951','The weather is good.','01-DEC-2012 7:00:45','','','1');
insert into public_message values('128952','Amazing performance!','14-DEC-2012 17:09:42','lat_33.98','lon_-118.76','2');
insert into public_message values('128953','Totally agree!','14-DEC-2012 0:00:00','','','3');
insert into public_message values('128954','Go Trojans!','20-DEC-2012 0:00:00','lat_29.98','lon_-125.76','4');
insert into public_message values('128955','One Love','23-DEC-2012 0:00:00','','','5');
insert into public_message values('128956','Happy New Year Amigos!','02-JAN-2013 0:00:00','','','6');
insert into public_message values('128957','Enjoy your stay there!','03-JAN-2013 0:00:00','','','7');
insert into public_message values('128958','Love the view here','07-FEB-2013 0:00:00','lat_-15.98','lon_99.88','7');
insert into public_message values('128959','Traffic in downtown!','15-FEB-2013 0:00:00','','','8');
insert into public_message values('128960','Apple vs Samsung, AGAIN!','28-FEB-2013 0:00:00','','','9');
insert into public_message values('128961','It''s also Apple vs Google','05-MAR-2013 0:00:00','','','15');
insert into public_message values('128962','Good to see both companies settle it outside the court.','15-MAR-2013 0:00:00','','','12');
insert into public_message values('128963','Totally agree!','16-MAY-2013 0:00:00','','','13');

insert into reposts values('128952','128953');
insert into reposts values('128960','128961');
insert into reposts values('128963','128962');

insert into public_message_hashtags values('128951','#random');
insert into public_message_hashtags values('128952','#ladygaga');
insert into public_message_hashtags values('128952','#performance');
insert into public_message_hashtags values('128954','#usc');
insert into public_message_hashtags values('128954','#tojans');
insert into public_message_hashtags values('128954','#fighton');
insert into public_message_hashtags values('128956','#newyear');
insert into public_message_hashtags values('128956','#spain');
insert into public_message_hashtags values('128956','#party');
insert into public_message_hashtags values('128958','#santamonica');
insert into public_message_hashtags values('128959','#la');
insert into public_message_hashtags values('128959','#traffic');
insert into public_message_hashtags values('128960','#apple');
insert into public_message_hashtags values('128960','#samsung');
insert into public_message_hashtags values('128960','#ios');
insert into public_message_hashtags values('128960','#android');
insert into public_message_hashtags values('128960','#patent');
insert into public_message_hashtags values('128961','#google');