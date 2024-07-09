package org.krainet.tracker.security.service;

import org.krainet.tracker.exception.custom.SameUserInDatabase;
import org.krainet.tracker.model.User;
import org.krainet.tracker.repository.UserRepository;
import org.krainet.tracker.security.model.Roles;
import org.krainet.tracker.security.model.Security;
import org.krainet.tracker.security.model.dto.AuthRequestDto;
import org.krainet.tracker.security.model.dto.RegistrationDto;
import org.krainet.tracker.security.repository.SecurityRepository;
import org.krainet.tracker.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SecurityService {
    private final SecurityRepository securityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    EmailService emailService;
    RandomPasswordGenerator randomPasswordGenerator;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils,
                           EmailService emailService, RandomPasswordGenerator randomPasswordGenerator) {
        this.securityRepository = securityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.randomPasswordGenerator = randomPasswordGenerator;
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationDto registrationDto) {
        Optional<Security> security = securityRepository.findByLogin(registrationDto.getLogin());
        if (security.isPresent()) {
            throw new SameUserInDatabase(registrationDto.getLogin());
        }
        User user = new User();
        user.setName(registrationDto.getName());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setChanged(Timestamp.valueOf(LocalDateTime.now()));
        User savedUser = userRepository.save(user);

        Security userSecurity = new Security();
        userSecurity.setLogin(registrationDto.getLogin());
        String password = randomPasswordGenerator.generatePassayPassword();
        userSecurity.setPassword(passwordEncoder.encode(password));
        userSecurity.setRole(Roles.USER);
        userSecurity.setUserId(savedUser.getId());
        securityRepository.save(userSecurity);
        emailService.sendEmailNoAttachment(userSecurity.getLogin(), emailService.getCc(),
                "Registration in tracker system", emailService.getRegistrationBody()
                        + "\n Your login: " + registrationDto.getLogin() + "\n Your password: " + password +
                        "\n Don't share to anyone this information");
    }

    public Optional<String> generateToken(AuthRequestDto authRequestDto) {
        Optional<Security> security = securityRepository.findByLogin(authRequestDto.getLogin());
        if (security.isPresent()
                && passwordEncoder.matches(authRequestDto.getPassword(), security.get().getPassword())) {
            return Optional.of(jwtUtils.generateToken(authRequestDto.getLogin()));
        }
        return Optional.empty();
    }

    public Boolean giveAdmin(Long id) {
        Optional<Security> securityOptional = securityRepository.findById(id);
        if (securityOptional.isPresent()) {
            Security security = securityOptional.get();
            security.setRole(Roles.ADMIN);
            Security savedSecurity = securityRepository.saveAndFlush(security);
            return savedSecurity.equals(security);
        }
        return false;
    }

    public Boolean downgradeAdmin(Long id) {
        Optional<Security> securityOptional = securityRepository.findById(id);
        if (securityOptional.isPresent()) {
            Security security = securityOptional.get();
            security.setRole(Roles.USER);
            Security savedSecurity = securityRepository.saveAndFlush(security);
            return savedSecurity.equals(security);
        }
        return false;
    }
}
