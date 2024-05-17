package tqs.peticket.vet.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tqs.peticket.vet.model.User;
import tqs.peticket.vet.model.Vet;
import tqs.peticket.vet.repository.VetRepository;
import tqs.peticket.vet.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  VetRepository vetRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User Not Found with email: " + email);
    }
    if (!vetRepository.existsByUserId(user.getId())) {
      throw new UsernameNotFoundException("User Not Found with email: " + email);      
    }
    return UserDetailsImpl.build(user);
  }

}