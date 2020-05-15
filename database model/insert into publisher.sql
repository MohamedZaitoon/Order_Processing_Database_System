insert into publisher values 
('Zephania Gallagher','Moldova','070 1011 5460'),
('Althea Molina','Zimbabwe','0353 373 6205'),
('Colin Hamilton','Kazakhstan','0800 805208'),
('Sloane Riggs','Venezuela','070 8308 3335'),
('Xyla Miles','Sri Lanka','0884 077 5167'),
('Azalia Oneil','Panama','0800 716 4094'),
('Harrison K. Haynes','Angola','056 4422 6416'),
('Barry Singleton','Latvia','(016977) 3982'),
('Perry Berg','Jamaica','0800 1111'),
('Sylvia Z. Preston','Swaziland','056 9963 6248');




INSERT INTO Book values 
('1236547891597', 'land of Animals', 'Science', 15, 33, 10,'Zephania Gallagher', '1995-1-1'),
('1298653428971', 'Atomic and electron', 'Science', 10, 63, 20, 'Zephania Gallagher','2005-1-1'),
('2589764135879', 'way to allah', 'Religion', 20, 40, 10, 'Althea Molina','1980-1-1');



INSERT INTO Authors VALUES ('Adel Shakal' , '1236547891597');
INSERT INTO Authors VALUES ('Fady 3adalat', '1236547891597');
INSERT INTO Authors VALUES ('Coco','1236547891597');
INSERT INTO Authors VALUES ('Mohamed Alkazzafy','1236547891597');
INSERT INTO Authors VALUES ('Mohamed Hesham','2589764135879');






INSERT INTO users
VALUES (NULL, 'helio21@gmail.com', 'john', 'whick', 'john21', '2000-2-1', 'Customer', 'M', 'dhsafihadshufh'),
       (NULL, 'jomia21@gmail.com', 'lala', 'dave', 'lal21', '1997-2-1', 'Customer', 'F', 'dhsafsdfejdsjflkufh'),
       (NULL, 'tomato21@gmail.com', 'mike', 'steve', 'mike21', '1990-2-1', 'Customer', 'M', 'dhsaf561551ihadshufh'),
       (NULL, 'joba21@gmail.com', 'juana', 'patric', 'juana21', '1994-2-1', 'Customer', 'F', 'dhsafi51565400000hadshufh'),
       (NULL, 'jhonny43@gmail.com', 'salem', 'rami', 'salem21', '2001-2-1', 'Manager', 'M', 'dhsafdsafasdfasdfadfasdfadfsdihadshufh');



call signup('helddio212@gmail.com', 'mark .v'
, 'whick', 'makr213', '2000-3-2', 'Customer', 'M', 'dhsafih564a6df6adshufh');

call signup('helio212@gmail.com', 'john .v', 'whick', 'john213',
 '2000-2-2', 'Customer', 'M', 'dhsafih56adshufh');






call purchase( '1236547891597', 1 , 5);
call purchase( '1236547891597', 9 , 5);
call purchase( '1236547891597', 3 , 5);
call purchase( '1236547891597', 4 , 5);
call purchase( '1236547891597', 5 , 5);
call purchase( '1236547891597', 6 , 5);
call purchase( '1236547891597', 7 , 5);

call purchase( '1236547891597', 8 , 5);
call purchase( '1236547891597', 1 , 5);
call purchase( '1236547891597', 9 , 5);
call purchase( '1236547891597', 3 , 5);

call purchase( '1298653428971', 4 , 5);
call purchase( '1298653428971', 8 , 5);
call purchase( '1298653428971', 1 , 5);


call purchase( '1298653428971', 4 , 5);
call purchase( '1298653428971', 1 , 5);