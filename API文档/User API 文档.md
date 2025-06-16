# User API 文档

## 概述
User API 提供完整的用户（员工）管理功能，包括员工信息管理、角色权限分配和在职状态控制。

## 基础信息
- **Base URL**: `/api/users`
- **Content-Type**: `application/json`
- **支持跨域**: 是

---

## 1. 基础CRUD操作

### 1.1 创建用户
**POST** `/api/users`

#### 请求体 (CreateUserRequest)
```json
{
  "nickname": "张技师",              // Required: 用户昵称/姓名
  "gender": "MALE",                 // Required: 性别 (MALE/FEMALE)
  "birthday": "1985-03-15",         // Optional: 生日
  "phone": "13812345678",           // Optional: 手机号码 (格式: ^[1-9]\\d{10}$)
  "email": "zhang@spa.com",         // Optional: 邮箱地址
  "hireDate": "2024-01-01",         // Optional: 入职日期
  "employmentStatus": "ACTIVE",     // Optional: 在职状态 (默认ACTIVE)
  "roles": ["THERAPIST", "CASHIER"] // Optional: 角色列表
}
```

#### 性别 (Gender)
- `MALE`: 男性
- `FEMALE`: 女性

#### 在职状态 (EmploymentStatus)
- `ACTIVE`: 在职
- `INACTIVE`: 离职
- `SUSPENDED`: 停职

#### 角色 (Role)
- `ADMIN`: 管理员
- `MANAGER`: 经理
- `CASHIER`: 收银员
- `THERAPIST`: 技师
- `RECEPTIONIST`: 前台接待

#### 响应示例
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440001",
  "createdAt": "2024-01-01T09:00:00",
  "employeeNumber": 1001,
  "nickname": "张技师",
  "gender": "MALE",
  "birthday": "1985-03-15",
  "phone": "13812345678",
  "email": "zhang@spa.com",
  "hireDate": "2024-01-01",
  "employmentStatus": "ACTIVE",
  "roles": ["THERAPIST", "CASHIER"]
}
```

### 1.2 获取所有用户 (分页)
**GET** `/api/users?page=0&size=10&sortBy=createdAt&sortDir=desc`

#### 查询参数
- `page`: 页码 (默认0)
- `size`: 每页大小 (默认10)
- `sortBy`: 排序字段 (默认createdAt)
- `sortDir`: 排序方向 (asc/desc, 默认desc)

#### 响应示例
```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440001",
      "employeeNumber": 1001,
      "nickname": "张技师",
      "gender": "MALE",
      "employmentStatus": "ACTIVE",
      "roles": ["THERAPIST", "CASHIER"]
      // ... 其他字段
    }
  ],
  "totalElements": 20,
  "totalPages": 2,
  "number": 0,
  "size": 10,
  "first": true,
  "last": false
}
```

### 1.3 根据UUID获取用户
**GET** `/api/users/{uuid}`

### 1.4 根据员工编号获取用户
**GET** `/api/users/employee/{employeeNumber}`

#### 示例
```bash
GET /api/users/employee/1001
```

### 1.5 根据昵称获取用户
**GET** `/api/users/nickname/{nickname}`

#### 示例
```bash
GET /api/users/nickname/张技师
```

### 1.6 更新用户信息
**PUT** `/api/users/{uuid}`

#### 请求体 (UpdateUserRequest)
```json
{
  "nickname": "高级张技师",          // Optional: 更新昵称
  "phone": "13987654321",           // Optional: 更新手机号
  "email": "zhang.senior@spa.com",  // Optional: 更新邮箱
  "employmentStatus": "ACTIVE",     // Optional: 更新在职状态
  "roles": ["THERAPIST", "MANAGER"] // Optional: 更新角色
  // 所有字段都是可选的，只传需要更新的字段
}
```

### 1.7 软删除用户 (设置为离职状态)
**DELETE** `/api/users/{uuid}`

将用户的在职状态设置为INACTIVE

### 1.8 彻底删除用户
**DELETE** `/api/users/{uuid}/hard`

从数据库中完全删除用户记录

---

## 2. 用户查询和筛选

### 2.1 获取所有在职用户
**GET** `/api/users/active`

返回在职状态为ACTIVE的所有用户

### 2.2 根据角色获取用户
**GET** `/api/users/role/{role}`

#### 路径参数
- `role`: 用户角色

#### 示例
```bash
GET /api/users/role/THERAPIST    # 获取所有技师
GET /api/users/role/CASHIER      # 获取所有收银员
GET /api/users/role/MANAGER      # 获取所有经理
```

### 2.3 根据在职状态获取用户
**GET** `/api/users/status/{status}`

#### 路径参数
- `status`: 在职状态 (ACTIVE/INACTIVE/SUSPENDED)

#### 示例
```bash
GET /api/users/status/ACTIVE     # 获取在职员工
GET /api/users/status/INACTIVE   # 获取离职员工
GET /api/users/status/SUSPENDED  # 获取停职员工
```

---

## 3. 检查和验证

### 3.1 检查用户是否存在
**GET** `/api/users/{uuid}/exists`

#### 响应
```json
true
```

---

## 错误响应

### 400 Bad Request - 验证失败
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "昵称不能为空",
  "path": "/api/users"
}
```

