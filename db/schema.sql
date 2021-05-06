CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       description TEXT,
                       created timestamp,
                       done boolean,
                       user_id int not null references users(id)
);
CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        login TEXT,
                        email TEXT,
                        password TEXT
);
CREATE TABLE categories (
                       id SERIAL PRIMARY KEY,
                       name TEXT
);