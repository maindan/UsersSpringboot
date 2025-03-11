package com.example.users.services;

import com.example.users.DTO.Person.PersonRequestDTO;
import com.example.users.DTO.User.UserRequestDTO;
import com.example.users.model.Person;
import com.example.users.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public List<PersonRequestDTO> getAll() {
        List<Person> people = personRepository.findAll();
        return this.convertPersonList(people);
    }

    public PersonRequestDTO getByUserId(long id) throws Exception {
        Optional<Person> person = personRepository.findByUser_Id(id);
        if (person.isPresent()) {
            return convertPersonDTO(person.get());
        }
        throw new Exception("Person not found");
    }

    public PersonRequestDTO getById(Long id) throws Exception {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            return convertPersonDTO(person.get());
        }
        throw new Exception("Person not found");
    }

    public void deleteById(Long id) throws Exception {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            this.personRepository.deleteById(id);
        }
        throw new Exception("Person not found");
    }

    private List<PersonRequestDTO> convertPersonList(List<Person> people) {
        return people.stream().map(person -> new PersonRequestDTO(
                person.getId(),
                person.getName(),
                person.getPhoneNumber(),
                new UserRequestDTO(person.getUser().getId(), person.getUser().getEmail(), person.getUser().getRoles()))).collect(Collectors.toList());
    }

    private PersonRequestDTO convertPersonDTO(Person person) {
        return new PersonRequestDTO(person.getId(), person.getName(), person.getPhoneNumber(), new UserRequestDTO(person.getUser().getId(), person.getUser().getEmail(), person.getUser().getRoles()));
    }
}
