package uz.ovir.ovir_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.exceptions.UniversalException;
import uz.ovir.ovir_project.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUserName = userRepository.findByEmailOrUserName(username);
        if (byUserName.isPresent())
            return byUserName.get();
        else throw new UniversalException("Not Found User", HttpStatus.NOT_FOUND);
    }
}
