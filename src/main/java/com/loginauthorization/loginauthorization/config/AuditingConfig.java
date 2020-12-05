package com.loginauthorization.loginauthorization.config;

import com.loginauthorization.loginauthorization.service.StudentService;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class AuditingConfig {
    public AuditorAware<Integer> auditorAware(){
        return new SimpleSecurityAuditAwareImpl();
    }
}

class SimpleSecurityAuditAwareImpl implements AuditorAware<Integer>{

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty();
        }

        StudentService studentService = (StudentService)authentication.getPrincipal();
        return  Optional.ofNullable(studentService.getId());
    }
}
