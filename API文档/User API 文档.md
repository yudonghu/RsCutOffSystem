# 用户管理模块 API 文档

## API 索引

| 序号 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 1 | POST | `/api/users` | 创建用户 |
| 2 | GET | `/api/users` | 获取所有用户（分页） |
| 3 | GET | `/api/users/{uuid}` | 根据UUID获取用户 |
| 4 | GET | `/api/users/employee/{employeeNumber}` | 根据员工编号获取用户 |
| 5 | GET | `/api/users/nickname/{nickname}` | 根据昵称获取用户 |
| 6 | GET | `/api/users/active` | 获取所有在职用户 |
| 7 | GET | `/api/users/role/{role}` | 根据角色获取用户 |
| 8 | GET | `/api/users/status/{status}` | 根据在职状态获取用户 |
| 9 | PUT | `/api/users/{uuid}` | 更新用户信息 |
| 10 | DELETE | `/api/users/{uuid}` | 软删除用户 |
| 11 | DELETE | `/api/users/{uuid}/hard` | 彻底删除用户 |
| 12 | GET | `/api/users/{uuid}/exists` | 检查用户是否存在 |

## 概述

用户管理模块提供完整的员工信息管理功能，包括用户的创建、查询、更新和删除操作。支持按摩店的员工管理需求。

**基础路径**: `/api/users`

---

## 数据模型

### 用户实体 (User)

| 字段 | 类型 | 必填 | 描述 |
|------|------|------|------|
| uuid | UUID | 是 | 用户唯一标识符 |
| employeeNumber | Long | 是 | 员工编号（自动生成，唯一） |
| nickname | String | 是 | 用户昵称（唯一） |
| gender | Enum | 是 | 性别：MALE/FEMALE/OTHER |
| birthday | LocalDate | 否 | 生日 |
| phone | String | 否 | 电话号码（唯一） |
| email | String | 否 | 邮箱地址（唯一） |
| hireDate | LocalDate | 否 | 入职日期 |
| employmentStatus | Enum | 是 | 在职状态：ACTIVE/RESIGNED/ON_LEAVE |
| roles | Set<Enum> | 否 | 角色集合：MANAGER/MASSAGE_THERAPIST/RECEPTIONIST/OWNER |
| createdAt | LocalDateTime | 是 | 创建时间（自动生成） |

---

## API 接口

### 1. 创建用户

**POST** `/api/users`

创建新的用户账户。

#### 请求体

```json
{
  "nickname": "张三",
  "gender": "MALE",
  "birthday": "1990-05-15",
  "phone": "1234567890",
  "email": "zhangsan@example.com",
  "hireDate": "2024-01-15",
  "employmentStatus": "ACTIVE",
  "roles": ["MASSAGE_THERAPIST", "RECEPTIONIST"]
}
```

#### 请求字段说明

| 字段 | 类型 | 必填 | 验证规则 | 描述 |
|------|------|------|----------|------|
| nickname | String | 是 | 不能为空 | 用户昵称 |
| gender | String | 是 | MALE/FEMALE/OTHER | 性别 |
| birthday | String | 否 | 日期格式 YYYY-MM-DD | 生日 |
| phone | String | 否 | 10位数字 | 手机号码 |
| email | String | 否 | 邮箱格式 | 邮箱地址 |
| hireDate | String | 否 | 日期格式 YYYY-MM-DD | 入职日期 |
| employmentStatus | String | 否 | 默认 ACTIVE | 在职状态 |
| roles | Array | 否 | - | 角色列表 |

#### 响应示例

