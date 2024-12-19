package com.epam.wca.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Value("${table-generator.initial-value}")
    private static final int ID_INITIAL_VALUE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_id_table_generator")
    @TableGenerator(
            name = "user_id_table_generator",
            table = "user_id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "user_id",
            initialValue = ID_INITIAL_VALUE,
            allocationSize = 1
    )
    private Long id;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", nullable = false)
    private String lastname;
    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    public User(
            String firstname,
            String lastname,
            String username,
            boolean isActive
    ) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.isActive = isActive;
    }

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
                "userName = " + username + '\n' +
                "isActive = " + isActive + '\n';
    }
}
