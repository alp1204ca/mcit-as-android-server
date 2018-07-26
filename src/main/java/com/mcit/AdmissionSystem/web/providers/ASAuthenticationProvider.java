package com.mcit.AdmissionSystem.web.providers;

import com.mcit.AdmissionSystem.model.User;
import com.mcit.AdmissionSystem.service.UserService;
import com.mcit.AdmissionSystem.web.filters.AsUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class ASAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String password = authentication.getCredentials().toString();

        User user = null;

        try {
            user = userService.findByUserName(authentication.getPrincipal().toString());
            if (user == null) throw new Exception("Invalid username or password");
            if (user.getRoles().stream().filter(r -> r.getCode().compareTo("ROLE_ADMIN") == 0).count() == 0)
                throw new Exception("User is not admin");
            if (!passwordEncoder.matches(password,user.getPassword()))
                throw new Exception("Invalid username or password");
            password = user.getPassword();
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        AsUserDetails asUserDetails = new AsUserDetails(
                new User(authentication.getPrincipal().toString(),password, user.getEmail(),user.getRoles()), password);
        return asUserDetails.authentication();

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
