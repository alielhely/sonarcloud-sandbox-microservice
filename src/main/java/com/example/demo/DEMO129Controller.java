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

    @GetMapping("/{rideId}")
    public Ride getRideDetails(@PathVariable String rideId) {
        return rideService.getRideDetails(rideId);
    }

    @PostMapping("/{rideId}/update")
    public Ride updateRide(@PathVariable String rideId, @RequestBody RideUpdateRequest request) {
        return rideService.updateRide(rideId, request);
    }

    @DeleteMapping("/{rideId}/cancel")
    public String cancelRide(@PathVariable String rideId) {
        rideService.cancelRide(rideId);
        return "Ride cancelled successfully";
    }
}

@Service
class RideService {

    private final List<Ride> rides = new ArrayList<>();

    public List<Ride> getUpcomingRides(String customerId) {
        // Mock implementation
        return rides.stream()
                .filter(ride -> ride.getCustomerId().equals(customerId) && ride.isUpcoming())
                .toList();
    }

    public Ride getRideDetails(String rideId) {
        // Mock implementation
        return rides.stream()
                .filter(ride -> ride.getId().equals(rideId))
                .findFirst()
                .orElseThrow(() -> new RideNotFoundException("Ride not found"));
    }

    public Ride updateRide(String rideId, RideUpdateRequest request) {
        // Mock implementation
        Optional<Ride> optionalRide = rides.stream()
                .filter(ride -> ride.getId().equals(rideId))
                .findFirst();

        if (optionalRide.isPresent()) {
            Ride ride = optionalRide.get();
            ride.setPickupLocation(request.getPickupLocation());
            return ride;
        } else {
            throw new RideNotFoundException("Ride not found");
        }
    }

    public void cancelRide(String rideId) {
        // Mock implementation
        rides.removeIf(ride -> ride.getId().equals(rideId));
    }
}

class Ride {
    private String id;
    private String customerId;
    private String date;
    private String time;
    private String driverInfo;
    private String pickupLocation;
    private boolean upcoming;

    // Getters and setters omitted for brevity
}

class RideUpdateRequest {
    private String pickupLocation;

    // Getters and setters omitted for brevity
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ride not found")
class RideNotFoundException extends RuntimeException {
    public RideNotFoundException(String message) {
        super(message);
    }
}