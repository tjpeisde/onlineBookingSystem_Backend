package com.tjpeisde.onlinebooking.dao;

import java.util.List;

public interface CustomLocationDao {

    List<Long> searchByDistance(double lat, double lon, String distance);
}
