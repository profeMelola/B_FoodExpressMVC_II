package es.daw.foodexpressmvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@RequiredArgsConstructor
public class OrderService {

    private final WebClient webClientAPI;

}
