package com.example.users.controller;

import com.example.users.DTO.Person.PersonRequestDTO;
import com.example.users.model.Person;
import com.example.users.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<PersonRequestDTO> getAll() {
        return personService.getAll();
    }
}
