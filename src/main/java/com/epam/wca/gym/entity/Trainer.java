package com.epam.wca.gym.entity;

import com.epam.wca.gym.service.ProfileService;
import com.epam.wca.gym.service.impl.ProfileServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class Trainer extends User{
    private static ProfileService profileService = new ProfileServiceImpl();

    private TrainingType specialization;
    private long userId;

    public Trainer(String firstName, String lastName, TrainingType specialization, long userId) {
        super(firstName, lastName, profileService.createUserName(firstName, lastName), profileService.createPassword(), true);
        this.specialization = specialization;
        this.userId = userId;
    }
    public Trainer(String firstName, String lastName, String userName, String password, boolean isActive, TrainingType specialization, long userId) {
        super(firstName, lastName, userName, password, isActive);
        this.specialization = specialization;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                super.toString() +
                "specialization=" + specialization +
                ", userId=" + userId +
                "}";
    }
}
