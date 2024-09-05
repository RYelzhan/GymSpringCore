package com.epam.wca.gym.entity;

import com.epam.wca.gym.service.ProfileService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class Trainee extends User {
    private static ProfileService profileService;

    private LocalDate dateOfBirth;
    private String address;
    private long userId;

    public Trainee(String firstName,
                   String lastName,
                   LocalDate dateOfBirth,
                   String address) {

        super(firstName,
                lastName,
                profileService.createUserName(firstName, lastName),
                profileService.createPassword(),
                true);

        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public Trainee(String firstName,
                   String lastName,
                   String userName,
                   String password,
                   boolean isActive,
                   LocalDate dateOfBirth,
                   String address,
                   long userId) {

        super(firstName,
                lastName,
                userName,
                password,
                isActive);

        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.userId = userId;
    }

    public static void setProfileService(ProfileService profileService) {
        Trainee.profileService = profileService;
    }

    @Override
    public String toString() {
        return super.toString() +
                "dateOfBirth = " + dateOfBirth + '\n' +
                "address = " + address + '\n' +
                "userId = " + userId + '\n';
    }
}
