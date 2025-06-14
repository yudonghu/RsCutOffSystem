// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/service/UserService.java
package com.relaxationspa.rscutoffsystem.service;

import com.relaxationspa.rscutoffsystem.dto.UserDTO;
import com.relaxationspa.rscutoffsystem.entity.User;
import com.relaxationspa.rscutoffsystem.exception.ResourceNotFoundException;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 创建新用户
     */
    public UserDTO.UserResponse createUser(UserDTO.CreateUserRequest request) {
        // 验证昵称唯一性
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new ValidationException("昵称已存在");
        }

        // 验证邮箱唯一性（如果提供）
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("邮箱已被使用");
        }

        // 验证电话唯一性（如果提供）
        if (request.getPhone() != null && userRepository.existsByPhone(request.getPhone())) {
            throw new ValidationException("电话号码已被使用");
        }

        // 创建用户实体
        User user = new User();
        user.setNickname(request.getNickname());
        user.setGender(request.getGender());
        user.setBirthday(request.getBirthday());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setHireDate(request.getHireDate());
        user.setEmploymentStatus(request.getEmploymentStatus());
        user.setRoles(request.getRoles());

        // 设置员工编号
        user.setEmployeeNumber(userRepository.getNextEmployeeNumber());

        User savedUser = userRepository.save(user);
        return new UserDTO.UserResponse(savedUser);
    }

    /**
     * 根据UUID获取用户
     */
    @Transactional(readOnly = true)
    public UserDTO.UserResponse getUserByUuid(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到: " + uuid));
        return new UserDTO.UserResponse(user);
    }

    /**
     * 根据员工编号获取用户
     */
    @Transactional(readOnly = true)
    public UserDTO.UserResponse getUserByEmployeeNumber(Long employeeNumber) {
        User user = userRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("员工未找到: " + employeeNumber));
        return new UserDTO.UserResponse(user);
    }

    /**
     * 获取所有用户（分页）
     */
    @Transactional(readOnly = true)
    public Page<UserDTO.UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDTO.UserResponse::new);
    }

    /**
     * 获取所有在职用户
     */
    @Transactional(readOnly = true)
    public List<UserDTO.UserResponse> getAllActiveUsers() {
        return userRepository.findAllActiveUsers().stream()
                .map(UserDTO.UserResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据角色获取用户
     */
    @Transactional(readOnly = true)
    public List<UserDTO.UserResponse> getUsersByRole(User.Role role) {
        return userRepository.findByRole(role).stream()
                .map(UserDTO.UserResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 根据在职状态获取用户
     */
    @Transactional(readOnly = true)
    public List<UserDTO.UserResponse> getUsersByEmploymentStatus(User.EmploymentStatus status) {
        return userRepository.findByEmploymentStatus(status).stream()
                .map(UserDTO.UserResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 更新用户信息
     */
    public UserDTO.UserResponse updateUser(UUID uuid, UserDTO.UpdateUserRequest request) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到: " + uuid));

        // 更新昵称（检查唯一性）
        if (request.getNickname() != null && !request.getNickname().equals(user.getNickname())) {
            if (userRepository.existsByNickname(request.getNickname())) {
                throw new ValidationException("昵称已存在");
            }
            user.setNickname(request.getNickname());
        }

        // 更新邮箱（检查唯一性）
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new ValidationException("邮箱已被使用");
            }
            user.setEmail(request.getEmail());
        }

        // 更新电话（检查唯一性）
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new ValidationException("电话号码已被使用");
            }
            user.setPhone(request.getPhone());
        }

        // 更新其他字段
        if (request.getBirthday() != null) {
            user.setBirthday(request.getBirthday());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getHireDate() != null) {
            user.setHireDate(request.getHireDate());
        }

        if (request.getEmploymentStatus() != null) {
            user.setEmploymentStatus(request.getEmploymentStatus());
        }

        if (request.getRoles() != null) {
            user.setRoles(request.getRoles());
        }

        User updatedUser = userRepository.save(user);
        return new UserDTO.UserResponse(updatedUser);
    }

    /**
     * 删除用户（软删除 - 设置为离职状态）
     */
    public void deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException("用户未找到: " + uuid));

        user.setEmploymentStatus(User.EmploymentStatus.RESIGNED);
        userRepository.save(user);
    }

    /**
     * 彻底删除用户
     */
    public void hardDeleteUser(UUID uuid) {
        if (!userRepository.existsById(uuid)) {
            throw new ResourceNotFoundException("用户未找到: " + uuid);
        }
        userRepository.deleteById(uuid);
    }

    /**
     * 根据昵称搜索用户
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO.UserResponse> getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .map(UserDTO.UserResponse::new);
    }

    /**
     * 检查用户是否存在
     */
    @Transactional(readOnly = true)
    public boolean userExists(UUID uuid) {
        return userRepository.existsById(uuid);
    }
}