package com.tjpeisde.onlinebooking.service;

import com.tjpeisde.onlinebooking.entity.Location;
import com.tjpeisde.onlinebooking.exception.GeoEncodingException;
import com.tjpeisde.onlinebooking.exception.InvalidStayAddressException;
import org.springframework.stereotype.Service;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.elasticsearch.common.geo.GeoPoint;
import java.io.IOException;

@Service
public class GeoEncodingService {
    @Value("${geocoding.apikey}")
    private String apiKey;

    public Location getLatLng(Long id, String address)
            throws InvalidStayAddressException, GeoEncodingException {
        GeoApiContext context = new GeoApiContext.Builder().apiKey(apiKey).build();
        try {
            GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
            if (result.partialMatch)
                throw new InvalidStayAddressException("Failed to find stay address");
            return new Location(id, new GeoPoint(result.geometry.location.lat, result.geometry.location.lng));
        } catch (IOException | ApiException | InterruptedException e) {
            e.printStackTrace();
            throw new GeoEncodingException("Failed to encode stay address");
        }
    }

}



