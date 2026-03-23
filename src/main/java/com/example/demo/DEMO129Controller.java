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
@RequestMapping("/rides")
class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @GetMapping("/upcoming")
    public List<Ride> getUpcomingRides() {
        return rideService.getUpcomingRides();
    }

    @GetMapping("/upcoming/{rideId}")
    public Ride getRideDetails(@PathVariable Long rideId) {
        return rideService.getRideDetails(rideId);
    }
}

@Service
class RideService {

    public List<Ride> getUpcomingRides() {
        // Mock implementation
        List<Ride> rides = new ArrayList<>();
        rides.add(new Ride(1L, "2023-10-15", "10:00 AM", "123 Main St", "456 Elm St", "John Doe", "Sedan", 20.0));
        return rides;
    }

    public Ride getRideDetails(Long rideId) {
        // Mock implementation
        Optional<Ride> ride = getUpcomingRides().stream().filter(r -> r.getId().equals(rideId)).findFirst();
        if (ride.isPresent()) {
            return ride.get();
        } else {
            throw new RideNotFoundException("Unable to retrieve ride details, please try again later");
        }
    }
}

class Ride {
    private Long id;
    private String date;
    private String time;
    private String pickupLocation;
    private String destination;
    private String driverName;
    private String vehicleType;
    private Double estimatedFare;

    // Constructors, getters and setters

    public Ride(Long id, String date, String time, String pickupLocation, String destination, String driverName, String vehicleType, Double estimatedFare) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.driverName = driverName;
        this.vehicleType = vehicleType;
        this.estimatedFare = estimatedFare;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Double getEstimatedFare() {
        return estimatedFare;
    }
}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }
}