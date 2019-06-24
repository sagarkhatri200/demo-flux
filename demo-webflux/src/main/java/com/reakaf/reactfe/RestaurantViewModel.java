package com.reakaf.reactfe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantViewModel {
    private UUID id;
    private String name;
    private String city;
    private int noOfLikes;
}
