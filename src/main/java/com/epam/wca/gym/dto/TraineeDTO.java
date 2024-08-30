package com.epam.wca.gym.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
public class TraineeDTO extends UserDTO {
    private Date dateOfBirth;
    private String address;

    public TraineeDTO(String firstName, String lastName, Date dateOfBirth, String address) {
        super(firstName, lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
