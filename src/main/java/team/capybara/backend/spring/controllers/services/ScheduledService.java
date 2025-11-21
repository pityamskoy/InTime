package team.capybara.backend.spring.controllers.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.controllers.repositories.InterestRepository;
import team.capybara.backend.spring.controllers.repositories.ProductRepository;
import team.capybara.backend.spring.entities.Interest;
import team.capybara.backend.spring.entities.Product;

import java.util.Date;
import java.util.List;

@Service
public class ScheduledService{
    private final ProductRepository productRepository;
    private final InterestRepository interestRepository;

    public ScheduledService(

            ProductRepository productRepository, InterestRepository interestRepository) {

        this.productRepository = productRepository;
        this.interestRepository = interestRepository;
        new Thread(this::clean).start();
    }

    private void clean(){
        long nowTime = new Date().getTime();
        List<Product>products = productRepository.findAll();
        for(Product el:products){
            if(el.getShelfLife().getTime()<nowTime){
                List<Interest>interests = interestRepository.findByProduct(el);
                for(Interest interest:interests){
                    interestRepository.delete(interest);
                }
                productRepository.delete(el);
                System.out.println("Delete product with id="+el.getId().toString());
            }
        }
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void delete_expired_products() {
        new Thread(this::clean).start();
    }
}