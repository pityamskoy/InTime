package team.capybara.backend.spring.controllers.repositories;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import team.capybara.backend.spring.entities.User;

import java.util.UUID;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class)
@SuppressWarnings(value = {"unused"})
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_Save_ReturnSavedUser() {
        User user = User.builder().id(UUID.randomUUID()).name("Leonid").email("mai@mail.ru").
                phoneNumber("+79998887777").password(".").isShopOwner(false).build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isEqualTo(user.getId());
        Assertions.assertThat(savedUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(savedUser.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
        Assertions.assertThat(savedUser.getIsShopOwner()).isEqualTo(user.getIsShopOwner());
    }
}
