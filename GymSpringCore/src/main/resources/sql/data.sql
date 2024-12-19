-- Populating the USERS table (30 users in total, 15 trainees and 15 trainers)
INSERT INTO USERS (ID, FIRSTNAME, LASTNAME, USERNAME, IS_ACTIVE)
VALUES
    (1, 'John', 'Doe', 'john.doe', true),
    (2, 'Jane', 'Smith', 'jane.smith', true),
    (3, 'Alice', 'Brown', 'alice.brown', true),
    (4, 'Michael', 'Johnson', 'michael.johnson', true),
    (5, 'Emily', 'Davis', 'emily.davis', true),
    (6, 'David', 'Garcia', 'david.garcia', true),
    (7, 'Emma', 'Martinez', 'emma.martinez', true),
    (8, 'Oliver', 'Lopez', 'oliver.lopez', true),
    (9, 'Sophia', 'Gonzalez', 'sophia.gonzalez', true),
    (10, 'James', 'Wilson', 'james.wilson', true),
    (11, 'Charlotte', 'Moore', 'charlotte.moore', true),
    (12, 'Benjamin', 'Taylor', 'benjamin.taylor', true),
    (13, 'Lucas', 'Anderson', 'lucas.anderson', true),
    (14, 'Amelia', 'Thomas', 'amelia.thomas', true),
    (15, 'Mason', 'Jackson', 'mason.jackson', true),
-- Trainers
    (16, 'Ethan', 'White', 'ethan.white', true),
    (17, 'Olivia', 'Harris', 'olivia.harris', true),
    (18, 'Isabella', 'Clark', 'isabella.clark', true),
    (19, 'Aiden', 'Lewis', 'aiden.lewis', true),
    (20, 'Ava', 'Young', 'ava.young', true),
    (21, 'Jacob', 'Walker', 'jacob.walker', true),
    (22, 'Liam', 'Hall', 'liam.hall', true),
    (23, 'Mia', 'Allen', 'mia.allen', true),
    (24, 'Noah', 'King', 'noah.king', true),
    (25, 'Ella', 'Wright', 'ella.wright', true),
    (26, 'William', 'Scott', 'william.scott', true),
    (27, 'Abigail', 'Torres', 'abigail.torres', true),
    (28, 'Elijah', 'Nguyen', 'elijah.nguyen', true),
    (29, 'Avery', 'Hill', 'avery.hill', true),
    (30, 'Evelyn', 'Green', 'evelyn.green', true);

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

-- Associating Trainees with Trainers in the TRAINEE_TRAINER_MAPPING table
INSERT INTO TRAINEE_TRAINER_MAPPING (TRAINEE_ID, TRAINER_ID)
VALUES
    (1, 16),  -- Trainee 1 is assigned to Trainer 16
    (2, 17),  -- Trainee 2 is assigned to Trainer 17
    (3, 18),  -- Trainee 3 is assigned to Trainer 18
    (4, 19),  -- Trainee 4 is assigned to Trainer 19
    (5, 20),  -- Trainee 5 is assigned to Trainer 20
    (6, 21),  -- Trainee 6 is assigned to Trainer 21
    (7, 22),  -- Trainee 7 is assigned to Trainer 22
    (8, 23),  -- Trainee 8 is assigned to Trainer 23
    (9, 24),  -- Trainee 9 is assigned to Trainer 24
    (10, 25), -- Trainee 10 is assigned to Trainer 25
    (11, 26), -- Trainee 11 is assigned to Trainer 26
    (12, 27), -- Trainee 12 is assigned to Trainer 27
    (13, 28), -- Trainee 13 is assigned to Trainer 28
    (14, 29), -- Trainee 14 is assigned to Trainer 29
    (15, 30); -- Trainee 15 is assigned to Trainer 30

-- Populating the TRAININGS table (15 trainings)
INSERT INTO TRAININGS (TRAINEE_ID, TRAINER_ID, NAME, TRAINING_TYPE_ID, DATE, DURATION)
VALUES
    (1, 16, 'Morning Strength', 1, '2025-10-10T07:30:00Z', 60),
    (1, 16, 'Morning Strength', 1, '2024-10-10T07:30:00Z', 120), -- In Past
    (2, 17, 'Powerlifting Session', 2, '2025-10-11T10:00:00Z', 90),
    (3, 18, 'CrossFit Endurance', 3, '2025-10-12T08:00:00Z', 75),
    (4, 19, 'Yoga Basics', 4, '2025-10-13T09:30:00Z', 60),
    (5, 20, 'Pilates Stretch', 5, '2024-10-14T10:00:00Z', 50), -- In Past
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
