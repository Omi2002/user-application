package user.com.user.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import user.com.user.dto.LoginRequest;
import user.com.user.dto.SessionUser;
import user.com.user.dto.UserDTO;
import user.com.user.exceptions.UserAuthorizationException;
import user.com.user.model.User;
import user.com.user.services.UserService;
import user.com.user.utils.Authmessages;
import user.com.user.utils.GenericResponse;
import user.com.user.utils.OAuthUtils;
import user.com.user.utils.PaginatedResponse;
import user.com.user.utils.ResponseMessages;
// import user.com.user.utils.authorization.AuthorizedToken;
import user.com.user.utils.authorization.AuthorizedToken;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author harshad
 *         Copyright Madtech Company confidential. 2025
 *
 *         This class contains controller related to user
 */
@RestController
@RequestMapping("/user")
public class UserController {

        @Autowired
        UserService userService;
        
        
        @Autowired
        AuthorizedToken authorizedToken;

        // Get/Fetch the data from database
        /**
         * 
         * @param id
         * @param token
         * @return
         */

        @GetMapping("/test")
        public ResponseEntity<String> testHeader(
                        @RequestHeader(value = "X-Custom-Header", required = false) String customHeader) {
                return ResponseEntity.ok(
                                "Received header: " + (customHeader != null ? customHeader : "Header not provided"));
        }

        @GetMapping("/{id}")
        public ResponseEntity<?> getUser(HttpServletRequest request,
                        @PathVariable("id") Integer id,
                        @RequestHeader(value = "Token") String token) {
                System.out.println("Request: " + request.toString());
                System.out.println(request.getHeader("Token"));

                Optional<User> user = userService.getUserById(id);
                GenericResponse<User> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_FETCH,
                                user.get());
                return ResponseEntity.ok(response);

        }

        // Create the data into the database
        @PostMapping("/user/")
        public ResponseEntity<?> postUser(@RequestBody User user,
                        @RequestHeader(value = "Token") String token) {

                userService.addUser(user);
                GenericResponse<User> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_CREATE, user

                );
                return ResponseEntity.ok(response);

        }

        // Delete the data from database
        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteUser(
                        @PathVariable("id") Integer id) {

                Optional<User> user = userService.getUserById(id);

                userService.deleteUser(id);
                GenericResponse<User> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_DELETE,
                                user.get());
                return ResponseEntity.ok(response);

        }

        // Update the data into database
        @PutMapping("/{id}")
        public ResponseEntity<?> updateUser(
                        @RequestBody User user,
                        @PathVariable("id") Integer id) {

                userService.updateUser(user, id);
                GenericResponse<User> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_UPDATE,
                                user);
                return ResponseEntity.ok(response);

        }

        // Get all the data or specific field which can be set from DTO's
        @GetMapping("/all")
        public ResponseEntity<?> getCustomUserList(
                        @RequestParam(required = false) String name,
                        @RequestParam(required = false) String email,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "5") int size) {

                Pageable pageable = PageRequest.of(page, size);
                GenericResponse<PaginatedResponse<UserDTO>> response = userService.getPaginatedUsers(name, email,
                                pageable);
                return ResponseEntity.ok(response);
        }

        @GetMapping("/download/excel")
        public ResponseEntity<Resource> downloadExcel() throws IOException {
                String filePath = userService.exportUsersToExcelFile();
                Path path = Paths.get(filePath);
                Resource resource = new UrlResource(path.toUri());

                String contentType = Files.probeContentType(path);
                if (contentType == null) {
                        contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }

                return ResponseEntity.ok()
                                .contentType(MediaType.parseMediaType(contentType))
                                .header(HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\"" + path.getFileName().toString() + "\"")
                                .body(resource);
        }

        @PostMapping("/register")
        public ResponseEntity<?> register(@RequestBody User user) {
                return ResponseEntity.ok(userService.registerUser(user));
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
                Optional<User> user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
                if (user.isPresent()) {
                        // iS NULL CHECK
                        HttpSession session = request.getSession(true);

                        // Create and store SessionUser instead of full User
                        SessionUser sessionUser = new SessionUser(user.get().getId(), user.get().getEmail());
                        session.setAttribute("currentUser", sessionUser);

                        GenericResponse<User> response = new GenericResponse<>(
                                        HttpStatus.OK.value(),
                                        ResponseMessages.SUCCESS_LOGIN,
                                        user.get());
                        return ResponseEntity.ok(response);
                }

                GenericResponse<String> errorResponse = new GenericResponse<>(
                                HttpStatus.UNAUTHORIZED.value(),
                                ResponseMessages.FAILURE_LOGIN, // "Invalid credentials" or custom message
                                null);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                SessionUser sessionUser = (SessionUser) (session != null ? session.getAttribute("currentUser") : null);
                if (sessionUser == null) {
                        GenericResponse<String> response = new GenericResponse<>(
                                        HttpStatus.UNAUTHORIZED.value(),
                                        "No user logged in",
                                        null);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }

                GenericResponse<User> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_FETCH,
                                null);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/logout")
        public ResponseEntity<?> logout(HttpServletRequest request) {
                HttpSession session = request.getSession(false);
                if (session != null) {
                        session.invalidate();
                }

                GenericResponse<String> response = new GenericResponse<>(
                                HttpStatus.OK.value(),
                                ResponseMessages.SUCCESS_LOGOUT,
                                null);
                return ResponseEntity.ok(response);
        }
}
