package pl.robous.webfluxrest.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import pl.robous.webfluxrest.domain.Category;
import pl.robous.webfluxrest.repository.CategoryRepostirory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.never;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepostirory categoryRepostirory;
    CategoryController categoryController;

    @BeforeEach
    void setUp() {
        categoryRepostirory = Mockito.mock(CategoryRepostirory.class);
        categoryController = new CategoryController(categoryRepostirory);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    void list() {
        given(categoryRepostirory.findAll())
                .willReturn(Flux.just(Category.builder().description("Cat1").build(),
                        Category.builder().description("Cat2").build()));

        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getById() {
        given(categoryRepostirory.findById("someId"))
                .willReturn(Mono.just(Category.builder().description("Cat").build()));

        webTestClient.get()
                .uri("/api/v1/categories/someId")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    void createCategory() {
        given(categoryRepostirory.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void updateCategory() {
        given(categoryRepostirory.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().description("Some Cat").build());

        webTestClient.put()
                .uri("/api/v1/categories/sassaa")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void patchWithChanges() {
        given(categoryRepostirory.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepostirory.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().description("New Description").build());

        webTestClient.patch()
                .uri("/api/v1/categories/sassaa")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepostirory).save(any());
    }

    @Test
    void patchNoChanges() {
        given(categoryRepostirory.findById(anyString()))
                .willReturn(Mono.just(Category.builder().build()));

        given(categoryRepostirory.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/sassaa")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepostirory, never()).save(any());
    }
}