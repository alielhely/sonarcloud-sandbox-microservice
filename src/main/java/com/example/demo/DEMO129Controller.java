package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @PostMapping("/cancel")
    public String cancelRide(@RequestParam String rideId) {
        return rideService.cancelRide(rideId);
    }
}

@Service
class RideService {

    private final RideRepository rideRepository;
    private final NotificationService notificationService;

    public RideService(RideRepository rideRepository, NotificationService notificationService) {
        this.rideRepository = rideRepository;
        this.notificationService = notificationService;
    }

    public List<Ride> getUpcomingRides(String customerId) {
        List<Ride> rides = rideRepository.findByCustomerIdAndDateAfter(customerId, LocalDateTime.now());
        if (rides.isEmpty()) {
            notificationService.sendNotification(customerId, "No upcoming rides.");
        }
        return rides;
    }

    public String cancelRide(String rideId) {
        Optional<Ride> ride = rideRepository.findById(rideId);
        if (ride.isPresent()) {
            rideRepository.delete(ride.get());
            notificationService.sendNotification(ride.get().getCustomerId(), "Ride canceled successfully.");
            return "Ride canceled successfully.";
        }
        return "Ride not found.";
    }
}

@Repository
interface RideRepository {
    List<Ride> findByCustomerIdAndDateAfter(String customerId, LocalDateTime date);
    Optional<Ride> findById(String rideId);
    void delete(Ride ride);
}

@Service
class NotificationService {
    public void sendNotification(String customerId, String message) {
        // Logic to send notification
    }
}

class Ride {
    private String id;
    private String customerId;
    private LocalDateTime date;
    private String pickupLocation;
    private String driverName;
    private String driverContact;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleLicensePlate;

    // Getters and Setters
}