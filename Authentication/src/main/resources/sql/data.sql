-- Script for User Entity with PASSWORD
INSERT INTO USERS (ID, USERNAME, PASSWORD, IS_ACTIVE)
VALUES
    (1, 'john.doe', '$2b$12$enIN.nkQGvz1jH6FvFKmtOMA3z/LaN1qIH7BPQIF1XLUM4Zmjg9t2', true), -- password123
    (2, 'jane.smith', '$2b$12$BeOlpZ4odLfeQSIrHLpwPe8vYtx9r30UaCKoKPSFv2JH/y9JDoYGC', true), -- password456
    (3, 'alice.brown', '$2b$12$iFVW60Zr6kG8zcokVReX2OwH7c36HBJtTgOC8dntZYpBtcqp4/t4y', true), -- password789
    (4, 'michael.johnson', '$2b$12$CvXBpBm4oShKXMwSIJNclO4GtgIrSbE3Du0QWs7ZDDOLnEPPFKDIy', true), -- password111
    (5, 'emily.davis', '$2b$12$rpVZLK9HiaDxUaloyIElH.ZqDjQTkA.wwbuueDU3nQPi65sr7oDbS', true), -- password222
    (6, 'david.garcia', '$2b$12$ZS0kskeaRbJBZ0oPTgIVc.Aui0cCB5p5F9BM8VFzaRRb9cjHSez5K', true), -- password333
    (7, 'emma.martinez', '$2b$12$CxQdDedkmh1nFKRR7X6D2eFCS5gCBkNTH/92jNNFZ64Ur96RMGYSm', true), -- password444
    (8, 'oliver.lopez', '$2b$12$7ihbAMYOt64pSX3CKHkdEuJI5kXy9AKiYQOdvzrAsUnd9tHKIwTA.', true), -- password555
    (9, 'sophia.gonzalez', '$2b$12$7e2IO5B83Eb0unbcvr/bUeGA93juWgTOTZZEy5yQPtSTTc5GDGvXO', true), -- password666
    (10, 'james.wilson', '$2b$12$b7zG5Wy48Qm7LOYoXjWKxOd0UzlSfigkJehIgHRnjNQRMLMBBp/ES', true), -- password777
    (11, 'charlotte.moore', '$2b$12$sBumZOOB2MBxq6.Ia.7IYex2NNQBZEBNjLwYOhzROaMFkBhsN93hG', true), -- password888
    (12, 'benjamin.taylor', '$2b$12$zRv9MrjTjGHaSKQaEho3veaexpVfLIfluwvqgQ6j7n8oybJdQlVLS', true), -- password999
    (13, 'lucas.anderson', '$2b$12$/BUsIS7nPMtYlvZ3YVqnZ.KD98eG5wpJkVCg14Zc87BjRmv/Kz/V.', true), -- password101
    (14, 'amelia.thomas', '$2b$12$U1YVSoHBHie0DtHHWhDQq.6h6/fBGgV0cK52ROhqvwrJbYSiGdndC', true), -- password202
    (15, 'mason.jackson', '$2b$12$XmfwWP0/zEER0MPtl/FF4u2XXfayAn.g9gQTxjzPS4AAT/9lKetj.', true), -- password303
-- Trainers
    (16, 'ethan.white', '$2b$12$63dYDkOIzIJYN8X5naDRCO5W6ipvg0Pr2t1NE/YiA5GpDMuPD7OSK', true), -- password404
    (17, 'olivia.harris', '$2b$12$OGjeXXAZ3bfjiEHUOmRnwuI0o/PSlX2uHTfF3hsIckloS/YGL4VzO', true), -- password505
    (18, 'isabella.clark', '$2b$12$XKTMmZ9CQ/j2OY.hLWhYJO2OujrUwe0dWNJXNae4mPptgMUiuvaiu', true), -- password606
    (19, 'aiden.lewis', '$2b$12$abNItLOghizbupXI3W/jwOTyKHuVVOkWy31wtmgMEfaAOEIVnYov2', true), -- password707
    (20, 'ava.young', '$2b$12$QKRAvHJAhkPj/wwwBnA1ZOEosgf.nFRBBQ93zc6TiUotKscR9l0va', true), -- password808
    (21, 'jacob.walker', '$2b$12$cOyO7869GC7Of9ruD4xGye.mFZatlVyrDeDy2HlsARr8ayeq2CjGW', true), -- password909
    (22, 'liam.hall', '$2b$12$h0hLe/Bgx8BcrH62vxVgFeOrNrxOvN2/vbmodfO4G/UMZQf//dC7e', true), -- password010
    (23, 'mia.allen', '$2b$12$mbGrGYnz.i2clgLGyhUkBeqKb2xPkQF1fnvYO/M.HgkBKguGBh8iS', true), -- password112
    (24, 'noah.king', '$2b$12$Jm6qioSa679fkyZUka6euuKiET.XSVyNf33s8DqB/WNVGuS3sX/QG', true), -- password213
    (25, 'ella.wright', '$2b$12$bySc0E96ihWqfQqgtiHwVOn4iY/Q9cXqqkNQ2c1ntcGRRQ9tXa9OS', true), -- password314
    (26, 'william.scott', '$2b$12$J8OXFC6ees6f34sAA7e9P.3VJBxp/ixMRAUl3L4BdFEJoR2N4lsLG', true), -- password415
    (27, 'abigail.torres', '$2b$12$5q2eaRCbf0mlFTHku8d/S.hGbmbL/ge16pQFG.yZeYSUTgLbrUvZ6', true), -- password516
    (28, 'elijah.nguyen', '$2b$12$hTZLqdBzk6Guxelz9SjKAeoKKf8XDwMQqxRljviADz0Y3h/GZ86aK', true), -- password617
    (29, 'avery.hill', '$2b$12$egu8.534d/MzIGfzQ2T7IusKQnGvpw5dfITkyW91QfnQk4dx8ao86', true), -- password718
    (30, 'evelyn.green', '$2b$12$HiF9TMx9zuyuKqXB3nV7xuM0fbgl0gpCKgaixGPVEGzE.veEPPXSO', true); -- password819

INSERT INTO ROLES (ID, NAME)
    VALUES
        (1, 'TRAINEE'),
        (2, 'TRAINER');

INSERT INTO USER_ROLE_MAPPING (USER_ID, ROLE_ID)
    VALUES
        (1, 1),
        (2, 1),
        (3, 1),
        (4, 1),
        (5, 1),
        (6, 1),
        (7, 1),
        (8, 1),
        (9, 1),
        (10, 1),
        (11, 1),
        (12, 1),
        (13, 1),
        (14, 1),
        (15, 1),
        -- Trainers
        (16, 2),
        (17, 2),
        (18, 2),
        (19, 2),
        (20, 2),
        (21, 2),
        (22, 2),
        (23, 2),
        (24, 2),
        (25, 2),
        (26, 2),
        (27, 2),
        (28, 2),
        (29, 2),
        (30, 2);

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
    ('mason.jackson', 1),
    ('ethan.white', 1),
    ('olivia.harris', 1),
    ('isabella.clark', 1),
    ('aiden.lewis', 1),
    ('ava.young', 1),
    ('jacob.walker', 1),
    ('liam.hall', 1),
    ('mia.allen', 1),
    ('noah.king', 1),
    ('ella.wright', 1),
    ('william.scott', 1),
    ('abigail.torres', 1),
    ('elijah.nguyen', 1),
    ('avery.hill', 1),
    ('evelyn.green', 1);
