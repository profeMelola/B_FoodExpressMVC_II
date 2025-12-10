package es.daw.foodexpressmvc.service;

import es.daw.foodexpressmvc.dto.DishDTO;
import es.daw.foodexpressmvc.dto.DishResponseDTO;
import es.daw.foodexpressmvc.dto.ErrorDTO;
import es.daw.foodexpressmvc.dto.PageResponse;
import es.daw.foodexpressmvc.exception.ConnectionApiRestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;


//@Service
//@RequiredArgsConstructor
//public class DishService {
//
//    private final WebClient webClientAPI;
//
//    public List<DishResponseDTO> getAllDishes() {
//
//        DishResponseDTO[] dishes;
//        try{
//            dishes = webClientAPI.get()
//                    .uri("/dishes")
//                    .retrieve()
//                    .bodyToMono(DishResponseDTO[].class)
//                    .block();
//
//            return Arrays.asList(dishes);
//        }catch (Exception e){
//            throw new ConnectionApiRestException(e.getMessage());
//        }
//
//
//    }
//}

@Service
@RequiredArgsConstructor
public class DishService {

    private final WebClient webClientAPI;

    public PageResponse<DishResponseDTO> getAllDishes(int page, int size) {

        try {
            return webClientAPI
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/dishes")
                            .queryParam("page", page)
                            .queryParam("size", size)
                            .build()
                    )
                    .retrieve()
                    // Si el status es 4xx o 5xx, intento leer un ErrorDTO
                    /*
                    onStatus(
                    Predicate<HttpStatusCode> statusPredicate,
                    Function<ClientResponse, Mono<? extends Throwable>> exceptionFunction)
                     */

                    .onStatus(
                            httpStatus -> httpStatus.is4xxClientError() || httpStatus.is5xxServerError(),
                            clientResponse -> clientResponse.bodyToMono(ErrorDTO.class)
                                    .defaultIfEmpty(new ErrorDTO()) // por si el body viene vacío
                                    // PENDIENTE MEJORAR LA GESTIÓN DE ERRORDTO..
                                    .flatMap(errorDto -> {
                                        String msg = "Error al llamar al API /dishes: "
                                                + (errorDto.getMessage() != null ? errorDto.getMessage() : "sin detalle");
                                        return Mono.error(new ConnectionApiRestException(msg));
                                    })
                    )
                    .bodyToMono(new ParameterizedTypeReference<PageResponse<DishResponseDTO>>() {})
                    .block(); // En MVC clásico, bloqueamos aquí
        } catch (Exception e) {
            // Aquí puedes loguear o wrappear más info
            throw new ConnectionApiRestException(e.getMessage());
        }
    }
}
