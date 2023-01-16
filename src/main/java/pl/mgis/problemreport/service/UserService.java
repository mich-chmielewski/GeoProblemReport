package pl.mgis.problemreport.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import pl.mgis.problemreport.exception.UserNotFoundException;
import pl.mgis.problemreport.mapper.Mapper;
import pl.mgis.problemreport.model.User;
import pl.mgis.problemreport.model.dto.UserDTO;
import pl.mgis.problemreport.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public UserDTO get(long id) {
        return Mapper.userToDto(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found!")), false);
    }

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(u -> Mapper.userToDto(u, false))
                .collect(Collectors.toList());
    }

    public UserDTO save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        var user = userRepository.save(Mapper.userFromDto(userDTO));
        return Mapper.userToDto(user, false);
    }

    @Transactional
    public UserDTO edit(UserDTO userDTO) {
        User editedUser = userRepository.findById(userDTO.getId()).orElseThrow(() -> {
            log.info("Requested for change user {} with id {} user not exist", userDTO.getUsername(), userDTO.getId());
            return new ResourceNotFoundException("Requested user not exist");
        });
        editedUser.setUsername(userDTO.getUsername());
        editedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        editedUser.setEnabled(userDTO.isEnabled());
        editedUser.setUserRole(userDTO.getUserRole());
        editedUser.setEmail(userDTO.getEmail());
        return Mapper.userToDto(editedUser, false);
    }

    public void resetPassword(String email) throws UserNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + email + " - not exist"));
        String generatedPassword = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        user.setPassword(passwordEncoder.encode(generatedPassword));
        userRepository.save(user);
        emailService.sendSimpleMessage(user.getEmail(), "Reset hasła do aplikacji", "Użytkownik: "
                + user.getUsername()
                + "\n Nowe hasło do aplikacji to:"
                + generatedPassword);
    }

    public void delete(long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.info("User with provided id not exists");
            return new ResourceNotFoundException("User with provided id not exists");
        });
        userRepository.deleteById(id);
    }

    public boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails;
    }
}
