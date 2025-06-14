// 文件路径: src/test/java/com/relaxationspa/rscutoffsystem/service/UserServiceTest.java

import com.relaxationspa.rscutoffsystem.dto.UserDTO;
import com.relaxationspa.rscutoffsystem.entity.User;
import com.relaxationspa.rscutoffsystem.exception.ValidationException;
import com.relaxationspa.rscutoffsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testCreateUser() {
        // 准备测试数据
        UserDTO.CreateUserRequest request = new UserDTO.CreateUserRequest();
        request.setNickname("测试用户");
        request.setGender(User.Gender.MALE);
        request.setEmail("test@example.com");
        request.setPhone("13812345678");
        request.setBirthday(LocalDate.of(1990, 1, 1));
        request.setHireDate(LocalDate.now());
        request.setRoles(Set.of(User.Role.TECHNICIAN));

        // 执行测试
        UserDTO.UserResponse response = userService.createUser(request);

        // 验证结果
        assertNotNull(response);
        assertNotNull(response.getUuid());
        assertNotNull(response.getEmployeeNumber());
        assertEquals("测试用户", response.getNickname());
        assertEquals(User.Gender.MALE, response.getGender());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("13812345678", response.getPhone());
        assertEquals(User.EmploymentStatus.ACTIVE, response.getEmploymentStatus());
        assertTrue(response.getRoles().contains(User.Role.TECHNICIAN));
    }

    @Test
    public void testCreateUserWithDuplicateNickname() {
        // 创建第一个用户
        UserDTO.CreateUserRequest request1 = new UserDTO.CreateUserRequest();
        request1.setNickname("重复昵称");
        request1.setGender(User.Gender.FEMALE);
        userService.createUser(request1);

        // 尝试创建具有相同昵称的第二个用户
        UserDTO.CreateUserRequest request2 = new UserDTO.CreateUserRequest();
        request2.setNickname("重复昵称");
        request2.setGender(User.Gender.MALE);

        // 验证抛出异常
        assertThrows(ValidationException.class, () -> {
            userService.createUser(request2);
        });
    }

    @Test
    public void testGetUserByEmployeeNumber() {
        // 创建用户
        UserDTO.CreateUserRequest request = new UserDTO.CreateUserRequest();
        request.setNickname("员工测试");
        request.setGender(User.Gender.OTHER);
        UserDTO.UserResponse createdUser = userService.createUser(request);

        // 根据员工编号查找用户
        UserDTO.UserResponse foundUser = userService.getUserByEmployeeNumber(createdUser.getEmployeeNumber());

        // 验证结果
        assertNotNull(foundUser);
        assertEquals(createdUser.getUuid(), foundUser.getUuid());
        assertEquals(createdUser.getNickname(), foundUser.getNickname());
    }

    @Test
    public void testUpdateUser() {
        // 创建用户
        UserDTO.CreateUserRequest createRequest = new UserDTO.CreateUserRequest();
        createRequest.setNickname("更新测试");
        createRequest.setGender(User.Gender.MALE);
        UserDTO.UserResponse createdUser = userService.createUser(createRequest);

        // 更新用户信息
        UserDTO.UpdateUserRequest updateRequest = new UserDTO.UpdateUserRequest();
        updateRequest.setNickname("更新后昵称");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setEmploymentStatus(User.EmploymentStatus.ON_LEAVE);

        UserDTO.UserResponse updatedUser = userService.updateUser(createdUser.getUuid(), updateRequest);

        // 验证结果
        assertEquals("更新后昵称", updatedUser.getNickname());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals(User.EmploymentStatus.ON_LEAVE, updatedUser.getEmploymentStatus());
    }

    @Test
    public void testDeleteUser() {
        // 创建用户
        UserDTO.CreateUserRequest request = new UserDTO.CreateUserRequest();
        request.setNickname("删除测试");
        request.setGender(User.Gender.FEMALE);
        UserDTO.UserResponse createdUser = userService.createUser(request);

        // 软删除用户
        userService.deleteUser(createdUser.getUuid());

        // 验证用户状态变为离职
        UserDTO.UserResponse deletedUser = userService.getUserByUuid(createdUser.getUuid());
        assertEquals(User.EmploymentStatus.RESIGNED, deletedUser.getEmploymentStatus());
    }
}