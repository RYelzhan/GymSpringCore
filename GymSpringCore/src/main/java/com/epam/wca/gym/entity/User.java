package com.epam.wca.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    private Long id;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof User otherUser)) {
            return false;
        }

        return Objects.equals(this.id, otherUser.id) &&
                Objects.equals(this.username, otherUser.username);
    }

    @Override
    public int hashCode() {
        // asked by SonarLink
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "\nid = " + id + '\n' +
                "firstName = " + firstname + '\n' +
                "lastName = " + lastname + '\n' +
                "userName = " + username + '\n';
    }
}
