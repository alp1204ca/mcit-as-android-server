package com.mcit.AdmissionSystem.web.filters;

import com.mcit.AdmissionSystem.model.Role;
import com.mcit.AdmissionSystem.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AsUserDetails implements UserDetails {

    private User user;

    private String password;

    private boolean credentialsNonExpired;

    public AsUserDetails(User user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuths = new ArrayList<>();
	     for(Role role : user.getRoles()) {
	        grantedAuths.add(new SimpleGrantedAuthority(role.getCode()));
	     }
        return grantedAuths;
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public Set<Role> getRoles() {
        return user.getRoles();
    }

    public boolean hasRole(String role) {
	    for(Role roleData : getRoles()) if(roleData.getCode().equals(role)) return true;

	    return false;
    }

    public Authentication authentication() {
        AsUserDetails asUserDetails = new AsUserDetails(user, password);
        return new UsernamePasswordAuthenticationToken(asUserDetails, asUserDetails.getPassword(), asUserDetails.getAuthorities());
    }

    public User getUser() {
        return this.user;
    }

    public boolean isProfessor() {
        return hasRole("ROLE_PROFESSOR");
    }

    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    public boolean isStudent() {
        return hasRole("ROLE_STUDENT");
    }

}
