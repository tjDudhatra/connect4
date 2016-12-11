/**
 * Copyright (C) 2016 @tdudhatra - All Rights Reserved.
 */

package com.tdudhatra.games.connect4.config;

import com.tdudhatra.games.connect4.entity.Role;
import com.tdudhatra.games.connect4.entity.User;
import com.tdudhatra.games.connect4.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of
 * org.springframework.security.core.userdetails.UserDetailsService.
 * 
 * @author tdudhatra
 */
@Service
public class Connect4UserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) {

    if (StringUtils.isEmpty(username)) {
      throw new UsernameNotFoundException("Username can not be empty.");
    }

    User user = userRepository.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("No user found with username: [ " + username + " ]");
    }

    Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
    for (Role role : user.getRoles()) {
      grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(), grantedAuthorities);
  }

  public User getLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null) {
      return null;
    }
    for (GrantedAuthority authority : auth.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_ANONYMOUS")) {
        return null;
      }
    }
    Object principal = auth.getPrincipal();
    User user = null;
    if (principal instanceof UserDetails) {
      org.springframework.security.core.userdetails.User systemUser = (org.springframework.security.core.userdetails.User) principal;
      user = userRepository.findByUsername(systemUser.getUsername());
    } else if (principal instanceof String) {
      user = userRepository.findByUsername((String) principal);
    }
    return user;
  }

  public User getLoggedInUserOrThrow() throws Exception {
    User user = this.getLoggedInUser();

    if (user == null) {
      throw new Exception("No logged in user found.");
    }
    return user;
  }

}
