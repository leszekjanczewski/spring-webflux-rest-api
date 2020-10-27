package pl.robous.webfluxrest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.robous.webfluxrest.domain.Category;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
public interface CategoryRepostirory extends ReactiveMongoRepository<Category, String> {
}
