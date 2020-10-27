package pl.robous.webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.robous.webfluxrest.domain.Vendor;
import pl.robous.webfluxrest.repository.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;
import static reactor.core.publisher.Mono.just;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Jim").lastName("Joe").build(),
                        Vendor.builder().firstName("Stiv").lastName("Black").build()));

        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(vendorRepository.findById("someId"))
                .willReturn(just(Vendor.builder().firstName("Jim").lastName("Joe").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someId")
                .exchange()
                .expectBody(Vendor.class);
    }

    @Test
    void create() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendorToSaveMono = just(Vendor.builder().firstName("Jim").lastName("Black").build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(vendorToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void update() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = just(Vendor.builder().firstName("Jim").lastName("Black").build());

        webTestClient.put()
                .uri("/api/v1/vendors/assas")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchWithChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(just(Vendor.builder().firstName("First").lastName("Last").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = just(Vendor.builder().firstName("New").lastName("Name").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/assas")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    void patchWithNoChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(just(Vendor.builder().firstName("First").lastName("Last").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = just(Vendor.builder().firstName("First").lastName("Last").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/assas")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }

}