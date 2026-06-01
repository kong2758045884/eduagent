# 大学生创新创业训练计划后端

第一阶段后端骨架：Spring Boot 3、MySQL、MyBatis-Plus、Spring Security、JWT、Swagger/OpenAPI。

当前已扩展为“云师道·EduAgent 引擎”的 MVP 后端，包含登录鉴权、教案生成与保存、课后反思、学情诊断、县域教研资源、课题推荐等接口。大模型调用统一放在后端，前端不需要也不应该保存模型 API Key。

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

1. 创建 MySQL 数据库和业务表：

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

4. 配置千问 API Key。不要把真实 Key 写进 `application.yml`，建议使用环境变量：

```powershell
$env:DASHSCOPE_API_KEY="你的千问 API Key"
```

后端默认使用：

- 文本模型：`qwen-plus`
- 视觉模型预留：`qwen-vl-plus`
- 语音转写模型：`qwen3-asr-flash`
- 图片/手写 OCR 模型：`qwen-vl-ocr-latest`
- OpenAI 兼容地址：`https://dashscope.aliyuncs.com/compatible-mode/v1`
- DashScope 原生多模态地址：`https://dashscope.aliyuncs.com/api/v1`

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
  -d "{\"username\":\"test\",\"password\":\"123456\",\"nickname\":\"测试用户\",\"teacherType\":\"mid\",\"county\":\"演示县\",\"school\":\"桥头小学\"}"
```

成功后返回用户基本信息，不包含 `password`。

`teacherType` 用于区分三端：

- `senior`：老教师端
- `mid`：中年/骨干教师端
- `novice`：新任教师/师范生端

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

### 个人档案

```text
GET /api/profile
PUT /api/profile
GET /api/profile/dashboard
```

`PUT /api/profile` 支持更新 `nickname/teacherType/county/school/avatarUrl/realName/phone/subject/grade/title/teachingYears/bio/expertiseTags`。`dashboard` 会返回教案、反思、诊断、资源、课题、成长事件、文件等个人工作台统计。

## 业务接口概览

以下接口均需要携带：

```text
Authorization: Bearer <token>
```

### 极简备课

```bash
curl -X POST http://localhost:8080/api/lessons/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d "{\"requirement\":\"三年级分数加减，结合分粮食的场景\",\"save\":true}"
```

返回 `contentJson`、`markdown`，当 `save=true` 时同时返回 `savedLesson`。

### 语音识别

前端录音后直接把音频上传给后端，后端保存音频并调用千问音频模型转写：

```text
POST /api/speech/transcribe
Content-Type: multipart/form-data
字段：
- file: 音频文件，建议 wav/mp3/m4a/webm，控制在约 7MB 以内
- language: 可选，默认 zh-CN
- bizType/bizId: 可选，用于绑定业务
```

返回：

```json
{
  "transcript": "下周讲三年级分数加减，要结合分粮食的场景",
  "language": "zh-CN",
  "file": { "id": 1, "publicUrl": "/uploads/speech/..." }
}
```

### 乡土教学案例库

案例库采用“首次访问空表 seed，之后查数据库”的方式，避免每次重复 mock。

```text
GET  /api/local-cases?keyword=磨盘&subject=数学&grade=六年级
POST /api/local-cases
POST /api/local-cases/seed
```

`POST /api/lessons/generate` 会自动从案例库匹配 3 条乡土案例拼入大模型提示词。

### 教案保存与查询

```text
POST /api/lessons
PUT  /api/lessons/{id}
GET  /api/lessons
```

### 课后反思

```text
POST /api/reflections
GET  /api/reflections?lessonId=1
```

### 学情诊断

```bash
curl -X POST http://localhost:8080/api/diagnoses \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d "{\"studentName\":\"王晨\",\"className\":\"六年级1班\",\"topic\":\"除法含义\",\"questionText\":\"12 ÷ 3 = 4 被写成 12 ÷ 4 = 3\",\"answerText\":\"学生混淆除数和被除数\"}"
```

当前 MVP 先接收题目/OCR 文本进行诊断；图片上传和视觉模型识别后续可接到同一模块。

图片上传诊断接口：

```text
POST /api/diagnoses/upload
Content-Type: multipart/form-data
字段：
- file: 错题图片
- studentName/className/subject/topic/questionText/answerText/imageNote: 可选补充信息
```

该接口会把图片保存到本地 `uploads/diagnosis/...`，先调用 `qwen-vl-ocr-latest` 识别题目、手写答案、批改痕迹，再把识别文本交给诊断智能体生成错因和干预策略。

诊断聚合接口：

```text
GET  /api/diagnoses/students?className=六年级1班
GET  /api/diagnoses/students/{studentName}?className=六年级1班
GET  /api/diagnoses/heatmap?className=六年级1班&days=180
POST /api/diagnoses/{id}/archive
```

### 县域教研资源

```text
POST /api/resources
GET  /api/resources?county=某县&resourceType=lesson&subject=数学&grade=三年级
POST /api/resources/{id}/like
POST /api/resources/{id}/favorite
POST /api/resources/{id}/watched
GET  /api/resources/audit/pending
POST /api/resources/{id}/review?auditStatus=approved
POST /api/resources/{id}/comments
GET  /api/resources/{id}/comments
```

资源支持 `coverUrl/mediaUrl/duration/uploader/tags/viewCount/favoriteCount` 等视频/经验库字段。新分享资源默认进入 `pending` 审核队列，审核后会给作者写入通知。

### 课题推荐

```bash
curl -X POST http://localhost:8080/api/research/topics/recommend \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d "{\"teacherGoal\":\"想做乡村数学错题研究\",\"sources\":[\"近半年鸡兔同笼错题较多\",\"学生审题时容易漏条件\"],\"save\":true}"
```

```text
POST /api/research/topics
GET  /api/research/topics
```

### 文件中心

```text
POST /api/files
GET  /api/files?bizType=diagnosis&bizId=1
```

`POST /api/files` 使用 `multipart/form-data`，字段：

- `file`：文件
- `bizType`：业务类型，可选
- `bizId`：业务 ID，可选

### 成长档案袋

```text
POST /api/growth/events
GET  /api/growth/events
GET  /api/growth/events/portfolio
POST /api/growth/events/feedback
GET  /api/growth/events/feedback?feedbackType=mentor_comment
```

系统也会在保存教案、记录反思、分享资源、评论资源、使用课堂资源包时自动写入成长事件。`feedback` 支持 `mentor_comment/student_feedback/method_case`，用于名师点评、学生反馈、教学方法转化案例。

### 经验沉淀助手

```text
GET  /api/experience/book
POST /api/experience/share
POST /api/experience/export?title=我的教学经验册
```

`book` 会按“教案 - 关联反思”聚合老教师经验资产；`share` 可把授权后的教案/心得同步到县域教研库；`export` 复用报告服务生成 PDF。

### 专家智库与科研提效

```text
GET  /api/experts?field=数学
POST /api/experts/appointments
GET  /api/experts/appointments
POST /api/experts/appointments/{id}
POST /api/reports/assessment-package
```

专家库首次查询会写入 3 条默认专家数据。预约会检查同一专家同一时间段是否冲突，并返回 `meetingUrl`；更新接口支持确认、取消、完成、回复备注。`assessment-package` 支持 `startDate/endDate/lessonIds/reflectionIds/title`，会按时间范围聚合备课、反思、课题记录生成 Word 材料。

### 本土教法库与在线答疑

```text
GET  /api/teaching-library/videos?tag=方言
POST /api/teaching-library/videos/{id}/favorite
POST /api/teaching-library/videos/{id}/watched

