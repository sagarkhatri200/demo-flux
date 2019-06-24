package com.reakaf.reactfe;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Restaurant {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String city;
    private int noOfLikes;
    private Boolean isApproved;
    private LocalDateTime dateCreated;
}
