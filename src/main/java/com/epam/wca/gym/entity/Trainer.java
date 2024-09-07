package com.epam.wca.gym.entity;

import com.epam.wca.gym.service.ProfileService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Trainer extends User {
    private static ProfileService profileService;

    private TrainingType specialization;

    private long userId;

    public Trainer(String firstName,
                   String lastName,
                   TrainingType specialization) {
        super(firstName,
                lastName,
                profileService.createUserName(firstName, lastName),
                profileService.createPassword(),
                true);

        this.specialization = specialization;
    }

    public Trainer(String firstName,
                   String lastName,
                   String userName,
                   String password,
                   boolean isActive,
                   TrainingType specialization,
                   long userId) {
        super(firstName,
                lastName,
                userName,
                password,
                isActive);

        this.specialization = specialization;
        this.userId = userId;
    }

    public static void setProfileService(ProfileService profileService) {
        Trainer.profileService = profileService;
    }

    @Override
    public String toString() {
        return super.toString() +
                "specialization = " + specialization + '\n' +
                "userId = " + userId + '\n';
    }
}
