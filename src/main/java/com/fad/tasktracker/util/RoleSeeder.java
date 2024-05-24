package com.fad.tasktracker.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fad.tasktracker.entity.Role;
import com.fad.tasktracker.repositories.RoleRepository;

@Component
public class RoleSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        if (roleRepository.findByName("DEVELOPER") == null) {
            Role developerRole = new Role();
            developerRole.setName("DEVELOPER");
            developerRole.setDescription("Developer Role");
            roleRepository.save(developerRole);
        }

        if (roleRepository.findByName("MANAGER") == null) {
            Role managerRole = new Role();
            managerRole.setName("MANAGER");
            managerRole.setDescription("Manager Role");
            roleRepository.save(managerRole);
        }
    }
}
