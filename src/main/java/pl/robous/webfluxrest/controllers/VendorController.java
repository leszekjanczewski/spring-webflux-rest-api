package pl.robous.webfluxrest.controllers;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.robous.webfluxrest.domain.Vendor;
import pl.robous.webfluxrest.repository.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors")
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream) {
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PutMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @PatchMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundedVendor = vendorRepository.findById(id).block();

        if (!foundedVendor.getFirstName().equals(vendor.getFirstName())) {
            foundedVendor.setFirstName(vendor.getFirstName());
            vendorRepository.save(foundedVendor);
        } else if (!foundedVendor.getLastName().equals(vendor.getLastName())) {
            foundedVendor.setLastName(vendor.getLastName());
            vendorRepository.save(foundedVendor);
        }

        return Mono.just(foundedVendor);
    }
}
