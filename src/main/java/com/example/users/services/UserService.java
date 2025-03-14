package com.example.users.services;

import com.example.users.DTO.JWT.RecoveryJwtTokenDTO;
import com.example.users.DTO.User.LoginUserDTO;
import com.example.users.DTO.User.UserCreateDTO;
import com.example.users.DTO.User.UserRequestDTO;
import com.example.users.model.Person;
import com.example.users.model.Role;
import com.example.users.model.User;
import com.example.users.repositories.PersonRepository;
import com.example.users.repositories.RoleRepository;
import com.example.users.repositories.UserRepository;
import com.example.users.security.authentication.JwtTokenService;
import com.example.users.security.config.SecurityConfiguration;
import com.example.users.security.userDetail.UserDetailImplementation;
import com.example.users.utils.RoleName;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private SecurityConfiguration securityConfiguration;
    @Autowired
    RoleRepository roleRepository;

    public List<UserRequestDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        System.out.println("Usuários encontrados: " + users);
        return this.convertUserList(users);
    }

    public RecoveryJwtTokenDTO authUser(LoginUserDTO data) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserDetailImplementation userDetails = (UserDetailImplementation) authentication.getPrincipal();
        return new RecoveryJwtTokenDTO(jwtTokenService.generateToken(userDetails));
    }

    @Transactional
    public void createUser(UserCreateDTO userData) throws Exception {
        try {
            User user = new User();
            user.setEmail(userData.email());
            user.setPassword(securityConfiguration.passwordEncoder().encode(userData.password()));
            List<Role> roles = userData.roles().stream()
                    .map(roleName -> {
                        try {
                            Role role = roleRepository.findByName(RoleName.valueOf(roleName.toUpperCase()))
                                    .orElseGet(() -> {
                                        Role newRole = new Role(RoleName.valueOf(roleName.toUpperCase()));
                                        roleRepository.save(newRole);
                                        return newRole;
                                    });
                            return role;
                        } catch (IllegalArgumentException e) {
                            throw new RuntimeException("Invalid role name: " + roleName);
                        }
                    })
                    .collect(Collectors.toList());

            user.setRoles(roles);
            user = userRepository.save(user);

            Person person = new Person();
            person.setName(userData.name());
            person.setPhoneNumber(userData.phoneNumber());
            person.setUser(user);
            person = personRepository.save(person);
        } catch (Exception e) {
            throw new Exception("Erro ao criar usuário " + e.getMessage());
        }
    }

    public UserRequestDTO getUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return convertUserDTO(user.get());
        }
        throw new Exception("User not found");
    }

    public void deleteUserById(Long id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) {
            throw new Exception("User not found");
        }
        userRepository.deleteById(id);
    }

    private List<UserRequestDTO> convertUserList(List<User> users) {
        return users.stream().map(user ->
                new UserRequestDTO(user.getId(), user.getEmail(), user.getRoles())).collect(Collectors.toList());
    }

    private UserRequestDTO convertUserDTO(User user) {
        return new UserRequestDTO(user.getId(), user.getEmail(), user.getRoles());
    }
}
