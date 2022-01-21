package com.tjpeisde.onlinebooking.dao;

import com.tjpeisde.onlinebooking.entity.Reservation;
import com.tjpeisde.onlinebooking.entity.Stay;
import com.tjpeisde.onlinebooking.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationDao extends JpaRepository<Reservation, Long> {

    List<Reservation> findByGuest(User guest);

    List<Reservation> findByStay(Stay stay);

    List<Reservation> findByStayAndCheckoutDateAfter(Stay stay, LocalDate date);
}
