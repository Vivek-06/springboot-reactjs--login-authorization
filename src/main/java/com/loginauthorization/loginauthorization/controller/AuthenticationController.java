package com.loginauthorization.loginauthorization.controller;

import com.loginauthorization.loginauthorization.entity.Role;
import com.loginauthorization.loginauthorization.entity.RoleName;
import com.loginauthorization.loginauthorization.entity.Student;
import com.loginauthorization.loginauthorization.exception.AppException;
import com.loginauthorization.loginauthorization.payloads.ApiResponse;
import com.loginauthorization.loginauthorization.payloads.JwtAuthenticationResponse;
import com.loginauthorization.loginauthorization.payloads.LoginRequest;
import com.loginauthorization.loginauthorization.payloads.SignUpRequest;
import com.loginauthorization.loginauthorization.repository.RoleRepository;
import com.loginauthorization.loginauthorization.repository.StudentRepository;
import com.loginauthorization.loginauthorization.security.JwtTokenProvider;
import com.loginauthorization.loginauthorization.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        StudentService studentService = (StudentService) authentication.getPrincipal();
        System.out.println(jwt);
        List<String> roles = studentService.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());
        System.out.println("Roles" + roles);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,
                    studentService.getId(), studentService.getUsername(), studentService.getEmail(),
                roles
                ));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        if(studentRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity<>(new ApiResponse("Email Address already taken"), HttpStatus.BAD_REQUEST);
        }
        Student student = new Student(signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        Role userRole = (Role) roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new AppException("User Role not Set"));
        student.setRoles(Collections.singleton(userRole));
        Student result = studentRepository.save(student);
        URI locationUri = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(locationUri)
                .body(new ApiResponse("User registered Successfully"));
    }
}
