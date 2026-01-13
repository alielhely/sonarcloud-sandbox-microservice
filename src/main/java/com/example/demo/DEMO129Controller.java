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
    public ResponseEntity<?> getUpcomingRides(@RequestParam boolean hasRides, @RequestParam boolean systemError) {
        if (systemError) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to retrieve ride details at this time. Please try again later.");
        }

        if (!hasRides) {
            return ResponseEntity.ok("No upcoming rides found");
        }

        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("2023-10-15", "10:00 AM", "123 Main St", "456 Elm St"));
        rides.add(new Ride("2023-10-16", "11:00 AM", "789 Oak St", "321 Pine St"));

        return ResponseEntity.ok(rides);
    }

    @GetMapping("/upcoming/{rideId}")
    public ResponseEntity<?> getRideDetails(@PathVariable int rideId, @RequestParam boolean systemError) {
        if (systemError) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to retrieve ride details at this time. Please try again later.");
        }

        RideDetail rideDetail = new RideDetail("John Doe", "Toyota Prius", 25.00);
        return ResponseEntity.ok(rideDetail);
    }
}

class Ride {
    private String date;
    private String time;
    private String pickupLocation;
    private String destination;

    public Ride(String date, String time, String pickupLocation, String destination) {
        this.date = date;
        this.time = time;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
    }

    // Getters and setters omitted for brevity
}

class RideDetail {
    private String driverName;
    private String vehicleDetails;
    private double estimatedFare;

    public RideDetail(String driverName, String vehicleDetails, double estimatedFare) {
        this.driverName = driverName;
        this.vehicleDetails = vehicleDetails;
        this.estimatedFare = estimatedFare;
    }

    // Getters and setters omitted for brevity
}