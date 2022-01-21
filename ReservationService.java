package com.tjpeisde.onlinebooking.service;

import com.tjpeisde.onlinebooking.dao.ReservationDao;
import com.tjpeisde.onlinebooking.dao.StayAvailabilityDao;
import com.tjpeisde.onlinebooking.entity.Reservation;
import com.tjpeisde.onlinebooking.entity.Stay;
import com.tjpeisde.onlinebooking.entity.User;
import com.tjpeisde.onlinebooking.exception.ReservationCollisionException;
import com.tjpeisde.onlinebooking.exception.ReservationNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private StayAvailabilityDao stayAvailabilityDao;

    @Autowired
    public ReservationService(ReservationDao reservationDao, StayAvailabilityDao stayAvailabilityDao){
        this.reservationDao = reservationDao;
        this.stayAvailabilityDao = stayAvailabilityDao;
    }

    public List<Reservation> listByGuest(String username){
        return reservationDao.findByGuest(new User.Builder().setUsername(username).build());
    }

    public List<Reservation> listByStay(Long stayId) {
        return reservationDao.findByStay(new
                Stay.Builder().setId(stayId).build());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void add(Reservation reservation) throws ReservationCollisionException {
        //check availability of the given stay
        //if success, continue save reservation, else return collision
        List<LocalDate> dates = stayAvailabilityDao.findByIdAndDateBetweenAndStateIsAvailable
                (reservation.getStay().getId(), reservation.getCheckinDate(), reservation.getCheckoutDate().minusDays(1));
        int duration = (int) Duration.between(reservation.getCheckinDate().atStartOfDay(),
                reservation.getCheckoutDate().atStartOfDay()).toDays();
        if(duration != dates.size()){
            throw new ReservationCollisionException("Duplicate Reservation");
        }
        //update stay availability
        stayAvailabilityDao.reserveByDateBetweenAndId(reservation.getStay().getId(), reservation.getCheckinDate(), reservation.getCheckoutDate().minusDays(1));
        reservationDao.save(reservation);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(
                () -> new ReservationNotFoundException("Reservation is not available"));
        stayAvailabilityDao.cancelByDateBetweenAndId(
                reservation.getStay().getId(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate().minusDays(1));
        reservationDao.deleteById(reservationId);
    }
}
