-- Populating the USERS table (30 users in total, 15 trainees and 15 trainers)
INSERT INTO USERS (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, IS_ACTIVE)
VALUES
    ('John', 'Doe', 'john.doe', '$2b$12$enIN.nkQGvz1jH6FvFKmtOMA3z/LaN1qIH7BPQIF1XLUM4Zmjg9t2', true), -- password123
    ('Jane', 'Smith', 'jane.smith', '$2b$12$BeOlpZ4odLfeQSIrHLpwPe8vYtx9r30UaCKoKPSFv2JH/y9JDoYGC', true), -- password456
    ('Alice', 'Brown', 'alice.brown', '$2b$12$iFVW60Zr6kG8zcokVReX2OwH7c36HBJtTgOC8dntZYpBtcqp4/t4y', true), -- password789
    ('Michael', 'Johnson', 'michael.johnson', '$2b$12$CvXBpBm4oShKXMwSIJNclO4GtgIrSbE3Du0QWs7ZDDOLnEPPFKDIy', true), -- password111
    ('Emily', 'Davis', 'emily.davis', '$2b$12$rpVZLK9HiaDxUaloyIElH.ZqDjQTkA.wwbuueDU3nQPi65sr7oDbS', true), -- password222
    ('David', 'Garcia', 'david.garcia', '$2b$12$ZS0kskeaRbJBZ0oPTgIVc.Aui0cCB5p5F9BM8VFzaRRb9cjHSez5K', true), -- password333
    ('Emma', 'Martinez', 'emma.martinez', '$2b$12$CxQdDedkmh1nFKRR7X6D2eFCS5gCBkNTH/92jNNFZ64Ur96RMGYSm', true), -- password444
    ('Oliver', 'Lopez', 'oliver.lopez', '$2b$12$7ihbAMYOt64pSX3CKHkdEuJI5kXy9AKiYQOdvzrAsUnd9tHKIwTA.', true), -- password555
    ('Sophia', 'Gonzalez', 'sophia.gonzalez', '$2b$12$7e2IO5B83Eb0unbcvr/bUeGA93juWgTOTZZEy5yQPtSTTc5GDGvXO', true), -- password666
    ('James', 'Wilson', 'james.wilson', '$2b$12$b7zG5Wy48Qm7LOYoXjWKxOd0UzlSfigkJehIgHRnjNQRMLMBBp/ES', true), -- password777
    ('Charlotte', 'Moore', 'charlotte.moore', '$2b$12$sBumZOOB2MBxq6.Ia.7IYex2NNQBZEBNjLwYOhzROaMFkBhsN93hG', true), -- password888
    ('Benjamin', 'Taylor', 'benjamin.taylor', '$2b$12$zRv9MrjTjGHaSKQaEho3veaexpVfLIfluwvqgQ6j7n8oybJdQlVLS', true), -- password999
    ('Lucas', 'Anderson', 'lucas.anderson', '$2b$12$/BUsIS7nPMtYlvZ3YVqnZ.KD98eG5wpJkVCg14Zc87BjRmv/Kz/V.', true), -- password101
    ('Amelia', 'Thomas', 'amelia.thomas', '$2b$12$U1YVSoHBHie0DtHHWhDQq.6h6/fBGgV0cK52ROhqvwrJbYSiGdndC', true), -- password202
    ('Mason', 'Jackson', 'mason.jackson', '$2b$12$XmfwWP0/zEER0MPtl/FF4u2XXfayAn.g9gQTxjzPS4AAT/9lKetj.', true), -- password303
-- Trainers
    ('Ethan', 'White', 'ethan.white', '$2b$12$63dYDkOIzIJYN8X5naDRCO5W6ipvg0Pr2t1NE/YiA5GpDMuPD7OSK', true), -- password404
    ('Olivia', 'Harris', 'olivia.harris', '$2b$12$OGjeXXAZ3bfjiEHUOmRnwuI0o/PSlX2uHTfF3hsIckloS/YGL4VzO', true), -- password505
    ('Isabella', 'Clark', 'isabella.clark', '$2b$12$XKTMmZ9CQ/j2OY.hLWhYJO2OujrUwe0dWNJXNae4mPptgMUiuvaiu', true), -- password606
    ('Aiden', 'Lewis', 'aiden.lewis', '$2b$12$abNItLOghizbupXI3W/jwOTyKHuVVOkWy31wtmgMEfaAOEIVnYov2', true), -- password707
    ('Ava', 'Young', 'ava.young', '$2b$12$QKRAvHJAhkPj/wwwBnA1ZOEosgf.nFRBBQ93zc6TiUotKscR9l0va', true), -- password808
    ('Jacob', 'Walker', 'jacob.walker', '$2b$12$cOyO7869GC7Of9ruD4xGye.mFZatlVyrDeDy2HlsARr8ayeq2CjGW', true), -- password909
    ('Liam', 'Hall', 'liam.hall', '$2b$12$h0hLe/Bgx8BcrH62vxVgFeOrNrxOvN2/vbmodfO4G/UMZQf//dC7e', true), -- password010
    ('Mia', 'Allen', 'mia.allen', '$2b$12$mbGrGYnz.i2clgLGyhUkBeqKb2xPkQF1fnvYO/M.HgkBKguGBh8iS', true), -- password112
    ('Noah', 'King', 'noah.king', '$2b$12$Jm6qioSa679fkyZUka6euuKiET.XSVyNf33s8DqB/WNVGuS3sX/QG', true), -- password213
    ('Ella', 'Wright', 'ella.wright', '$2b$12$mo/P2CkOKgV5mG4KyWkTV.DJlpOKlhI3XqY4WDozSnIi.1yejRwB6', true), -- password314
    ('William', 'Scott', 'william.scott', '$2b$12$J8OXFC6ees6f34sAA7e9P.3VJBxp/ixMRAUl3L4BdFEJoR2N4lsLG', true), -- password415
    ('Abigail', 'Torres', 'abigail.torres', '$2b$12$5q2eaRCbf0mlFTHku8d/S.hGbmbL/ge16pQFG.yZeYSUTgLbrUvZ6', true), -- password516
    ('Elijah', 'Nguyen', 'elijah.nguyen', '$2b$12$hTZLqdBzk6Guxelz9SjKAeoKKf8XDwMQqxRljviADz0Y3h/GZ86aK', true), -- password617
    ('Avery', 'Hill', 'avery.hill', '$2b$12$egu8.534d/MzIGfzQ2T7IusKQnGvpw5dfITkyW91QfnQk4dx8ao86', true), -- password718
    ('Evelyn', 'Green', 'evelyn.green', '$2b$12$HiF9TMx9zuyuKqXB3nV7xuM0fbgl0gpCKgaixGPVEGzE.veEPPXSO', true); -- password819

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
