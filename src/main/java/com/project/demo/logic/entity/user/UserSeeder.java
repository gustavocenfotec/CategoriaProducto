package com.project.demo.logic.entity.user;


import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createUserNormal();
    }

    private void createUserNormal() {
        User userNormal = new User();
        userNormal.setName("Usuario");
        userNormal.setLastname("Normal");
        userNormal.setEmail("normal.user@gmail.com");
        userNormal.setPassword("userNormal123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(userNormal.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(userNormal.getName());
        user.setLastname(userNormal.getLastname());
        user.setEmail(userNormal.getEmail());
        user.setPassword(passwordEncoder.encode(userNormal.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }

}
