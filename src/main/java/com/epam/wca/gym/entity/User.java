package com.epam.wca.gym.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "FIRSTNAME", nullable = false)
    private String firstName;
    @Column(name = "LASTNAME", nullable = false)
    private String lastName;
    @Column(name = "USERNAME", nullable = false)
    private String userName;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Column(name = "IS_ACTIVE", nullable = false)
    private boolean isActive;

    public User(String firstName,
                String lastName,
                String userName,
                String password,
                boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
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
                Objects.equals(this.userName, otherUser.userName);
    }

    @Override
    public int hashCode() {
        // asked by SonarLink
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "id = " + id + '\n' +
                "firstName = " + firstName + '\n' +
                "lastName = " + lastName + '\n' +
                "userName = " + userName + '\n' +
                "isActive = " + isActive + '\n';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Object getRole() {
        return new SimpleGrantedAuthority("USER");
    }
}