### 404 Not Found - 用户不存在
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "用户不存在",
  "path": "/api/users/550e8400-e29b-41d4-a716-446655440001"
}
```

### 409 Conflict - 昵称重复
```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "昵称已存在",
  "path": "/api/users"
}
```

---

## 使用示例

### 创建员工账户
```javascript
// 创建技师账户
const therapist = await fetch('/api/users', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nickname: '李技师',
    gender: 'FEMALE',
    birthday: '1990-05-20',
    phone: '13812345678',
    email: 'li@spa.com',
    hireDate: '2024-01-01',
    employmentStatus: 'ACTIVE',
    roles: ['THERAPIST']
  })
});

const therapistData = await therapist.json();
console.log('技师创建成功:', therapistData.employeeNumber);

// 创建前台收银员账户
const cashier = await fetch('/api/users', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nickname: '王前台',
    gender: 'FEMALE',
    phone: '13987654321',
    email: 'wang@spa.com',
    hireDate: '2024-01-01',
    roles: ['RECEPTIONIST', 'CASHIER']
  })
});

// 创建经理账户
const manager = await fetch('/api/users', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    nickname: '陈经理',
    gender: 'MALE',
    phone: '13765432109',
    email: 'chen@spa.com',
    hireDate: '2024-01-01',
    roles: ['MANAGER', 'CASHIER', 'THERAPIST']
  })
});
```

### 员工角色管理
```javascript
// 获取所有技师
const therapistsResponse = await fetch('/api/users/role/THERAPIST');
const therapists = await therapistsResponse.json();

console.log('=== 技师列表 ===');
therapists.forEach(therapist => {
  console.log(`${therapist.nickname} (工号: ${therapist.employeeNumber})`);
  console.log(`角色: ${therapist.roles.join(', ')}`);
  console.log(`状态: ${therapist.employmentStatus}`);
  console.log('---');
});

