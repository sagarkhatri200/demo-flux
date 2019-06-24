package com.reakaf.reactfe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reakaf.reactfe.custom.KCustomMonoOperator;
import com.reakaf.reactfe.custom.KPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@RestController
public class RestaurantsController {

    @Autowired
    RestaurantsService restaurantsService;

    public static RestaurantViewModel mapIntoViewModel(Restaurant restaurant) {
        return new ObjectMapper().convertValue(restaurant, RestaurantViewModel.class);
    }

    @PostMapping("/restaurantscustom")
    public KPublisher<Restaurant> post(@RequestBody Restaurant restaurant) throws InterruptedException {
        System.out.println("Thread " + Thread.currentThread().getId() + ", Controller Method Thread");
        Mono cat = Mono
                .just("0e3501f4-0659-425b-8522-a4c3bd558946")
                .flatMap(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", mapping id: " + c);
                    return Mono.just(c).delayElement(Duration.ofMillis(1));
                })
                .flatMap(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", justing to c");
                    return Mono.just(c).delayElement(Duration.ofMillis(1));
                })
                .flatMap(d -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", extracing sync method");
                    return Mono.fromFuture(extractMenuId());
                })
                .map(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", Extracting the Id from the HashMap");
                    return c.get("ID");
                })
                .flatMap(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", getting detail: " + c);
                    return getDetail(c);
                })
                .flatMap(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", getting menu: " + c.getId());
                    return getMenu(c.getId().toString(), "4f47711f-2ed8-43b4-9e43-747744d1aaee");
                })
                .delayElement(Duration.ofMillis(1))
                .doOnError(ex -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", Error Happened in the pipeline");
                })
                .doOnTerminate(() -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + ", Terminated");
                });


        Mono cat2 = new KCustomMonoOperator(cat, "SeedValue");

        cat2.subscribe(c -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + "," + c);
                },
                e -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + "," + e);
                },
                () -> {
                    System.out.println("Thread " + Thread.currentThread().getId() + "," + "Completed!");
                });

        return restaurantsService
                .Save(restaurant);
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

    public CompletableFuture<HashMap<String, String>> extractMenuId() {
        System.out.println("Thread " + Thread.currentThread().getId() + ", building completeable future");
        CompletableFuture<HashMap<String, String>> extractFuture = CompletableFuture.supplyAsync(() -> {
            HashMap<String, String> map = new HashMap<>();
            try {
                System.out.println("Thread " + Thread.currentThread().getId() + ", From Supplier Method");
                map.put("ID", "0e3501f4-0659-425b-8522-a4c3bd558946");
                map.put("MENUID", "4f47711f-2ed8-43b4-9e43-747744d1aaee");
                System.out.println("Thread " + Thread.currentThread().getId() + ", returning map with delay");
            } finally {
            }
            return map;
        });
        System.out.println("Thread " + Thread.currentThread().getId() + ", returning completeable future");
        return extractFuture;
    }
}
