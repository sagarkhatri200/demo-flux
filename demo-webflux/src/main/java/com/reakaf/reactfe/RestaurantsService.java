package com.reakaf.reactfe;

import com.reakaf.reactfe.custom.KPublisher;
import org.springframework.stereotype.Service;

@Service
public class RestaurantsService {


    public KPublisher<Restaurant> Save(Restaurant restaurant) {
        return KPublisher.just(new Restaurant());
    }
}
