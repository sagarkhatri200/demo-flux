package com.reakaf.reactfe;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@RestController
public class RestaurantsControllerNonReactive {

    @PostMapping("/restaurantsnonreactive")
    public Restaurant post(@RequestBody Restaurant restaurant) throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getId() + ", Controller Method Thread");
        Restaurant restaurant1 = null;
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + ", mapping id: ");
            Thread.sleep(1L);

            System.out.println("Thread " + Thread.currentThread().getId() + ", justing to c");
            Thread.sleep(1);

            System.out.println("Thread " + Thread.currentThread().getId() + ", extracing sync method");
            HashMap<String, String> etxractMap = BlockingMethod();

            System.out.println("Thread " + Thread.currentThread().getId() + ", Extracting the Id from the HashMap");
            String id = etxractMap.get("ID");

            System.out.println("Thread " + Thread.currentThread().getId() + ", getting detail: " + id);

            restaurant1 = getDetail(id).block();
            System.out.println("Thread " + Thread.currentThread().getId() + ", getting menu: " + id);

            Restaurant menu = getMenu(id, "4f47711f-2ed8-43b4-9e43-747744d1aaee").block();
            Thread.sleep(1);
        } catch (Exception ex) {
            System.out.println("Thread " + Thread.currentThread().getId() + ", Error Happened in the pipeline");
        }

        System.out.println("Thread " + Thread.currentThread().getId() + ", Terminated");

        return restaurant1;
    }

    public Mono<Restaurant> getDetail(String id) {
        System.out.println("Thread " + Thread.currentThread().getId() + ", getting detail in call");
        return WebClient
                .create("https://www.5milerestaurants.com")
                .get()
                .uri("/api/facade/RestaurantDetailsById?id=" + id)
                .retrieve()
                .bodyToMono(Restaurant.class);
    }

    public Mono<Restaurant> getMenu(String id, String menuId) {
        System.out.println("Thread " + Thread.currentThread().getId() + ", getting menu in call");
        return WebClient
                .create("https://www.5milerestaurants.com")
                .get()
                .uri("/api/facade/MenuByRestaurantId/" + id + "?menuId=" + menuId)
                .retrieve()
                .bodyToMono(Restaurant.class);
    }


    private HashMap<String, String> BlockingMethod() {
        HashMap<String, String> map = new HashMap<>();
        try {
            System.out.println("Thread " + Thread.currentThread().getId() + ", From Supplier Method");
            map.put("ID", "0e3501f4-0659-425b-8522-a4c3bd558946");
            map.put("MENUID", "4f47711f-2ed8-43b4-9e43-747744d1aaee");
            System.out.println("Thread " + Thread.currentThread().getId() + ", returning map with delay");
        } finally {
        }
        return map;
    }
}
