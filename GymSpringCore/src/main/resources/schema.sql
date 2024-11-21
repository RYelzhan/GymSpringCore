-- Creating the USERS table
CREATE TABLE USERS (
                       ID SERIAL PRIMARY KEY,
                       FIRSTNAME VARCHAR(50) NOT NULL,
                       LASTNAME VARCHAR(50) NOT NULL,
                       USERNAME VARCHAR(50) UNIQUE NOT NULL,
                       PASSWORD VARCHAR(100) NOT NULL,
                       IS_ACTIVE BOOLEAN NOT NULL
);

-- Creating the TRAINING_TYPE table
CREATE TABLE TRAINING_TYPE (
                               ID SERIAL PRIMARY KEY,
                               TYPE VARCHAR(50) UNIQUE NOT NULL
);

-- Creating the TRAINEES table
CREATE TABLE TRAINEES (
                          ID SERIAL PRIMARY KEY,
                          DATE_OF_BIRTH TIMESTAMP,
                          ADDRESS VARCHAR(255),
                          USER_ID INTEGER REFERENCES USERS(ID) ON DELETE CASCADE
);

-- Creating the TRAINERS table
CREATE TABLE TRAINERS (
                          ID SERIAL PRIMARY KEY,
                          TRAINING_TYPE_ID INTEGER REFERENCES TRAINING_TYPE(ID) ON DELETE CASCADE,
                          USER_ID INTEGER REFERENCES USERS(ID) ON DELETE CASCADE
);

-- Creating the TRAININGS table
CREATE TABLE TRAININGS (
                           ID SERIAL PRIMARY KEY,
                           TRAINEE_ID INTEGER REFERENCES TRAINEES(ID) ON DELETE CASCADE,
                           TRAINER_ID INTEGER REFERENCES TRAINERS(ID) ON DELETE CASCADE,
                           NAME VARCHAR(100) NOT NULL,
                           TRAINING_TYPE_ID INTEGER REFERENCES TRAINING_TYPE(ID) ON DELETE CASCADE,
                           DATE TIMESTAMP NOT NULL,
                           DURATION INTEGER NOT NULL
);

-- Creating the USERNAMES table
CREATE TABLE USERNAMES (
                           BASE_USERNAME VARCHAR(50) PRIMARY KEY,
                           COUNTER INTEGER NOT NULL
);
