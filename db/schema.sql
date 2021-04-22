CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY,
                       description TEXT,
                       created timestamp,
                       done boolean
);