**成功 (201 Created)**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "employeeNumber": 10001,
  "nickname": "张三",
  "gender": "MALE",
  "birthday": "1990-05-15",
  "phone": "1234567890",
  "email": "zhangsan@example.com",
  "hireDate": "2024-01-15",
  "employmentStatus": "ACTIVE",
  "roles": ["MASSAGE_THERAPIST", "RECEPTIONIST"],
  "createdAt": "2024-01-15T10:30:00"
}
```

**错误响应**
```json
{
  "message": "昵称已存在",
  "status": 400
}
```

---

### 2. 获取所有用户（分页）

**GET** `/api/users`

分页获取所有用户列表。

#### 查询参数

| 参数 | 类型 | 默认值 | 描述 |
|------|------|--------|------|
| page | int | 0 | 页码（从0开始） |
| size | int | 10 | 每页数量 |
| sortBy | String | createdAt | 排序字段 |
| sortDir | String | desc | 排序方向（asc/desc） |

#### 请求示例

```
GET /api/users?page=0&size=20&sortBy=employeeNumber&sortDir=asc
```

#### 响应示例

```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "employeeNumber": 10001,
      "nickname": "张三",
      "gender": "MALE",
      "birthday": "1990-05-15",
      "phone": "1234567890",
      "email": "zhangsan@example.com",
      "hireDate": "2024-01-15",
      "employmentStatus": "ACTIVE",
      "roles": ["MASSAGE_THERAPIST"],
      "createdAt": "2024-01-15T10:30:00"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "numberOfElements": 1
}
```

---

### 3. 根据UUID获取用户

**GET** `/api/users/{uuid}`

根据用户UUID获取用户详细信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 用户唯一标识符 |

#### 请求示例

```
GET /api/users/550e8400-e29b-41d4-a716-446655440000
```

#### 响应示例

**成功 (200 OK)**
```json
{
  "uuid": "550e8400-e29b-41d4-a716-446655440000",
  "employeeNumber": 10001,
  "nickname": "张三",
  "gender": "MALE",
  "birthday": "1990-05-15",
  "phone": "1234567890",
  "email": "zhangsan@example.com",
  "hireDate": "2024-01-15",
  "employmentStatus": "ACTIVE",
  "roles": ["MASSAGE_THERAPIST"],
  "createdAt": "2024-01-15T10:30:00"
}
```

**用户不存在 (404 Not Found)**
```json
{
  "message": "用户未找到: 550e8400-e29b-41d4-a716-446655440000",
  "status": 404
}
```

---

### 4. 根据员工编号获取用户

**GET** `/api/users/employee/{employeeNumber}`

根据员工编号获取用户信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| employeeNumber | Long | 员工编号 |

#### 请求示例

```
GET /api/users/employee/10001
```

#### 响应示例

参考"根据UUID获取用户"的响应格式。

---

### 5. 根据昵称获取用户

**GET** `/api/users/nickname/{nickname}`

根据昵称获取用户信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| nickname | String | 用户昵称 |

#### 请求示例

```
GET /api/users/nickname/张三
```

#### 响应示例

**成功**: 参考"根据UUID获取用户"的响应格式
**用户不存在**: 返回 404 Not Found

---

### 6. 获取所有在职用户

**GET** `/api/users/active`

获取所有在职状态的用户列表。

#### 响应示例

```json
[
  {
    "uuid": "550e8400-e29b-41d4-a716-446655440000",
    "employeeNumber": 10001,
    "nickname": "张三",
    "gender": "MALE",
    "employmentStatus": "ACTIVE",
    "roles": ["MASSAGE_THERAPIST"],
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

---

### 7. 根据角色获取用户

**GET** `/api/users/role/{role}`

根据角色获取用户列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| role | String | MANAGER, MASSAGE_THERAPIST, RECEPTIONIST, OWNER | 用户角色 |

#### 请求示例

```
GET /api/users/role/MASSAGE_THERAPIST
```

#### 响应示例

返回用户数组，格式参考"获取所有在职用户"。

---

### 8. 根据在职状态获取用户

**GET** `/api/users/status/{status}`

根据在职状态获取用户列表。

#### 路径参数

| 参数 | 类型 | 可选值 | 描述 |
|------|------|--------|------|
| status | String | ACTIVE, RESIGNED, ON_LEAVE | 在职状态 |

#### 请求示例

```
GET /api/users/status/ACTIVE
```

#### 响应示例

返回用户数组，格式参考"获取所有在职用户"。

---

### 9. 更新用户信息

**PUT** `/api/users/{uuid}`

更新指定用户的信息。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 用户唯一标识符 |

#### 请求体

```json
{
  "nickname": "李四",
  "birthday": "1992-08-20",
  "phone": "0987654321",
  "email": "lisi@example.com",
  "gender": "FEMALE",
  "hireDate": "2024-02-01",
  "employmentStatus": "ACTIVE",
  "roles": ["MANAGER", "RECEPTIONIST"]
}
```

#### 请求字段说明

所有字段都是可选的，只更新提供的字段。字段验证规则与创建用户相同。

#### 响应示例

**成功 (200 OK)**

返回更新后的用户信息，格式参考"根据UUID获取用户"。

**验证失败 (400 Bad Request)**
```json
{
  "message": "昵称已存在",
  "status": 400
}
```

---

### 10. 软删除用户

**DELETE** `/api/users/{uuid}`

软删除用户（将用户状态设置为已离职）。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 用户唯一标识符 |

#### 请求示例

```
DELETE /api/users/550e8400-e29b-41d4-a716-446655440000
```

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 11. 彻底删除用户

**DELETE** `/api/users/{uuid}/hard`

彻底删除用户记录（从数据库中完全删除）。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 用户唯一标识符 |

#### 请求示例

```
DELETE /api/users/550e8400-e29b-41d4-a716-446655440000/hard
```

#### 响应示例

**成功 (204 No Content)**

无响应体。

---

### 12. 检查用户是否存在

**GET** `/api/users/{uuid}/exists`

检查指定UUID的用户是否存在。

#### 路径参数

| 参数 | 类型 | 描述 |
|------|------|------|
| uuid | UUID | 用户唯一标识符 |

#### 请求示例

```
GET /api/users/550e8400-e29b-41d4-a716-446655440000/exists
```

#### 响应示例

```json
true
```

或

```json
false
```

---

## 错误码说明

| HTTP状态码 | 错误类型 | 描述 |
|------------|----------|------|
| 400 | Bad Request | 请求参数验证失败或业务逻辑错误 |
| 404 | Not Found | 请求的资源不存在 |
| 500 | Internal Server Error | 服务器内部错误 |

## 常见错误信息

- `昵称已存在` - 创建或更新用户时昵称重复
- `邮箱已被使用` - 创建或更新用户时邮箱重复
- `电话号码已被使用` - 创建或更新用户时电话重复
- `用户未找到: {uuid}` - 指定的用户不存在
- `员工未找到: {employeeNumber}` - 指定的员工编号不存在

## 注意事项

1. **员工编号自动生成**: 员工编号由系统自动生成，是连续递增的数字
2. **唯一性约束**: 昵称、邮箱、电话号码都必须唯一
3. **软删除机制**: 默认删除操作只是将用户状态设为已离职，数据仍保留
4. **跨域支持**: API支持跨域请求
5. **数据验证**: 所有输入数据都会进行格式和业务逻辑验证