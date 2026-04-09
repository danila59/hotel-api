package org.example.hotelapi.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Contacts {
    private String phone;
    private String email;

    public Contacts() {}

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
