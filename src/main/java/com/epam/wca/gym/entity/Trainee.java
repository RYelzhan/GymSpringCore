package com.epam.wca.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee extends User{
    private Date dateOfBirth;
    private String address;
    private long userId;
}
