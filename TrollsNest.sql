PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE Categories(cat_id INTEGER PRIMARY KEY NOT NULL, name text NOT NULL, description text NOT NULL);
INSERT INTO "Categories" VALUES(1,'test','Troll training!');
CREATE TABLE Users (user_id INTEGER PRIMARY KEY NOT NULL, name text NOT NULL, password text NOT NULL);
INSERT INTO "Users" VALUES(1,'Admin','lol');
CREATE TABLE Topics (topic_id INTEGER PRIMARY KEY NOT NULL, subject text NOT NULL, time date NOT NULL, category INTEGER NOT NULL, user INTEGER NOT NULL, FOREIGN KEY (category) REFERENCES Categories(cat_id), FOREIGN KEY (user) REFERENCES Users(user_id));
INSERT INTO "Topics" VALUES(1,'TROLL-O-LOL','2004-08-19 18:51:06',1,1);
CREATE TABLE Posts(post_id INTEGER PRIMARY KEY NOT NULL, message text NOT NULL, time date NOT NULL, topic INTEGER NOT NULL, user INTEGER NOT NULL, FOREIGN KEY (topic) REFERENCES Topics(topic_id), FOREIGN KEY (user) REFERENCES Users(user_id));
INSERT INTO "Posts" VALUES(1,'hahahaha gg ez nubz lol','2017-02-27 21:54:33',1,1);
COMMIT;