package com.sustainabilitytracker.sustainabilitytracker.services;

import com.sustainabilitytracker.sustainabilitytracker.dtos.request.ChangePasswordRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.request.RegisterUserRequest;
import com.sustainabilitytracker.sustainabilitytracker.dtos.response.UserResponse;
import com.sustainabilitytracker.sustainabilitytracker.entities.Department;
import com.sustainabilitytracker.sustainabilitytracker.entities.User;
import com.sustainabilitytracker.sustainabilitytracker.enums.Role;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.BadRequestException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.DuplicateResourceException;
import com.sustainabilitytracker.sustainabilitytracker.exceptions.ResourceNotFoundException;
import com.sustainabilitytracker.sustainabilitytracker.mappers.UserMapper;
import com.sustainabilitytracker.sustainabilitytracker.repositories.DepartmentRepository;
import com.sustainabilitytracker.sustainabilitytracker.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }

        User user = userMapper.toEntity(request);

        Role role = request.getRole();

        if (role == Role.DEPT_MANAGER || role == Role.EMPLOYEE) {

            if (request.getDepartmentId() == null) {
                throw new BadRequestException("Department is required for DEPT_MANAGER and EMPLOYEE roles");
            }

            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Department with id " + request.getDepartmentId() + " does not exist"));

            user.assignToDepartment(department);
            user.setCompany(department.getCompany());
        }
        else if (role == Role.ADMIN) {
            if (request.getCompanyId() != null || request.getDepartmentId() != null) {
                throw new BadRequestException("ADMIN should not have companyId or departmentId");
            }
        }
        else {
            throw new BadRequestException("Invalid role");
        }

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public User getCurrentUser(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId =(Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found please login first."));
    }

    public void changePassword(ChangePasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(
                request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is wrong");
        }

        if (passwordEncoder.matches(
                request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password must be different");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        String hashedNewPassword = passwordEncoder.encode(request.getNewPassword());

        user.setPassword(hashedNewPassword);

        user.setIsFirstLogin(false);

        userRepository.save(user);
    }

}
