package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Value("${file.upload-dir:uploads/profile_images/}")
    private String uploadDir;

    public void signUp(User user) {
        userRepository.save(user);
    }

    public Optional<User> findByUsernameAndPhoneNumber(String username, String phoneNumber) {
        return Optional.ofNullable(userRepository.findByUsernameAndPhoneNumber(username, phoneNumber));
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
    }
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    public List<User> searchUsersByUsername(String username, Long currentUserId) {
        String lowerCaseSearchTerm = username.toLowerCase();
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(lowerCaseSearchTerm)
                        && !user.getId().equals(currentUserId)) // Exclude the current user
                .map(user -> {
                    String formattedUsername = formatUsername(user.getUsername());
                    user.setUsername(formattedUsername);
                    return user;
                })
                .collect(Collectors.toList());
    }
    private String formatUsername(String username) {
        return Arrays.stream(username.split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
    public List<User> findAllUsersExceptCurrent(Long currentUserId) {
        return userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .collect(Collectors.toList());
    }

    public void updateUserRole(Long userId, String role) {
        User user = findById(userId);
        user.setRole(role);
        userRepository.save(user);
    }
    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public void uploadProfileImage(Long userId, MultipartFile profileImage) throws IOException {
        User user = findById(userId);
        String uploadDir = "C:/uploads/profile_images/"; // Suratlarni saqlash uchun statik yo'l
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        String fileName = userId + "_" + profileImage.getOriginalFilename();
        File file = new File(uploadPath, fileName);

        profileImage.transferTo(file);
        user.setProfileImageUrl("/profile_images/" + fileName);
        signUp(user);
    }
    private void validateFile(MultipartFile file) {
        List<String> allowedFileTypes = List.of("image/jpeg", "image/png", "image/jpg");
        if (!allowedFileTypes.contains(file.getContentType())) {
            throw new RuntimeException("Invalid file type. Only JPEG and PNG are allowed.");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5 MB size limit
            throw new RuntimeException("File size exceeds the limit of 5MB.");
        }
    }
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi!"));
    }
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Foydalanuvchi topilmadi!"));
    }

}
