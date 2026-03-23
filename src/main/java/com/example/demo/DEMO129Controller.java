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
    public List<Ride> getUpcomingRides(@RequestParam Long customerId) {
        try {
            List<Ride> rides = rideService.getUpcomingRides(customerId);
            if (rides.isEmpty()) {
                throw new RideNotFoundException("No upcoming rides");
            }
            return rides;
        } catch (RideNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve ride details, please try again later");
        }
    }

    @GetMapping("/upcoming/{rideId}")
    public Ride getRideDetails(@PathVariable Long rideId) {
        try {
            return rideService.getRideDetails(rideId)
                    .orElseThrow(() -> new RideNotFoundException("Ride not found"));
        } catch (RideNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to retrieve ride details, please try again later");
        }
    }
}

@Service
class RideService {

    public List<Ride> getUpcomingRides(Long customerId) {
        // Mock implementation, replace with actual data retrieval logic
        List<Ride> rides = new ArrayList<>();
        if (customerId == 1L) {
            rides.add(new Ride(1L, "2023-12-01", "10:00 AM", "123 Main St", "456 Elm St", "John Doe", 25.00));
        }
        return rides;
    }

    public Optional<Ride> getRideDetails(Long rideId) {
        // Mock implementation, replace with actual data retrieval logic
        if (rideId == 1L) {
            return Optional.of(new Ride(1L, "2023-12-01", "10:00 AM", "123 Main St", "456 Elm St", "John Doe", 25.00));
        }
        return Optional.empty();
    }
}

class Ride {
    private Long id;
    private String date;
    private String time;
    private String pickupLocation;
    private String destination;
    private String driverName;
    private Double estimatedFare;

    public Ride(Long id, String date, String time, String pickupLocation, String destination, String driverName, Double estimatedFare) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.pickupLocation = pickupLocation;
        this.destination = destination;
        this.driverName = driverName;
        this.estimatedFare = estimatedFare;
    }

    // Getters and setters omitted for brevity
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }
}