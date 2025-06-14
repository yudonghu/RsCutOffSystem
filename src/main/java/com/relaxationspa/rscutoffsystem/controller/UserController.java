// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/controller/UserController.java
package com.relaxationspa.rscutoffsystem.controller;

import com.relaxationspa.rscutoffsystem.dto.UserDTO;
import com.relaxationspa.rscutoffsystem.entity.User;
import com.relaxationspa.rscutoffsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 创建新用户
     */
    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> createUser(@Valid @RequestBody UserDTO.CreateUserRequest request) {
        UserDTO.UserResponse response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * 获取所有用户（分页）
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO.UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO.UserResponse> users = userService.getAllUsers(pageable);

        return ResponseEntity.ok(users);
    }

    /**
     * 根据UUID获取用户
     */
    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO.UserResponse> getUserByUuid(@PathVariable UUID uuid) {
        UserDTO.UserResponse response = userService.getUserByUuid(uuid);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据员工编号获取用户
     */
    @GetMapping("/employee/{employeeNumber}")
    public ResponseEntity<UserDTO.UserResponse> getUserByEmployeeNumber(@PathVariable Long employeeNumber) {
        UserDTO.UserResponse response = userService.getUserByEmployeeNumber(employeeNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据昵称获取用户
     */
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<UserDTO.UserResponse> getUserByNickname(@PathVariable String nickname) {
        return userService.getUserByNickname(nickname)
                .map(user -> ResponseEntity.ok(user))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取所有在职用户
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserDTO.UserResponse>> getAllActiveUsers() {
        List<UserDTO.UserResponse> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 根据角色获取用户
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO.UserResponse>> getUsersByRole(@PathVariable User.Role role) {
        List<UserDTO.UserResponse> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }

    /**
     * 根据在职状态获取用户
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserDTO.UserResponse>> getUsersByEmploymentStatus(@PathVariable User.EmploymentStatus status) {
        List<UserDTO.UserResponse> users = userService.getUsersByEmploymentStatus(status);
        return ResponseEntity.ok(users);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDTO.UserResponse> updateUser(
            @PathVariable UUID uuid,
            @Valid @RequestBody UserDTO.UpdateUserRequest request) {
        UserDTO.UserResponse response = userService.updateUser(uuid, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 软删除用户（设置为离职状态）
     */
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 彻底删除用户
     */
    @DeleteMapping("/{uuid}/hard")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable UUID uuid) {
        userService.hardDeleteUser(uuid);
        return ResponseEntity.noContent().build();
    }

    /**
     * 检查用户是否存在
     */
    @GetMapping("/{uuid}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable UUID uuid) {
        boolean exists = userService.userExists(uuid);
        return ResponseEntity.ok(exists);
    }
}