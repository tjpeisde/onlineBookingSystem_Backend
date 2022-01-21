package com.tjpeisde.onlinebooking.service;


import com.tjpeisde.onlinebooking.dao.LocationDao;
import com.tjpeisde.onlinebooking.dao.ReservationDao;
import com.tjpeisde.onlinebooking.dao.StayDao;
import com.tjpeisde.onlinebooking.entity.*;
import com.tjpeisde.onlinebooking.exception.StayDeleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StayService {
    private StayDao stayDao;
    private LocationDao locationDao;
    private ReservationDao reservationDao;
    private ImageStorageService imageStorageService;
    private GeoEncodingService geoEncodingService;

    @Autowired
    public StayService(StayDao stayDao, LocationDao locationDao, ReservationDao reservationDao,ImageStorageService imageStorageService, GeoEncodingService geoEncodingService){
        this.stayDao = stayDao;
        this.locationDao = locationDao;
        this.reservationDao = reservationDao;
        this.imageStorageService = imageStorageService;
        this.geoEncodingService = geoEncodingService;
    }

    public Stay findById(Long stayId){
        return stayDao.findById(stayId).orElse(null);
    }

    public void deleteById(Long stayId){
        stayDao.deleteById(stayId);
    }

    public List<Stay> listByUser(String username) {
        return stayDao.findByHost(new User.Builder().setUsername(username).build());
    }

    public void add(Stay stay, MultipartFile[] images) {
        LocalDate date = LocalDate.now().plusDays(1);
        List<StayAvailability> availabilities = new ArrayList<>();
        for (int i = 0; i < 30 ; i++) {
            availabilities.add(
                    new StayAvailability.Builder()
                            .setId(new StayAvailabilityKey(stay.getId(),date))
                            .setStay(stay)
                            .setState(StayAvailabilityState.AVAILABLE)
                            .build());
            date = date.plusDays(1);

        }
        stay.setAvailabilities(availabilities);
        List<String> mediaLinks = Arrays.stream(images).
                parallel().map(
                        image -> imageStorageService.save(image))
                .collect(Collectors.toList());
        List<StayImage> stayImages = new ArrayList<>();
        for (String mediaLink : mediaLinks) {
            stayImages.add(new StayImage(mediaLink, stay));
        }
        stay.setImages(stayImages);
        stayDao.save(stay);

        Location location = geoEncodingService.getLatLng(stay.getId(), stay.getAddress());
        locationDao.save(location);
    }
    public void delete(Long stayId) throws StayDeleteException {
        List<Reservation> reservations = reservationDao.findByStayAndCheckoutDateAfter(
                new Stay.Builder().setId(stayId).build(), LocalDate.now());
            if (reservations != null && reservations.size() > 0)
                throw new StayDeleteException("Cannot delete stay with active reservation");
        stayDao.deleteById(stayId);
    }
}
