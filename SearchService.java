package com.tjpeisde.onlinebooking.service;

import com.tjpeisde.onlinebooking.dao.LocationDao;
import com.tjpeisde.onlinebooking.dao.StayAvailabilityDao;
import com.tjpeisde.onlinebooking.dao.StayDao;
import com.tjpeisde.onlinebooking.entity.Stay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private LocationDao locationDao;
    private StayAvailabilityDao stayAvailabilityDao;
    private StayDao stayDao;

    @Autowired
    public SearchService(LocationDao locationDao, StayAvailabilityDao stayAvailabilityDao, StayDao stayDao){
        this.locationDao = locationDao;
        this.stayAvailabilityDao = stayAvailabilityDao;
        this.stayDao = stayDao;
    }

    public List<Stay> search(int guestNumber, LocalDate checkinDate, LocalDate checkoutDate, double lat, double lon, String distance){
        List<Long> stayIds = locationDao.searchByDistance(lat, lon, distance);
        long duration = Duration.between
                (checkinDate.atStartOfDay(), checkoutDate.atStartOfDay()).toDays();
        List<Long> filteredStayIds = stayAvailabilityDao.
                findByIdInAndDateBetweenAndStateIsAvailable
                        (stayIds, checkinDate, checkoutDate.minusDays(1), duration);
        return stayDao.findByIdInAndGuestNumberGreaterThanEqual(filteredStayIds, guestNumber);
    }

}