// 提升技师为主管（增加经理角色）
const seniorTherapist = therapists[0];
if (seniorTherapist) {
  const updateResponse = await fetch(`/api/users/${seniorTherapist.uuid}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      nickname: '高级' + seniorTherapist.nickname,
      roles: [...seniorTherapist.roles, 'MANAGER']
    })
  });
  
  const updatedUser = await updateResponse.json();
  console.log('用户升职成功:', updatedUser.nickname, updatedUser.roles);
}
```

### 员工出勤管理
```javascript
async function getEmployeesByShift() {
  // 获取所有在职员工
  const activeResponse = await fetch('/api/users/active');
  const activeUsers = await activeResponse.json();
  
  // 按角色分组
  const shifts = {
    managers: activeUsers.filter(user => user.roles.includes('MANAGER')),
    therapists: activeUsers.filter(user => user.roles.includes('THERAPIST')),
    cashiers: activeUsers.filter(user => user.roles.includes('CASHIER')),
    receptionists: activeUsers.filter(user => user.roles.includes('RECEPTIONIST'))
  };
  
  console.log('=== 今日值班安排 ===');
  console.log(`经理: ${shifts.managers.length}人`);
  shifts.managers.forEach(user => console.log(`  - ${user.nickname}`));
  
  console.log(`技师: ${shifts.therapists.length}人`);
  shifts.therapists.forEach(user => console.log(`  - ${user.nickname}`));
  
  console.log(`收银员: ${shifts.cashiers.length}人`);
  shifts.cashiers.forEach(user => console.log(`  - ${user.nickname}`));
  
  console.log(`前台: ${shifts.receptionists.length}人`);
  shifts.receptionists.forEach(user => console.log(`  - ${user.nickname}`));
  
  return shifts;
}

const shifts = await getEmployeesByShift();
```

### 员工绩效统计
```javascript
async function getEmployeePerformance(startDate, endDate) {
  // 获取所有在职技师
  const therapistsResponse = await fetch('/api/users/role/THERAPIST');
  const therapists = await therapistsResponse.json();
  
  const performance = [];
  
  for (const therapist of therapists) {
    // 获取技师的交易记录
    const transactionsResponse = await fetch(`/api/transactions/user/${therapist.uuid}`);
    const transactions = await transactionsResponse.json();
    
    // 筛选指定日期范围的收入交易
    const periodTransactions = transactions.filter(t => 
      t.type === 'INCOME' && 
      t.transactionDate >= startDate && 
      t.transactionDate <= endDate
    );
    
    const totalAmount = periodTransactions.reduce((sum, t) => sum + t.totalAmount, 0);
    const serviceCount = periodTransactions.length;
    
    performance.push({
      therapist: therapist.nickname,
      employeeNumber: therapist.employeeNumber,
      totalAmount,
      serviceCount,
      averageAmount: serviceCount > 0 ? totalAmount / serviceCount : 0
    });
  }
  
  // 按总金额排序
  performance.sort((a, b) => b.totalAmount - a.totalAmount);
  
  console.log('=== 技师绩效排行 ===');
  performance.forEach((perf, index) => {
    console.log(`${index + 1}. ${perf.therapist} (${perf.employeeNumber})`);
    console.log(`   服务次数: ${perf.serviceCount}次`);
    console.log(`   总收入: ¥${perf.totalAmount.toFixed(2)}`);
    console.log(`   平均单价: ¥${perf.averageAmount.toFixed(2)}`);
    console.log('---');
  });
  
  return performance;
}

// 获取本月绩效
const thisMonth = new Date();
const startDate = new Date(thisMonth.getFullYear(), thisMonth.getMonth(), 1)
  .toISOString().split('T')[0];
const endDate = thisMonth.toISOString().split('T')[0];

const performance = await getEmployeePerformance(startDate, endDate);
```

### 员工生命周期管理
```javascript
// 新员工入职流程
async function onboardNewEmployee(employeeData) {
  console.log('=== 新员工入职流程 ===');
  
  // 1. 创建员工账户
  const createResponse = await fetch('/api/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      ...employeeData,
      employmentStatus: 'ACTIVE',
      hireDate: new Date().toISOString().split('T')[0]
    })
  });
  
  const newEmployee = await createResponse.json();
  console.log(`✓ 员工账户创建成功: ${newEmployee.nickname} (工号: ${newEmployee.employeeNumber})`);
  
  // 2. 验证账户创建
  const existsResponse = await fetch(`/api/users/${newEmployee.uuid}/exists`);
  const exists = await existsResponse.json();
  console.log(`✓ 账户验证: ${exists ? '成功' : '失败'}`);
  
  return newEmployee;
}

// 员工离职流程
async function offboardEmployee(employeeUuid, hardDelete = false) {
  console.log('=== 员工离职流程 ===');
  
  // 1. 获取员工信息
  const employeeResponse = await fetch(`/api/users/${employeeUuid}`);
  const employee = await employeeResponse.json();
  console.log(`处理员工: ${employee.nickname} (工号: ${employee.employeeNumber})`);
  
  // 2. 检查是否有未完成的交易
  const transactionsResponse = await fetch(`/api/transactions/user/${employeeUuid}`);
  const transactions = await transactionsResponse.json();
  const pendingTransactions = transactions.filter(t => t.status === 'PENDING');
  
  if (pendingTransactions.length > 0) {
    console.log(`警告: 员工有 ${pendingTransactions.length} 笔待处理交易`);
    // 这里可以添加处理逻辑
  }
  
  // 3. 执行删除操作
  if (hardDelete) {
    await fetch(`/api/users/${employeeUuid}/hard`, { method: 'DELETE' });
    console.log('✓ 员工记录已彻底删除');
  } else {
    await fetch(`/api/users/${employeeUuid}`, { method: 'DELETE' });
    console.log('✓ 员工状态已设置为离职');
  }
}

// 使用示例
const newEmployee = await onboardNewEmployee({
  nickname: '新技师小王',
  gender: 'MALE',
  phone: '13712345678',
  email: 'xiaowang@spa.com',
  roles: ['THERAPIST']
});

// 3个月后，如果需要离职
// await offboardEmployee(newEmployee.uuid, false);
```

### 权限管理和角色分配
```javascript
async function manageUserPermissions() {
  console.log('=== 权限管理系统 ===');
  
  // 获取所有用户
  const allUsersResponse = await fetch('/api/users?size=100');
  const usersPage = await allUsersResponse.json();
  const allUsers = usersPage.content;
  
  // 角色权限矩阵
  const rolePermissions = {
    ADMIN: ['所有权限'],
    MANAGER: ['查看报表', '管理交易', '管理员工', '管理客户'],
    CASHIER: ['处理交易', '查看客户', '日结操作'],
    THERAPIST: ['查看服务', '记录服务', '查看客户'],
    RECEPTIONIST: ['管理客户', '预约管理', '基础查询']
  };
  
  console.log('\n=== 角色权限配置 ===');
  Object.entries(rolePermissions).forEach(([role, permissions]) => {
    console.log(`${role}:`);
    permissions.forEach(permission => console.log(`  - ${permission}`));
    console.log('');
  });
  
  // 用户权限分析
  console.log('=== 用户权限分析 ===');
  allUsers.forEach(user => {
    console.log(`${user.nickname} (${user.employeeNumber}):`);
    console.log(`  角色: ${user.roles.join(', ')}`);
    console.log(`  状态: ${user.employmentStatus}`);
    
    // 汇总权限
    const userPermissions = new Set();
    user.roles.forEach(role => {
      rolePermissions[role]?.forEach(permission => userPermissions.add(permission));
    });
    
    console.log(`  权限: ${Array.from(userPermissions).join(', ')}`);
    console.log('---');
  });
}

await manageUserPermissions();
```

### 员工档案管理
```javascript
async function generateEmployeeDirectory() {
  console.log('=== 员工档案系统 ===');
  
  // 获取所有员工（包括离职）
  const allResponse = await fetch('/api/users?size=100');
  const activeResponse = await fetch('/api/users/active');
  const inactiveResponse = await fetch('/api/users/status/INACTIVE');
  
  const allUsers = (await allResponse.json()).content;
  const activeUsers = await activeResponse.json();
  const inactiveUsers = await inactiveResponse.json();
  
  console.log('=== 员工总览 ===');
  console.log(`总员工数: ${allUsers.length}`);
  console.log(`在职员工: ${activeUsers.length}`);
  console.log(`离职员工: ${inactiveUsers.length}`);
  
  // 按部门/角色统计
  const roleStats = {};
  activeUsers.forEach(user => {
    user.roles.forEach(role => {
      roleStats[role] = (roleStats[role] || 0) + 1;
    });
  });
  
  console.log('\n=== 部门人员配置 ===');
  Object.entries(roleStats).forEach(([role, count]) => {
    const roleNames = {
      ADMIN: '管理员',
      MANAGER: '经理',
      CASHIER: '收银员', 
      THERAPIST: '技师',
      RECEPTIONIST: '前台'
    };
    console.log(`${roleNames[role] || role}: ${count}人`);
  });
  
  // 生成详细档案
  console.log('\n=== 详细员工档案 ===');
  activeUsers.forEach(user => {
    const age = user.birthday ? 
      new Date().getFullYear() - new Date(user.birthday).getFullYear() : '未知';
    const workDays = user.hireDate ? 
      Math.floor((new Date() - new Date(user.hireDate)) / (1000 * 60 * 60 * 24)) : 0;
    
    console.log(`姓名: ${user.nickname}`);
    console.log(`工号: ${user.employeeNumber}`);
    console.log(`性别: ${user.gender === 'MALE' ? '男' : '女'}`);
    console.log(`年龄: ${age}岁`);
    console.log(`联系方式: ${user.phone || '未填写'}`);
    console.log(`邮箱: ${user.email || '未填写'}`);
    console.log(`入职日期: ${user.hireDate || '未知'}`);
    console.log(`工作天数: ${workDays}天`);
    console.log(`角色: ${user.roles.join(', ')}`);
    console.log(`状态: ${user.employmentStatus}`);
    console.log('========================');
  });
  
  return {
    total: allUsers.length,
    active: activeUsers.length,
    inactive: inactiveUsers.length,
    roleStats,
    directory: activeUsers
  };
}

const directory = await generateEmployeeDirectory();
```