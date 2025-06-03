package user.com.user.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import java.net.URL;
import java.util.Scanner;

//mport org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import jakarta.persistence.criteria.Path;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.beans.factory.annotation.Value;

import java.nio.file.Path;

import user.com.user.dto.UserDTO;
import user.com.user.exceptions.DuplicateUserException;
import user.com.user.exceptions.InvalidUserDataException;
import user.com.user.exceptions.UserNotFoundException;
import user.com.user.model.User;
import user.com.user.repository.UserRepository;
import user.com.user.utils.Authmessages;
import user.com.user.utils.GenericResponse;
import user.com.user.utils.PaginatedResponse;
import user.com.user.utils.ResponseMessages;
import org.apache.poi.ss.usermodel.Sheet;

//Service class or Logic class
@Service
public class UserService implements UserServiceInterface {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    // private final ClientRegistration googleClientRegistration;

    // Retrive data by id
    @Override
    public Optional<User> getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException(Authmessages.USER_NOT_FOUND + " ID " + id);
        }
        return user;
    }

    // Delete the users by id
    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Cannot delete, user with ID " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    // Update the user through id
    @Override
    public User updateUser(User user, Integer id) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isEmpty()) {
            throw new UserNotFoundException(Authmessages.USER_NOT_FOUND + " ID :" + id);
        }

        // need to pass null
        if (user.getName() == null || user.getEmail() == null) {
            System.out.println("User name" + user.getName() + "UserEmail" + user.getEmail());
            throw new InvalidUserDataException(Authmessages.INVALID_USER_DATA);
        }

        User existingUser = existing.get();
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setAge(user.getAge());

        return userRepository.save(existingUser);
    }

    // To create a user
    public User addUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new DuplicateUserException(Authmessages.EMAIL_ALREADY_EXISTS);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Get filterd fields
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public GenericResponse<PaginatedResponse<UserDTO>> getPaginatedUsers(String name, String email, Pageable pageable) {
        Page<User> allUsers = userRepository.findAll(pageable);

        // Filtering after pagination
        List<User> filteredUsers = allUsers.getContent().stream()
                .filter(user -> (name == null || user.getName().equalsIgnoreCase(name)) &&
                        (email == null || user.getEmail().equalsIgnoreCase(email)))
                .toList();

        // Convert to DTO
        List<UserDTO> userDTOs = filteredUsers.stream()
                .map(user -> new UserDTO(user.getName(), user.getEmail()))
                .toList();

        PaginatedResponse<UserDTO> paginatedResponse = new PaginatedResponse<>(
                userDTOs,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                allUsers.getTotalElements(),
                allUsers.getTotalPages(),
                allUsers.isLast());
        return new GenericResponse<>(
                HttpStatus.OK.value(),
                ResponseMessages.SUCCESS_FETCH,
                paginatedResponse);
    }

    public String exportUsersToExcelFile() throws IOException {
        String filename = "users_" + System.currentTimeMillis() + ".xlsx";
        Path filePath = Paths.get("exports", filename);

        Files.createDirectories(filePath.getParent());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Users");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Name");
        header.createCell(1).setCellValue("Email");
        header.createCell(2).setCellValue("Mobile");

        List<User> users = userRepository.findAll();
        int rowNum = 1;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getName());
            row.createCell(1).setCellValue(user.getEmail());
            row.createCell(2).setCellValue(user.getMobile());
        }

        try (FileOutputStream out = new FileOutputStream(filePath.toFile())) {
            workbook.write(out);
        }
        workbook.close();

        return filePath.toAbsolutePath().toString();
    }

    // Session
    public User registerUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String rawPassword) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            System.out.println("User not found with email: " + email);
            return Optional.empty();
        }

        User user = userOpt.get();
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Stored encoded password: " + user.getPassword());

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("Password matches: " + matches);

        if (matches) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

}