POST /api/qa/questions
GET  /api/qa/questions?mineOnly=false
POST /api/qa/questions/{id}/replies
POST /api/qa/questions/{id}/forward?mentorUserId=2
```

本土教法库优先返回同县域 `video/experience` 类型资源；若县域库暂无视频，会返回 3 条内置示例，方便前端先跑通体验。

### 报告导出

```text
POST /api/reports/experience-book      # 老教师经验册 PDF
POST /api/reports/assessment-package   # 中年教师考核材料 Word
POST /api/reports/growth-report        # 新任教师成长报告 PDF
```

返回值里包含文件中心记录和 `publicUrl`，可直接下载。

### 课堂辅助资源包

```text
GET  /api/classroom/assets
POST /api/classroom/assets/{assetId}/use
```

课堂资源包现在落库管理，字段包含 `version/demoUrl/offlinePackageUrl/fileSize/offlineReady/useCount`。首次空表会写入分数饼图、圆柱展开、线段图、鸡兔同笼操作板等 mock 包。

### 通知中心

```text
GET  /api/notifications?unreadOnly=false
POST /api/notifications/{id}/read
```

资源提交审核、审核结果、专家预约等流程会写通知。

### 课程体系

```text
GET  /api/courses?audience=novice
POST /api/courses/{id}/enroll
POST /api/courses/{id}/progress
```

首次空表会写入老教师、中年教师、新教师、师范生 4 类课程，每门课含 3 个 mock 课时，学习进度和反馈单独落库。

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

## 后续建议

每个业务模块按 `controller / dto / entity / mapper / service` 拆分。Controller 只处理 HTTP 入参和返回，业务逻辑放到 Service，数据库访问通过 MyBatis-Plus Mapper 完成。

下一阶段建议优先补：

- 前端 `src/api/*` 服务层，把 mock/localStorage 切到后端接口
- 教师身份字段与三端权限控制
- 直播服务、真实数字人渲染/驱动服务
- 生产环境对象存储与公网文件回源，替换本地 `uploads`
