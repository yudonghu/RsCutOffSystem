// 文件路径: src/main/java/com/relaxationspa/rscutoffsystem/repository/UserRepository.java
package com.relaxationspa.rscutoffsystem.repository;

import com.relaxationspa.rscutoffsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // 根据员工编号查找
    Optional<User> findByEmployeeNumber(Long employeeNumber);

    // 根据昵称查找
    Optional<User> findByNickname(String nickname);

    // 根据邮箱查找
    Optional<User> findByEmail(String email);

    // 根据电话号码查找
    Optional<User> findByPhone(String phone);

    // 根据在职状态查找
    List<User> findByEmploymentStatus(User.EmploymentStatus status);

    // 查找所有在职员工
    @Query("SELECT u FROM User u WHERE u.employmentStatus = 'ACTIVE'")
    List<User> findAllActiveUsers();

    // 根据角色查找用户
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = :role")
    List<User> findByRole(@Param("role") User.Role role);

    // 根据多个角色查找用户
    @Query("SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r IN :roles")
    List<User> findByRolesIn(@Param("roles") List<User.Role> roles);

    // 检查员工编号是否存在
    boolean existsByEmployeeNumber(Long employeeNumber);

    // 检查邮箱是否存在
    boolean existsByEmail(String email);

    // 检查电话是否存在
    boolean existsByPhone(String phone);

    // 检查昵称是否存在
    boolean existsByNickname(String nickname);

    // 获取下一个员工编号
    @Query("SELECT COALESCE(MAX(u.employeeNumber), 0) + 1 FROM User u")
    Long getNextEmployeeNumber();
}