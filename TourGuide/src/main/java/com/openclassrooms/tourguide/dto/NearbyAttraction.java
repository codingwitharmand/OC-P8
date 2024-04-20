package com.openclassrooms.tourguide.dto;

import gpsUtil.location.Attraction;
import lombok.*;

@Getter
@Setter
public class NearbyAttraction extends Attraction {
    private double distance;
    private int rewardPoints;

    public NearbyAttraction(String attractionName, String city, String state, double latitude, double longitude, double distance, int rewardPoints) {
        super(attractionName, city, state, latitude, longitude);
        this.distance = distance;
        this.rewardPoints = rewardPoints;
    }
}
