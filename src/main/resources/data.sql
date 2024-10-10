-- Populating the USERS table (30 users in total, 15 trainees and 15 trainers)
INSERT INTO USERS (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, IS_ACTIVE)
VALUES
    ('John', 'Doe', 'john.doe', 'password123', true),
    ('Jane', 'Smith', 'jane.smith', 'password456', true),
    ('Alice', 'Brown', 'alice.brown', 'password789', true),
    ('Michael', 'Johnson', 'michael.johnson', 'password111', true),
    ('Emily', 'Davis', 'emily.davis', 'password222', true),
    ('David', 'Garcia', 'david.garcia', 'password333', true),
    ('Emma', 'Martinez', 'emma.martinez', 'password444', true),
    ('Oliver', 'Lopez', 'oliver.lopez', 'password555', true),
    ('Sophia', 'Gonzalez', 'sophia.gonzalez', 'password666', true),
    ('James', 'Wilson', 'james.wilson', 'password777', true),
    ('Charlotte', 'Moore', 'charlotte.moore', 'password888', true),
    ('Benjamin', 'Taylor', 'benjamin.taylor', 'password999', true),
    ('Lucas', 'Anderson', 'lucas.anderson', 'password101', true),
    ('Amelia', 'Thomas', 'amelia.thomas', 'password202', true),
    ('Mason', 'Jackson', 'mason.jackson', 'password303', true),
-- Trainers
    ('Ethan', 'White', 'ethan.white', 'password404', true),
    ('Olivia', 'Harris', 'olivia.harris', 'password505', true),
    ('Isabella', 'Clark', 'isabella.clark', 'password606', true),
    ('Aiden', 'Lewis', 'aiden.lewis', 'password707', true),
    ('Ava', 'Young', 'ava.young', 'password808', true),
    ('Jacob', 'Walker', 'jacob.walker', 'password909', true),
    ('Liam', 'Hall', 'liam.hall', 'password010', true),
    ('Mia', 'Allen', 'mia.allen', 'password112', true),
    ('Noah', 'King', 'noah.king', 'password213', true),
    ('Ella', 'Wright', 'ella.wright', 'password314', true),
    ('William', 'Scott', 'william.scott', 'password415', true),
    ('Abigail', 'Torres', 'abigail.torres', 'password516', true),
    ('Elijah', 'Nguyen', 'elijah.nguyen', 'password617', true),
    ('Avery', 'Hill', 'avery.hill', 'password718', true),
    ('Evelyn', 'Green', 'evelyn.green', 'password819', true);

-- Populating the TRAINING_TYPE table
INSERT INTO TRAINING_TYPE (TYPE)
VALUES
    ('ORDINARY'),
    ('POWERLIFTING'),
    ('CROSSFIT'),
    ('YOGA'),
    ('PILATES');

-- Populating the TRAINEES table (15 trainees)
INSERT INTO TRAINEES (DATE_OF_BIRTH, ADDRESS, ID)
VALUES
    ('1985-04-12T00:00:00Z', '123 Maple Street', 1),
    ('1990-08-22T00:00:00Z', '456 Oak Avenue', 2),
    ('1995-12-05T00:00:00Z', '789 Pine Road', 3),
    ('1983-03-11T00:00:00Z', '234 Birch Avenue', 4),
    ('1992-07-19T00:00:00Z', '567 Cedar Street', 5),
    ('1997-11-25T00:00:00Z', '678 Spruce Lane', 6),
    ('1981-06-18T00:00:00Z', '321 Willow Blvd', 7),
    ('1993-09-23T00:00:00Z', '901 Ash Court', 8),
    ('1996-01-15T00:00:00Z', '876 Elm Drive', 9),
    ('1994-04-14T00:00:00Z', '345 Poplar Terrace', 10),
    ('1987-02-28T00:00:00Z', '123 Redwood Circle', 11),
    ('1991-08-08T00:00:00Z', '654 Sycamore Ave', 12),
    ('1998-10-20T00:00:00Z', '789 Maple Street', 13),
    ('1986-12-18T00:00:00Z', '432 Oak Boulevard', 14),
    ('1999-06-25T00:00:00Z', '567 Birch Place', 15);

-- Populating the TRAINERS table (15 trainers)
INSERT INTO TRAINERS (TRAINING_TYPE_ID, ID)
VALUES
    (1, 16), -- TrainingType ORDINARY
    (2, 17), -- TrainingType POWERLIFTING
    (3, 18), -- TrainingType CROSSFIT
    (4, 19), -- TrainingType YOGA
    (5, 20), -- TrainingType PILATES
    (1, 21),
    (2, 22),
    (3, 23),
    (4, 24),
    (5, 25),
    (1, 26),
    (2, 27),
    (3, 28),
    (4, 29),
    (5, 30);

-- Populating the TRAININGS table (15 trainings)
INSERT INTO TRAININGS (TRAINEE_ID, TRAINER_ID, NAME, TRAINING_TYPE_ID, DATE, DURATION)
VALUES
    (1, 16, 'Morning Strength', 1, '2024-10-10T07:30:00Z', 60),
    (2, 17, 'Powerlifting Session', 2, '2024-10-11T10:00:00Z', 90),
    (3, 18, 'CrossFit Endurance', 3, '2024-10-12T08:00:00Z', 75),
    (4, 19, 'Yoga Basics', 4, '2024-10-13T09:30:00Z', 60),
    (5, 20, 'Pilates Stretch', 5, '2024-10-14T10:00:00Z', 50),
    (6, 21, 'Strength Training', 1, '2024-10-15T07:30:00Z', 70),
    (7, 22, 'Advanced Powerlifting', 2, '2024-10-16T10:30:00Z', 95),
    (8, 23, 'CrossFit Mastery', 3, '2024-10-17T08:00:00Z', 80),
    (9, 24, 'Yoga for Relaxation', 4, '2024-10-18T09:00:00Z', 65),
    (10, 25, 'Pilates Strength', 5, '2024-10-19T11:00:00Z', 55),
    (11, 26, 'Endurance Training', 1, '2024-10-20T07:00:00Z', 60),
    (12, 27, 'Powerlifting Basics', 2, '2024-10-21T09:30:00Z', 85),
    (13, 28, 'CrossFit Strength', 3, '2024-10-22T08:30:00Z', 70),
    (14, 29, 'Yoga for Strength', 4, '2024-10-23T10:00:00Z', 60),
    (15, 30, 'Pilates Core', 5, '2024-10-24T11:30:00Z', 50);

-- Populating the USERNAMES table
INSERT INTO USERNAMES (BASE_USERNAME, COUNTER)
VALUES
    ('john.doe', 1),
    ('jane.smith', 1),
    ('alice.brown', 1),
    ('michael.johnson', 1),
    ('emily.davis', 1),
    ('david.garcia', 1),
    ('emma.martinez', 1),
    ('oliver.lopez', 1),
    ('sophia.gonzalez', 1),
    ('james.wilson', 1),
    ('charlotte.moore', 1),
    ('benjamin.taylor', 1),
    ('lucas.anderson', 1),
    ('amelia.thomas', 1),
    ('mason.jackson', 1);
