package pl.robous.webfluxrest.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.robous.webfluxrest.domain.Category;
import pl.robous.webfluxrest.domain.Vendor;
import pl.robous.webfluxrest.repository.CategoryRepostirory;
import pl.robous.webfluxrest.repository.VendorRepository;

/**
 * @author Leszek Janczewski on 2020.10.27
 * @project webflux-rest
 */
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepostirory categoryRepostirory;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepostirory categoryRepostirory, VendorRepository vendorRepository) {
        this.categoryRepostirory = categoryRepostirory;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepostirory.count().block() == 0) {
            // load data
            System.out.println("#### LOADING DATA ####");

            categoryRepostirory.save(Category.builder().description("Fruits").build()).block();
            categoryRepostirory.save(Category.builder().description("Nuts").build()).block();
            categoryRepostirory.save(Category.builder().description("Breads").build()).block();
            categoryRepostirory.save(Category.builder().description("Meats").build()).block();
            categoryRepostirory.save(Category.builder().description("Eggs").build()).block();

            System.out.println("Loaded categories: " + categoryRepostirory.count().block());

            vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Bucks").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Michel").lastName("Wetson").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Jessi").lastName("Wendor").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Frank").lastName("Mars").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Peet").lastName("Dark").build()).block();

            System.out.println("Loaded vendors: " + vendorRepository.count().block());
        }
    }
}
