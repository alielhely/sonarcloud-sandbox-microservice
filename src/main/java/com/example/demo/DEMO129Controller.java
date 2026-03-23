package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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
    public ResponseEntity<?> getUpcomingRides(@RequestParam Long customerId) {
        try {
            List<Ride> rides = rideService.getUpcomingRides(customerId);
            if (rides.isEmpty()) {
                return ResponseEntity.ok("No upcoming rides");
            }
            return ResponseEntity.ok(rides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error loading upcoming rides. Please try again later.");
        }
    }

    @GetMapping("/upcoming/{rideId}")
    public ResponseEntity<?> getRideDetails(@PathVariable Long rideId) {
        Optional<Ride> ride = rideService.getRideDetails(rideId);
        return ride.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ride not found"));
    }

    @PutMapping("/upcoming/{rideId}")
    public ResponseEntity<?> updateRideDetails(@PathVariable Long rideId, @RequestBody Ride updatedRide) {
        try {
            Ride ride = rideService.updateRideDetails(rideId, updatedRide);
            return ResponseEntity.ok(ride);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating ride details. Please try again later.");
        }
    }
}

@Service
class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public List<Ride> getUpcomingRides(Long customerId) {
        return rideRepository.findUpcomingRidesByCustomerId(customerId);
    }

    public Optional<Ride> getRideDetails(Long rideId) {
        return rideRepository.findById(rideId);
    }

    public Ride updateRideDetails(Long rideId, Ride updatedRide) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        ride.setDate(updatedRide.getDate());
        ride.setTime(updatedRide.getTime());
        ride.setPickupLocation(updatedRide.getPickupLocation());
        ride.setDestination(updatedRide.getDestination());
        ride.setDriverName(updatedRide.getDriverName());
        ride.setVehicleType(updatedRide.getVehicleType());
        ride.setContactInfo(updatedRide.getContactInfo());
        return rideRepository.save(ride);
    }
}

@Repository
interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findUpcomingRidesByCustomerId(Long customerId);
}

@Entity
class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private String date;
    private String time;
    private String pickupLocation;
    private String destination;
    private String driverName;
    private String vehicleType;
    private String contactInfo;

    // Getters and Setters
}