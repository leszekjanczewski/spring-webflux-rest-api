package pl.robous.webfluxrest.controllers;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.robous.webfluxrest.domain.Category;
import pl.robous.webfluxrest.repository.CategoryRepostirory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
@RestController
public class CategoryController {

    private final CategoryRepostirory categoryRepostirory;

    public CategoryController(CategoryRepostirory categoryRepostirory) {
        this.categoryRepostirory = categoryRepostirory;
    }

    @GetMapping("/api/v1/categories")
    public Flux<Category> list() {
        return categoryRepostirory.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Mono<Category> getById(@PathVariable String id) {
        return categoryRepostirory.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/categories")
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepostirory.saveAll(categoryStream).then();
    }

    @PutMapping("/api/v1/categories/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepostirory.save(category);
    }

    @PatchMapping("/api/v1/categories/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {
        Category foundCategory = categoryRepostirory.findById(id).block();

        if (foundCategory.getDescription() != category.getDescription()) {
            foundCategory.setDescription(category.getDescription());
            return categoryRepostirory.save(foundCategory);
        }
        return Mono.just(foundCategory);
    }
}
