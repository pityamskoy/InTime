package team.capybara.backend.spring.controllers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.capybara.backend.spring.entitys.User;
import team.capybara.backend.spring.controllers.repositories.UserRepository;

import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User userToCreate) {
        User newUser = new User(
                userToCreate.getId(),
                userToCreate.getName(),
                userToCreate.getEmail(),
                userToCreate.getPassword()
        );

        return userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
