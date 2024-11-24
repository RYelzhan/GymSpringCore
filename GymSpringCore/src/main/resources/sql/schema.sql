CREATE TABLE TRAINEE_TRAINER_MAPPING (
                                         TRAINEE_ID BIGINT NOT NULL,
                                         TRAINER_ID BIGINT NOT NULL,
                                         PRIMARY KEY (TRAINEE_ID, TRAINER_ID),
                                         FOREIGN KEY (TRAINEE_ID) REFERENCES TRAINEES(ID),
                                         FOREIGN KEY (TRAINER_ID) REFERENCES TRAINERS(ID)
);