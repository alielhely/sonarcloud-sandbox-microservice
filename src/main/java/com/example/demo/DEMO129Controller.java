package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.ArrayList;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/rides")
class RideController {

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingRides(@RequestParam String customerId) {
        try {
            List<Ride> rides = RideService.getUpcomingRides(customerId);
            if (rides.isEmpty()) {
                return ResponseEntity.ok("No upcoming rides.");
            }
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving ride details. Please try again later.");
        }
    }

    @GetMapping("/upcoming/{rideId}")
    public ResponseEntity<?> getRideDetails(@PathVariable String rideId) {
        try {
            Ride ride = RideService.getRideDetails(rideId);
            if (ride == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found.");
            }
            return ResponseEntity.ok(ride);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving ride details. Please try again later.");
        }
    }
}

class RideService {

    public static List<Ride> getUpcomingRides(String customerId) {
        // Mock implementation
        List<Ride> rides = new ArrayList<>();
        if ("customerWithRides".equals(customerId)) {
            rides.add(new Ride("1", "2023-10-10", "10:00 AM", "Location A", "Location B"));
            rides.add(new Ride("2", "2023-10-11", "11:00 AM", "Location C", "Location D"));
        }
        return rides;
    }

    public static Ride getRideDetails(String rideId) {
        // Mock implementation
        if ("1".equals(rideId)) {
            return new Ride("1", "2023-10-10", "10:00 AM", "Location A", "Location B");
        }
        return null;
    }
}

class Ride {
    private String id;
    private String date;
    private String time;
    private String pickupLocation;
    private String destination;

    public Ride(String id, String date, String time, String pickupLocation, String destination) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
}