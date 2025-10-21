package team.capybara;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import team.capybara.backend.hibernate.*;

import java.util.ArrayList;
import java.util.Date;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"team.capybara.backend.spring"})
@EntityScan(basePackages = {"team.capybara.backend.hibernate"})
@EnableJpaRepositories(basePackages = {"team.capybara.backend.spring.controllers.repositories"})
public class Main {
    static void main(String[] args) {
        SpringApplication.run(Main.class, args);
            try {
                Configuration configuration = new Configuration().configure();

                configuration.addAnnotatedClass(Product.class);
                configuration.addAnnotatedClass(ProductType.class);
                configuration.addAnnotatedClass(Image.class);
                configuration.addAnnotatedClass(Shop.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Interests.class);
                configuration.addAnnotatedClass(Favorite.class);
                configuration.addAnnotatedClass(Review.class);
                configuration.addAnnotatedClass(Category.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());

                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();

                ArrayList<Image> imgs = new ArrayList<>();
                imgs.add(new Image("image1.com"));
                imgs.add(new Image("image2.com"));

                ArrayList<Image> imgs2 = new ArrayList<>();
                imgs2.add(new Image("image3.com"));
                imgs2.add(new Image("image4.com"));

                ArrayList<Image> imgsShop = new ArrayList<>();
                imgsShop.add(new Image("image5.com"));
                imgsShop.add(new Image("image6.com"));

                Shop shop = new Shop("shop1","bakery", true,"image.ru",imgsShop,"moscow",0.5,1.5);

                ProductType productType = new ProductType("name_of_type1","desc","image.org",imgs,shop);
                ProductType productType2 = new ProductType("name_of_type2","desc2","image2.org",imgs2,shop);

                ArrayList<ProductType> productTypes = new ArrayList<>();
                productTypes.add(productType);
                productTypes.add(productType2);

                Category category = new Category("category12","something tasty1",productTypes);

                Product product1 = new Product(productType,new Date(),1000,150,false);
                Product product2 = new Product(productType,new Date(),1800,550,false);

                User usr = new User("Leo","leonid.11@gmail.com","pass123456789");
                User usr2 = new User("Andrey","andrey.12@gmail.com","pass123456789");

                Interests interests1 = new Interests(usr,product1,new Date());
                Interests interests2 = new Interests(usr,product2,new Date());
                Interests interests3 = new Interests(usr2,product2,new Date());

                Favorite favorite1 = new Favorite(usr,productType);
                Favorite favorite2 = new Favorite(usr,productType2);
                Favorite favorite3 = new Favorite(usr2,productType2);

                Review review1 = new Review(usr,shop,productType,"something",5);
                Review review2 = new Review(usr,shop,productType2,"something2",4);
                Review review3 = new Review(usr2,shop,productType,"something",1);
                Review review4 = new Review(usr2,shop,productType,"something",1);

                session.persist(shop);

                session.persist(imgs.get(0));
                session.persist(imgs.get(1));

                session.persist(imgs2.get(0));
                session.persist(imgs2.get(1));

                session.persist(imgsShop.get(0));
                session.persist(imgsShop.get(1));

                session.persist(productType);
                session.persist(productType2);

                session.persist(product1);
                session.persist(product2);

                session.persist(category);

                session.persist(usr);
                session.persist(usr2);

                session.persist(interests1);
                session.persist(interests2);
                session.persist(interests3);

                session.persist(favorite1);
                session.persist(favorite2);
                session.persist(favorite3);

                session.persist(review1);
                session.persist(review2);
                session.persist(review3);
                session.persist(review4);
                transaction.commit();
                session.close();

            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
    }
}
