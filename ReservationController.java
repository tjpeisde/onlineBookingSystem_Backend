package com.tjpeisde.onlinebooking.controller;

import com.tjpeisde.onlinebooking.entity.Reservation;
import com.tjpeisde.onlinebooking.entity.User;
import com.tjpeisde.onlinebooking.exception.InvalidReservationDateException;
import com.tjpeisde.onlinebooking.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    @GetMapping(value = "/reservations")
    public List<Reservation> listReservations(Principal principal) {//Principal是token里面的信息
        return reservationService.listByGuest(principal.getName());
    }
    @PostMapping("/reservations")
    public void addReservation(@RequestBody Reservation reservation, Principal principal) {
        LocalDate checkinDate = reservation.getCheckinDate();
        LocalDate checkoutDate = reservation.getCheckoutDate();
        if (checkinDate.equals(checkoutDate) ||
                checkinDate.isAfter(checkoutDate) ||
                checkinDate.isBefore(LocalDate.now())) {
            throw new InvalidReservationDateException("Invalid date for reservation");
        }
        reservation.setGuest(new User.Builder().setUsername(principal.getName()).build());
        reservationService.add(reservation);
    }
    @DeleteMapping("/reservations/{reservationId}")
    public void deleteReservation(@PathVariable Long reservationId) {
        reservationService.delete(reservationId);
    }
}
