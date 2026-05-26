# 大学生创新创业训练计划后端

第一阶段后端骨架：Spring Boot 3、MySQL、MyBatis-Plus、Spring Security、JWT、Swagger/OpenAPI。当前只包含注册、登录、获取当前用户、健康检查接口，方便后续继续添加业务 CRUD。

## 技术栈

- Java 17
- Spring Boot 3.5.12
- Maven
- MySQL
- MyBatis-Plus 3.5.16
- Spring Security
- JWT
- BCryptPasswordEncoder
- springdoc-openapi

## 目录结构

```text
src/main/java/com/innovation/training
├── common              # 统一返回、错误码、业务异常
├── config              # Security、CORS、OpenAPI 配置
├── exception           # 全局异常处理
├── security            # JWT 工具、JWT 过滤器、认证用户加载
└── module/user         # 用户模块：controller、dto、entity、mapper、service
```

## 数据库初始化

1. 创建 MySQL 数据库和用户表：

```bash
mysql -u root -p < sql/init.sql
```

PowerShell 可使用：

```powershell
Get-Content .\sql\init.sql | mysql -u root -p
```

2. 修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/training_backend?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
```

3. 开发环境可直接使用示例 `jwt.secret`。生产环境必须换成更安全的密钥，并避免提交到代码仓库。

## 启动项目

```bash
mvn spring-boot:run
```

默认端口：`8080`

Swagger 页面：

```text
http://localhost:8080/swagger-ui.html
```

健康检查：

```bash
curl http://localhost:8080/api/health
```

## 接口测试

### 注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"test\",\"password\":\"123456\",\"nickname\":\"测试用户\"}"
```

成功后返回用户基本信息，不包含 `password`。

### 登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"test\",\"password\":\"123456\"}"
```

返回示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJ...",
    "tokenType": "Bearer",
    "user": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "role": "USER",
      "status": 1
    }
  }
}
```

### 获取当前用户

将登录接口返回的 token 填入 Authorization 请求头：

```bash
curl http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer <token>"
```

不携带 token 访问 `/api/auth/me` 会返回 HTTP 401，响应体格式仍为统一结果：

```json
{
  "code": 401,
  "message": "未登录或 Token 无效",
  "data": null
}
```

## 统一返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

常见业务码：

- `200`：成功
- `400`：请求参数错误
- `401`：未登录或 Token 无效
- `403`：无权限
- `500`：服务器内部错误

## 后续添加 CRUD 的建议

每个业务模块按 `controller / dto / entity / mapper / service` 拆分。Controller 只处理 HTTP 入参和返回，业务逻辑放到 Service，数据库访问通过 MyBatis-Plus Mapper 完成。
