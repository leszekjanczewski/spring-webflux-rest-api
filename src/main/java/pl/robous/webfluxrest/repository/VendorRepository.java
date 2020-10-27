package pl.robous.webfluxrest.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.robous.webfluxrest.domain.Vendor;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
