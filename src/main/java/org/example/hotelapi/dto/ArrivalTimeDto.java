package org.example.hotelapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ArrivalTimeDto {

    @NotNull(message = "Check-in time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time format. Expected HH:MM (14:00)")
    private String checkIn;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time format. Expected HH:MM (12:00)")
    private String checkOut;

    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }

    public void setCheckIn(String checkIn) { this.checkIn = checkIn; }
    public void setCheckOut(String checkOut) { this.checkOut = checkOut; }
}
