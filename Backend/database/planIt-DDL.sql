drop table if exists users, payments, items, item_owings, item_payings, events, event_items, event_participant, test;

CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  fname VARCHAR(255) NOT NULL,
  mname VARCHAR(255),
  lname VARCHAR(255),
  mobile VARCHAR(20) UNIQUE,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE
);



CREATE TABLE events (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  date TIMESTAMP NOT NULL,
  description TEXT,
  completed BOOL NOT NULL 
);

CREATE TABLE items (
  id SERIAL PRIMARY KEY,
  name VARCHAR(63) NOT NULL,
  description VARCHAR(255) NOT NULL,  
  expense int
);


CREATE TABLE item_payings (
  item_id INT,
  payer_id INT,
  amount INT NOT NULL,
  PRIMARY KEY (item_id, payer_id),
  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
  FOREIGN KEY (payer_id) REFERENCES users(id)
);

CREATE TABLE item_owings (
  item_id INT,
  ower_id INT,
  amount INT NOT NULL,
  PRIMARY KEY (item_id, ower_id),
  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE, 
  FOREIGN KEY (ower_id) REFERENCES users(id)
);

CREATE TABLE event_participant (
  event_id INT,
  participant_id INT,
  PRIMARY KEY (event_id, participant_id),
  FOREIGN KEY (event_id) REFERENCES events(id),
  FOREIGN KEY (participant_id) REFERENCES users(id)
);

CREATE TABLE event_items (
  event_id INT,
  item_id INT,
  PRIMARY KEY (event_id, item_id),
  FOREIGN KEY (event_id) REFERENCES events(id),
  FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE payments (
  payer_id INT NOT NULL,
  receiver_id INT NOT NULL,
  item_id INTEGER,
  amount INTEGER,
  FOREIGN KEY (receiver_id) REFERENCES users(id),
  FOREIGN KEY (payer_id) REFERENCES users(id),
  FOREIGN KEY (item_id) REFERENCES items(id) on delete cascade
);
