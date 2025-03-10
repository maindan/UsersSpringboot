package com.example.users.services;

import com.example.users.DTO.User.UserCreateDTO;
import com.example.users.model.Person;
import com.example.users.model.User;
import com.example.users.repositories.PersonRepository;
import com.example.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User createUser(UserCreateDTO userData) throws Exception {
        try {
            User user = new User();
            user.setEmail(userData.email());
            user.setPassword(userData.password());
            user = userRepository.save(user);

            Person person = new Person();
            person.setName(userData.name());
            person.setPhoneNumber(userData.phoneNumber());
            person.setUser(user);
            person = personRepository.save(person);

            return user;
        } catch (Exception e) {
            throw new Exception("Erro ao criar usuário " + e.getMessage());
        }
    }
}
