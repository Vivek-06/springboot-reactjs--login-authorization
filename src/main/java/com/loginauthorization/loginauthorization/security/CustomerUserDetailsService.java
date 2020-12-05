package com.loginauthorization.loginauthorization.security;

import com.loginauthorization.loginauthorization.entity.Student;
import com.loginauthorization.loginauthorization.repository.StudentRepository;
import com.loginauthorization.loginauthorization.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    StudentRepository studentRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found at username: " + username));
        return StudentService.create(student);
    }

    public UserDetails loadUserById(Integer id){
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return StudentService.create(student);
    }
}
