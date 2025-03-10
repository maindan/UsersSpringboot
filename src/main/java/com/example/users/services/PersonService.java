package com.example.users.services;

import com.example.users.DTO.Person.PersonRequestDTO;
import com.example.users.DTO.User.UserRequestDTO;
import com.example.users.model.Person;
import com.example.users.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public List<PersonRequestDTO> getAll() {
        List<Person> people = personRepository.findAll();
        return people.stream().map(person ->
                new PersonRequestDTO(
                        person.getId(),
                        person.getName(),
                        person.getPhoneNumber(),
                        new UserRequestDTO(person.getUser().getId(), person.getUser().getEmail())
                )
        ).collect(Collectors.toList());
    }
}
