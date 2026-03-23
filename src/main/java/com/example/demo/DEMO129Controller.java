package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}

@RestController
@RequestMapping("/api/rides")
class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/upcoming")
    public List<Ride> getUpcomingRides(@RequestParam String customerId) {
        return rideService.getUpcomingRides(customerId);
    }

    @GetMapping("/details/{rideId}")
    public RideDetails getRideDetails(@PathVariable String rideId) {
        return rideService.getRideDetails(rideId)
                .orElseThrow(() -> new RideNotFoundException("Ride details not found"));
    }
}

@Service
class RideService {

    public List<Ride> getUpcomingRides(String customerId) {
        // Mock implementation
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride("1", "Pickup Location", "Destination", "2023-10-10", "10:00 AM"));
        return rides;
    }

    public Optional<RideDetails> getRideDetails(String rideId) {
        // Mock implementation
        if ("1".equals(rideId)) {
            return Optional.of(new RideDetails("Pickup Location", "Destination", "2023-10-10", "10:00 AM", 
                    "Driver Name", "Driver Contact", "Car Make", "Car Model", "License Plate"));
        }
        return Optional.empty();
    }
}

class Ride {
    private String id;
    private String pickupLocation;
    private String destination;
    private String date;
    private String time;

    // Constructors, getters, and setters omitted for brevity
}

class RideDetails {
    private String pickupLocation;
    private String destination;
    private String date;
    private String time;
    private String driverName;
    private String driverContact;
    private String vehicleMake;
    private String vehicleModel;
    private String licensePlate;

    // Constructors, getters, and setters omitted for brevity
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }
}