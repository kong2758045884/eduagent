package com.innovation.training.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("云师道 EduAgent 引擎 API")
                        .version("v1")
                        .description("""
                                面向乡村数学教师的多智能体协同教学系统。
                                核心工作流：极简备课 -> 课堂辅助 -> 学情诊断 -> 教研共享 -> 成长/科研/经验沉淀。
                                用户分层：novice 新任教师、mid 中年教师、senior 老教师。
                                统一响应格式：{"code":200,"message":"success","data":...}。
                                除注册、登录、健康检查外，请在请求头携带 Authorization: Bearer <登录接口返回的 token>。
                                """))
                .tags(List.of(
                        new Tag().name("认证接口").description("注册、登录和当前用户信息。登录成功后前端保存 token，并在后续接口 Header 中传 Authorization: Bearer <token>。"),
                        new Tag().name("个人档案").description("当前教师基础资料、教师类型、县域学校和工作台统计。teacherType 决定三类教师端功能显示。"),
                        new Tag().name("教案与反思").description("极简备课、AI 生成结构化教案、保存教案、课后反思录制转写后的保存与查询。"),
                        new Tag().name("课堂辅助资源包").description("数学数字人、动态几何模型、分数饼图等课堂演示资源包，前端可缓存离线包。"),
                        new Tag().name("学情诊断").description("错题文本或图片诊断、错因归纳、干预建议、班级错题热力图和成长档案归档。"),
                        new Tag().name("县域教研资源").description("教案、反思、案例、视频等县域共享资源的发布、查询、点赞、收藏、评论和审核。"),
                        new Tag().name("乡土教学案例库").description("乡土资源与数学知识点结合的案例库，如水稻产量、家禽数量、磨盘周长等本地化案例。"),
                        new Tag().name("经验沉淀助手").description("老教师端：经验册、教学反思分享和 PDF 导出。"),
                        new Tag().name("课题研究").description("中年教师端：基于错题、反思、教研数据推荐课题，辅助生成申报材料。"),
                        new Tag().name("专家智库").description("中年教师端：专家查询、课题咨询预约、会议链接和专家回复管理。"),
                        new Tag().name("成长档案袋").description("新任教师端：成长事件、名师点评、方法转化案例和成长报告数据。"),
                        new Tag().name("本土教法库").description("新任教师端：本县优秀教师实操视频、收藏观看记录和本地名师经验。"),
                        new Tag().name("报告导出").description("经验册 PDF、考核材料 Word、成长报告 PDF 等材料导出。"),
                        new Tag().name("文件中心").description("通用文件上传与业务对象绑定。上传接口使用 multipart/form-data，文件字段名为 file。"),
                        new Tag().name("语音识别").description("语音文件上传、转写和业务对象绑定，可用于语音备课、随堂反思等场景。"),
                        new Tag().name("通知中心").description("当前用户通知查询和已读状态管理。"),
                        new Tag().name("配套课程体系").description("教师分层课程、课程报名、学习进度和反馈。")
                ))
                .components(new Components().addSecuritySchemes(SCHEME_NAME,
                        new SecurityScheme()
                                .name(SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer apifoxDocumentationCustomizer() {
        return openApi -> {
            if (openApi.getComponents() != null && openApi.getComponents().getSchemas() != null) {
                openApi.getComponents().getSchemas().forEach((schemaName, schema) -> {
                    String schemaDescription = schemaDescriptions().get(schemaName);
                    if (isBlank(schema.getDescription()) && schemaDescription != null) {
                        schema.setDescription(schemaDescription);
                    }
                    enrichSchemaProperties(schema);
                });
            }

            if (openApi.getPaths() == null) {
                return;
            }
            openApi.getPaths().forEach((path, pathItem) -> pathItem.readOperationsMap().forEach((method, operation) -> {
                boolean publicApi = isPublicApi(path, method.name());
                if (!publicApi) {
                    operation.addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME));
                }

                operation.setDescription(buildOperationDescription(path, method.name(), publicApi));

                if (operation.getParameters() != null) {
                    operation.getParameters().forEach(this::enrichParameter);
                }

                if (operation.getRequestBody() != null && isBlank(operation.getRequestBody().getDescription())) {
                    operation.getRequestBody().setDescription("请求体请按 schema 字段说明提交；JSON 接口使用 application/json，上传接口使用 multipart/form-data。");
                }

                if (operation.getResponses() != null) {
                    operation.getResponses().addApiResponse("400", new ApiResponse().description("请求参数错误，返回字段校验失败原因"));
                    if (!publicApi) {
                        operation.getResponses().addApiResponse("401", new ApiResponse().description("未登录或 Token 无效"));
                        operation.getResponses().addApiResponse("403", new ApiResponse().description("当前账号无权限访问该功能"));
                    }
                    operation.getResponses().addApiResponse("500", new ApiResponse().description("服务器内部错误"));
                }
            }));
        };
    }

    private void enrichSchemaProperties(Schema<?> schema) {
        if (schema == null || schema.getProperties() == null) {
            return;
        }
        schema.getProperties().forEach((fieldName, property) -> {
            if (property instanceof Schema<?> propertySchema) {
                applyFieldDoc(fieldName, propertySchema);
                enrichSchemaProperties(propertySchema);
            }
        });
    }

    private void enrichParameter(Parameter parameter) {
        if (parameter == null || isBlank(parameter.getName())) {
            return;
        }
        String name = parameter.getName();
        FieldDoc doc = fieldDocs().get(name);
        if (doc != null) {
            if (isBlank(parameter.getDescription())) {
                parameter.setDescription(doc.description());
            }
            if (parameter.getExample() == null) {
                parameter.setExample(doc.example());
            }
            return;
        }
        if (isBlank(parameter.getDescription())) {
            parameter.setDescription(name + " 参数");
        }
    }

    private void applyFieldDoc(String fieldName, Schema<?> schema) {
        FieldDoc doc = fieldDocs().get(fieldName);
        if (doc == null) {
            return;
        }
        if (isBlank(schema.getDescription())) {
            schema.setDescription(doc.description());
        }
        if (schema.getExample() == null) {
            schema.setExample(doc.example());
        }
    }

    private String buildOperationDescription(String path, String method, boolean publicApi) {
        List<String> lines = new ArrayList<>();
        String endpointDoc = operationDocs().get(method + " " + path);
        if (!isBlank(endpointDoc)) {
            lines.add(endpointDoc);
        }
        lines.add("响应格式固定为 Result 包装对象：code 表示业务状态码，message 为提示信息，data 为实际数据。");
        if (publicApi) {
            lines.add("该接口无需登录。");
        } else {
            lines.add("该接口需要登录，请在 Header 中传 Authorization: Bearer <token>。");
        }
        lines.add("前端联调时优先查看 requestBody/query/path 参数示例和响应 schema。");
        return String.join("\n", lines);
    }

    private boolean isPublicApi(String path, String method) {
        return ("/api/auth/register".equals(path) && "POST".equals(method))
                || ("/api/auth/login".equals(path) && "POST".equals(method))
                || ("/api/health".equals(path) && "GET".equals(method));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private Map<String, String> schemaDescriptions() {
        return Map.ofEntries(
                Map.entry("Result", "统一响应包装对象，所有接口都返回该结构"),
                Map.entry("RegisterRequest", "注册请求体"),
                Map.entry("LoginRequest", "登录请求体"),
                Map.entry("LoginResponse", "登录成功响应，包含 JWT token 和用户信息"),
                Map.entry("UserResponse", "用户资料响应"),
                Map.entry("UpdateProfileRequest", "个人资料更新请求体"),
                Map.entry("ProfileDashboardResponse", "个人工作台统计响应"),
                Map.entry("CreateLocalTeachingCaseRequest", "新增乡土教学案例请求体"),
                Map.entry("LocalTeachingCaseResponse", "乡土教学案例响应"),
                Map.entry("CreateDiagnosisRequest", "创建错题诊断请求体"),
                Map.entry("DiagnosisResponse", "错题诊断报告响应"),
                Map.entry("ArchiveDiagnosisRequest", "诊断报告归档请求体"),
                Map.entry("ClassHeatmapResponse", "班级错题热力图响应"),
                Map.entry("StudentDiagnosisSummaryResponse", "学生诊断摘要响应"),
                Map.entry("StudentDiagnosisProfileResponse", "学生诊断档案响应"),
                Map.entry("GenerateLessonRequest", "AI 生成教案请求体"),
                Map.entry("GenerateLessonResponse", "AI 生成教案响应"),
                Map.entry("SaveLessonRequest", "保存或更新教案请求体"),
                Map.entry("LessonResponse", "教案响应"),
                Map.entry("CreateReflectionRequest", "课后反思请求体"),
                Map.entry("ReflectionResponse", "课后反思响应"),
                Map.entry("CreateResourceRequest", "资源分享请求体"),
                Map.entry("ResourceResponse", "县域教研资源响应"),
                Map.entry("CreateCommentRequest", "资源评论请求体"),
                Map.entry("CommentResponse", "资源评论响应"),
                Map.entry("CreateQuestionRequest", "答疑提问请求体"),
                Map.entry("CreateReplyRequest", "答疑回复请求体"),
                Map.entry("QaQuestionResponse", "答疑问题响应"),
                Map.entry("QaReplyResponse", "答疑回复响应"),
                Map.entry("CourseResponse", "课程响应"),
                Map.entry("CourseLessonResponse", "课程课时响应"),
                Map.entry("UpdateCourseProgressRequest", "课程学习进度更新请求体"),
                Map.entry("CreateAppointmentRequest", "专家预约请求体"),
                Map.entry("UpdateAppointmentRequest", "专家预约状态更新请求体"),
                Map.entry("AppointmentResponse", "专家预约响应"),
                Map.entry("ExpertResponse", "专家信息响应"),
                Map.entry("ShareExperienceRequest", "分享教学经验请求体"),
                Map.entry("ExperienceBookResponse", "经验册汇总响应"),
                Map.entry("ExperienceItemResponse", "经验册条目响应"),
                Map.entry("CreateGrowthEventRequest", "成长事件请求体"),
                Map.entry("CreateGrowthFeedbackRequest", "成长档案点评请求体"),
                Map.entry("GrowthEventResponse", "成长事件响应"),
                Map.entry("GrowthFeedbackResponse", "成长档案点评响应"),
                Map.entry("GrowthPortfolioResponse", "成长档案统计响应"),
                Map.entry("ReportRequest", "报告导出筛选请求体"),
                Map.entry("ReportResponse", "报告导出结果响应"),
                Map.entry("RecommendTopicRequest", "AI 推荐课题请求体"),
                Map.entry("RecommendTopicResponse", "AI 推荐课题响应"),
                Map.entry("SaveTopicRequest", "保存课题请求体"),
                Map.entry("ResearchTopicResponse", "课题响应"),
                Map.entry("FileResponse", "文件信息响应"),
                Map.entry("SpeechTranscriptionResponse", "语音转写响应"),
                Map.entry("NotificationResponse", "通知响应"),
                Map.entry("ClassroomAssetResponse", "课堂辅助资源包响应")
        );
    }

    private Map<String, String> operationDocs() {
        return Map.ofEntries(
                Map.entry("POST /api/auth/register", "用户注册。前端至少提交 username、password；可同时提交 teacherType、county、school、subject、grade，用于进入老/中/新教师端。"),
                Map.entry("POST /api/auth/login", "用户登录。成功后 data.token 为 JWT，后续接口请求头使用 Authorization: Bearer <token>。"),
                Map.entry("GET /api/auth/me", "获取当前登录用户资料。前端启动后可用该接口恢复登录态和教师端类型。"),
                Map.entry("GET /api/profile", "获取当前教师个人档案。用于个人中心和根据 teacherType 控制专属功能入口。"),
                Map.entry("PUT /api/profile", "更新个人档案。teacherType 建议使用 novice/mid/senior；county、school 用于县域资源筛选和本土教法库推荐。"),
                Map.entry("GET /api/profile/dashboard", "获取个人工作台统计，包括教案、资源、成长事件等计数和最近动态。"),
                Map.entry("POST /api/lessons/generate", "极简备课接口。前端提交自然语言备课需求，后端生成结构化数学教案草稿；适用于语音转文字后的内容，也适用于手动输入。"),
                Map.entry("POST /api/lessons", "保存教案。前端可把 AI 生成结果编辑后保存；contentJson 建议保存教学目标、重难点、教学过程、乡土案例等结构化 JSON 字符串。"),
                Map.entry("PUT /api/lessons/{id}", "更新教案。用于教师微调 AI 草稿、补充本地化案例或修改课堂流程。"),
                Map.entry("GET /api/lessons", "查询当前用户的教案列表。前端可作为“我的教案”页面数据源。"),
                Map.entry("POST /api/reflections", "保存课后反思。适用于语音转写后的随堂心得，建议关联 lessonId，形成老教师经验册或成长档案素材。"),
                Map.entry("GET /api/reflections", "查询课后反思列表。可按 lessonId 过滤，展示某份教案下的心得记录。"),
                Map.entry("GET /api/classroom/assets", "获取课堂辅助资源包列表。资源包可包含数学数字人、动态几何、分数饼图、H5/WebGL 模型和离线包地址。"),
                Map.entry("POST /api/classroom/assets/{assetId}/use", "记录课堂辅助资源包使用。前端在打开演示、离线包或数字人资源后调用，用于贡献度和工作台统计。"),
                Map.entry("POST /api/diagnoses", "创建错题诊断。前端提交题目、学生答案、图片地址等，后端返回根因分析和 2-3 条干预建议。"),
                Map.entry("POST /api/diagnoses/upload", "上传错题图片并诊断。使用 multipart/form-data，file 为图片；可附带学生姓名、班级、学科、题目文本等补充信息。"),
                Map.entry("GET /api/diagnoses", "查询当前用户诊断记录。用于学情诊断历史列表。"),
                Map.entry("GET /api/diagnoses/students", "按学生聚合诊断档案。前端可展示学生错题次数、最近错因、风险等级。"),
                Map.entry("GET /api/diagnoses/students/{studentName}", "获取单个学生诊断档案。用于学生详情页，包含主题分布和历史报告。"),
                Map.entry("GET /api/diagnoses/heatmap", "生成班级错题热力图数据。前端可按 topics 和 students 渲染高频错题点、高错学生群体。"),
                Map.entry("POST /api/diagnoses/{id}/archive", "把诊断报告归档到成长档案。可提交 note 作为归档备注。"),
                Map.entry("GET /api/resources", "查询县域教研资源。支持按 county、resourceType、subject、grade 筛选，资源卡片展示标题、学校、简介、点赞收藏等。"),
                Map.entry("POST /api/resources", "分享资源到县域教研库。用于教案、反思、案例、视频等成果共享；resourceType 可用 lesson/video/experience/case。"),
                Map.entry("GET /api/resources/audit/pending", "查询待审核资源。用于教研员或管理员审核共享内容。"),
                Map.entry("POST /api/resources/{id}/like", "资源点赞。前端点击点赞后调用，返回更新后的资源对象。"),
                Map.entry("POST /api/resources/{id}/favorite", "资源收藏。用于县域库和本土教法库收藏。"),
                Map.entry("POST /api/resources/{id}/watched", "记录资源观看或浏览。用于短视频信息流、课堂资源浏览统计。"),
                Map.entry("POST /api/resources/{id}/review", "资源审核。auditStatus 建议传 approved 或 rejected。"),
                Map.entry("POST /api/resources/{id}/comments", "新增资源评论。新教师可在本土教法视频下评论或 @ 作者提问。"),
                Map.entry("GET /api/resources/{id}/comments", "查询资源评论列表。用于资源详情页评论区。"),
                Map.entry("GET /api/local-cases", "查询乡土教学案例。可按 keyword、subject、grade 筛选，供教案生成和前端案例库页面使用。"),
                Map.entry("POST /api/local-cases", "新增乡土教学案例。用于沉淀水稻产量、家禽数量、磨盘周长等本地化数学案例。"),
                Map.entry("POST /api/local-cases/seed", "初始化 mock 案例数据，仅空表时写入。开发联调用，生产环境谨慎调用。"),
                Map.entry("GET /api/experience/book", "老教师端：获取我的经验册数据，包含教案和反思的组合条目。"),
                Map.entry("POST /api/experience/share", "老教师端：把教学经验分享到县域教研库。authorized 表示教师是否授权共享。"),
                Map.entry("POST /api/experience/export", "老教师端：导出我的教学经验册 PDF。title 不传时后端使用默认标题。"),
                Map.entry("POST /api/research/topics/recommend", "中年教师端：调用 AI 推荐教研课题。可基于教师目标、错题、反思等来源生成选题和申报草稿。"),
                Map.entry("POST /api/research/topics", "中年教师端：保存课题。meta、extra、sources 可保存结构化 JSON 字符串。"),
                Map.entry("GET /api/research/topics", "中年教师端：获取当前用户课题库。"),
                Map.entry("GET /api/experts", "中年教师端：查询专家库。field 可按数学教研、课程与教学论、教育技术等领域筛选。"),
                Map.entry("POST /api/experts/appointments", "中年教师端：预约专家咨询。提交 expertId、title、question、appointmentTime 等信息。"),
                Map.entry("GET /api/experts/appointments", "中年教师端：查询我的专家咨询预约。"),
                Map.entry("POST /api/experts/appointments/{id}", "中年教师端：更新专家预约状态、回复或会议链接。status 可用 pending/confirmed/replied/cancelled。"),
                Map.entry("POST /api/reports/experience-book", "生成老教师教学经验册 PDF。request 可为空；可用日期和 lessonIds/reflectionIds 限定范围。"),
                Map.entry("POST /api/reports/assessment-package", "生成中年教师考核材料 Word。聚合备课、诊断、分享、课题等佐证材料。"),
                Map.entry("POST /api/reports/growth-report", "生成新任教师成长报告 PDF。可按服务期或日期范围导出成长节点和教研参与情况。"),
                Map.entry("POST /api/growth/events", "新任教师端：新增成长事件。可记录备课修改、学生反馈、名师点评、首次分享等节点。"),
                Map.entry("GET /api/growth/events", "新任教师端：获取成长时间线。前端按时间倒序展示成长节点。"),
                Map.entry("GET /api/growth/events/portfolio", "新任教师端：获取成长档案袋统计。用于成长档案首页。"),
                Map.entry("POST /api/growth/events/feedback", "新任教师端：新增成长档案点评、反馈或方法转化案例。"),
                Map.entry("GET /api/growth/events/feedback", "新任教师端：查询成长档案点评、反馈或方法转化案例。feedbackType 可筛选类型。"),
                Map.entry("GET /api/teaching-library/videos", "新任教师端：查询本县本土教法视频或经验。tag 可按方言教学、教具制作、课堂管理等筛选。"),
                Map.entry("POST /api/teaching-library/videos/{id}/favorite", "新任教师端：收藏本土教法资源。"),
                Map.entry("POST /api/teaching-library/videos/{id}/watched", "新任教师端：记录观看本土教法资源。"),
                Map.entry("POST /api/qa/questions", "新任教师端：发布课堂答疑问题。可关联资源、教案或诊断报告。"),
                Map.entry("GET /api/qa/questions", "查询答疑动态。mineOnly=true 时只看我的问题。"),
                Map.entry("POST /api/qa/questions/{id}/replies", "回复答疑问题。role 可表示 teacher/mentor/expert 等回复角色。"),
                Map.entry("POST /api/qa/questions/{id}/forward", "转发问题给名师。mentorUserId 不传时可由后端默认匹配。"),
                Map.entry("GET /api/courses", "查询配套课程。audience 可用 novice/mid/senior，对应新任、中年、老教师课程包。"),
                Map.entry("POST /api/courses/{id}/enroll", "报名课程。"),
                Map.entry("POST /api/courses/{id}/progress", "更新课程学习进度和反馈。progress 范围 0-100，rating 建议 1-5。"),
                Map.entry("POST /api/files", "上传通用业务文件。使用 multipart/form-data，file 为文件；bizType/bizId 用于绑定教案、诊断、资源等业务。"),
                Map.entry("GET /api/files", "查询当前用户文件。可按 bizType、bizId 过滤。"),
                Map.entry("POST /api/speech/transcribe", "上传语音音频并转写。适用于语音备课、课后反思、心得录制等场景；language 默认 zh-CN。"),
                Map.entry("GET /api/notifications", "查询当前用户通知。unreadOnly=true 时只返回未读通知。"),
                Map.entry("POST /api/notifications/{id}/read", "标记通知已读。"),
                Map.entry("GET /api/health", "健康检查。用于部署探活和前端确认后端服务是否可用。")
        );
    }

    private Map<String, FieldDoc> fieldDocs() {
        return Map.ofEntries(
                Map.entry("code", new FieldDoc("业务状态码，200 表示成功，400/401/403/500 表示失败", 200)),
                Map.entry("message", new FieldDoc("业务提示信息", "success")),
                Map.entry("data", new FieldDoc("实际业务数据，具体结构查看当前接口响应 schema", null)),
                Map.entry("id", new FieldDoc("主键 ID", 1)),
                Map.entry("userId", new FieldDoc("用户 ID", 1)),
                Map.entry("username", new FieldDoc("登录账号，建议使用手机号、工号或英文账号", "teacher001")),
                Map.entry("password", new FieldDoc("登录密码，注册时至少 6 位", "123456")),
                Map.entry("token", new FieldDoc("JWT 登录凭证，后续请求放到 Authorization 请求头", "eyJhbGciOiJIUzI1NiJ9.xxx")),
                Map.entry("tokenType", new FieldDoc("Token 类型，前端拼接请求头时使用", "Bearer")),
                Map.entry("role", new FieldDoc("账号角色，如 teacher/admin", "teacher")),
                Map.entry("nickname", new FieldDoc("用户昵称", "张老师")),
                Map.entry("teacherType", new FieldDoc("教师阶段：novice 新任教师、mid 中年教师、senior 老教师", "novice")),
                Map.entry("county", new FieldDoc("县域名称", "永宁县")),
                Map.entry("school", new FieldDoc("学校名称", "第一中学")),
                Map.entry("avatarUrl", new FieldDoc("头像 URL", "https://example.com/avatar.png")),
                Map.entry("realName", new FieldDoc("真实姓名", "张明")),
                Map.entry("phone", new FieldDoc("手机号", "13800000000")),
                Map.entry("subject", new FieldDoc("学科名称", "数学")),
                Map.entry("grade", new FieldDoc("年级", "七年级")),
                Map.entry("title", new FieldDoc("标题", "七年级数学一次函数乡土案例")),
                Map.entry("teachingYears", new FieldDoc("教龄，单位：年", 8)),
                Map.entry("bio", new FieldDoc("个人简介", "长期关注乡土资源融入数学课堂")),
                Map.entry("expertiseTags", new FieldDoc("专长标签，多个标签用英文逗号分隔", "函数教学,项目化学习")),
                Map.entry("status", new FieldDoc("状态值，含义由具体业务决定", "pending")),
                Map.entry("createdAt", new FieldDoc("创建时间，ISO-8601 格式", "2026-06-01T09:30:00")),
                Map.entry("updatedAt", new FieldDoc("更新时间，ISO-8601 格式", "2026-06-01T10:00:00")),
                Map.entry("keyword", new FieldDoc("搜索关键字，支持标题、内容等模糊匹配", "函数")),
                Map.entry("knowledgePoint", new FieldDoc("知识点", "一次函数的图像与性质")),
                Map.entry("localScenario", new FieldDoc("乡土场景或真实情境", "本地公交票价与里程关系")),
                Map.entry("resourceType", new FieldDoc("资源类型，如 lesson/video/experience/scenario", "lesson")),
                Map.entry("content", new FieldDoc("正文内容，前端可按富文本或普通文本展示", "课堂问题、活动流程和评价建议")),
                Map.entry("summary", new FieldDoc("摘要或简要说明", "围绕本地公交出行设计一次函数探究活动")),
                Map.entry("tags", new FieldDoc("标签，多个标签用英文逗号分隔", "乡土案例,一次函数")),
                Map.entry("usageCount", new FieldDoc("使用次数", 12)),
                Map.entry("studentName", new FieldDoc("学生姓名", "李华")),
                Map.entry("className", new FieldDoc("班级名称", "七年级1班")),
                Map.entry("topic", new FieldDoc("主题、课题或知识模块", "一次函数")),
                Map.entry("questionText", new FieldDoc("题目文本", "已知 y=2x+1，求 x=3 时 y 的值")),
                Map.entry("answerText", new FieldDoc("学生答案或作答内容", "y=7")),
                Map.entry("imageUrl", new FieldDoc("图片访问地址", "/uploads/diagnosis/demo.png")),
                Map.entry("imageNote", new FieldDoc("图片补充说明", "学生手写解题过程较清晰")),
                Map.entry("reportJson", new FieldDoc("AI 诊断结构化 JSON 字符串", "{\"rootCause\":\"概念混淆\"}")),
                Map.entry("rootCause", new FieldDoc("错因归纳", "变量代入步骤不熟练")),
                Map.entry("interventions", new FieldDoc("干预建议", "补充 3 道同类代入练习")),
                Map.entry("note", new FieldDoc("备注说明", "已同步到成长档案")),
                Map.entry("days", new FieldDoc("统计最近天数", 180)),
                Map.entry("totalReports", new FieldDoc("诊断报告总数", 24)),
                Map.entry("highRiskStudents", new FieldDoc("高风险学生数量", 3)),
                Map.entry("topics", new FieldDoc("主题分布列表", null)),
                Map.entry("students", new FieldDoc("学生诊断摘要列表", null)),
                Map.entry("level", new FieldDoc("风险或热度等级，如 low/medium/high", "medium")),
                Map.entry("count", new FieldDoc("数量", 5)),
                Map.entry("latestTopic", new FieldDoc("最近一次诊断主题", "分式方程")),
                Map.entry("latestRootCause", new FieldDoc("最近一次错因", "审题不完整")),
                Map.entry("latestInterventions", new FieldDoc("最近一次干预建议", "先圈画关键词再列式")),
                Map.entry("reportCount", new FieldDoc("报告数量", 4)),
                Map.entry("riskLevel", new FieldDoc("风险等级：low/medium/high", "low")),
                Map.entry("latestAt", new FieldDoc("最近更新时间", "2026-06-01T09:30:00")),
                Map.entry("requirement", new FieldDoc("生成或保存教案的需求说明", "生成一份 45 分钟的七年级数学教案")),
                Map.entry("contentJson", new FieldDoc("结构化内容 JSON 字符串，适合前端按模块渲染", "{\"objectives\":[\"理解一次函数\"]}")),
                Map.entry("lessonId", new FieldDoc("教案 ID", 1)),
                Map.entry("reflectionId", new FieldDoc("反思 ID", 1)),
                Map.entry("reflections", new FieldDoc("课后反思列表", null)),
                Map.entry("lesson", new FieldDoc("关联教案对象", null)),
                Map.entry("lessonCount", new FieldDoc("教案数量", 6)),
                Map.entry("reflectionCount", new FieldDoc("反思数量", 10)),
                Map.entry("items", new FieldDoc("条目列表", null)),
                Map.entry("category", new FieldDoc("分类", "experience")),
                Map.entry("authorized", new FieldDoc("是否确认授权分享", true)),
                Map.entry("coverUrl", new FieldDoc("封面图片 URL", "/uploads/covers/demo.png")),
                Map.entry("mediaUrl", new FieldDoc("视频、音频或附件 URL", "/uploads/resources/demo.mp4")),
                Map.entry("duration", new FieldDoc("时长，视频可用 mm:ss，课程课时可用分钟数", "12:30")),
                Map.entry("uploader", new FieldDoc("上传者显示名称", "张老师")),
                Map.entry("sourceType", new FieldDoc("来源类型，如 lesson/reflection/diagnosis/resource", "lesson")),
                Map.entry("sourceId", new FieldDoc("来源业务 ID", 1)),
                Map.entry("viewCount", new FieldDoc("浏览次数", 120)),
                Map.entry("favoriteCount", new FieldDoc("收藏次数", 18)),
                Map.entry("likes", new FieldDoc("点赞数", 30)),
                Map.entry("commentCount", new FieldDoc("评论数", 4)),
                Map.entry("auditStatus", new FieldDoc("审核状态：pending 待审、approved 通过、rejected 驳回", "approved")),
                Map.entry("mentorUserId", new FieldDoc("被转发或指定辅导的名师用户 ID，可不传", 2)),
                Map.entry("mineOnly", new FieldDoc("是否只查询我自己的数据", false)),
                Map.entry("unreadOnly", new FieldDoc("是否只查询未读通知", false)),
                Map.entry("field", new FieldDoc("专家领域筛选", "数学教研")),
                Map.entry("expertId", new FieldDoc("专家 ID", 1)),
                Map.entry("topicId", new FieldDoc("课题 ID，可不传", 1)),
                Map.entry("question", new FieldDoc("咨询问题描述", "如何把本地农业场景融入函数教学？")),
                Map.entry("appointmentTime", new FieldDoc("预约时间，ISO-8601 格式", "2026-06-05T14:30:00")),
                Map.entry("meetingUrl", new FieldDoc("线上会议链接", "https://meeting.example.com/abc")),
                Map.entry("replyNote", new FieldDoc("专家回复或处理备注", "建议先补充学生学情材料")),
                Map.entry("expert", new FieldDoc("专家信息", null)),
                Map.entry("name", new FieldDoc("姓名", "王教授")),
                Map.entry("organization", new FieldDoc("单位", "省教研院")),
                Map.entry("introduction", new FieldDoc("简介", "长期从事数学课程与教学研究")),
                Map.entry("availableSlots", new FieldDoc("可预约时间段，多个值用英文逗号分隔", "周二下午,周四上午")),
                Map.entry("eventType", new FieldDoc("成长事件类型，如 manual/lesson/reflection/diagnosis", "manual")),
                Map.entry("feedbackType", new FieldDoc("点评类型，如 feedback/case/mentor", "feedback")),
                Map.entry("startDate", new FieldDoc("开始日期，格式 yyyy-MM-dd", "2026-05-01")),
                Map.entry("endDate", new FieldDoc("结束日期，格式 yyyy-MM-dd", "2026-06-01")),
                Map.entry("lessonIds", new FieldDoc("教案 ID 列表", List.of(1, 2))),
                Map.entry("reflectionIds", new FieldDoc("反思 ID 列表", List.of(1, 2))),
                Map.entry("reportType", new FieldDoc("报告类型", "experience-book")),
                Map.entry("file", new FieldDoc("上传文件或生成文件对象；上传接口字段名固定为 file", null)),
                Map.entry("originalName", new FieldDoc("原始文件名", "lesson.docx")),
                Map.entry("contentType", new FieldDoc("文件 MIME 类型", "application/vnd.openxmlformats-officedocument.wordprocessingml.document")),
                Map.entry("publicUrl", new FieldDoc("文件公开访问地址", "/uploads/2026/06/lesson.docx")),
                Map.entry("size", new FieldDoc("文件大小，单位：字节", 204800)),
                Map.entry("bizType", new FieldDoc("业务类型，用于关联文件，如 lesson/diagnosis/resource", "lesson")),
                Map.entry("bizId", new FieldDoc("关联业务 ID", 1)),
                Map.entry("language", new FieldDoc("语音识别语言", "zh-CN")),
                Map.entry("transcript", new FieldDoc("语音转写文本", "今天我们复习一次函数的图像。")),
                Map.entry("teacherGoal", new FieldDoc("教师教研目标", "提升乡土资源融入数学课堂的能力")),
                Map.entry("sources", new FieldDoc("参考来源列表或字符串", List.of("课堂观察", "学生作业"))),
                Map.entry("save", new FieldDoc("是否把 AI 推荐结果保存为课题", true)),
                Map.entry("recommendationJson", new FieldDoc("AI 推荐结果 JSON 字符串", "{\"topics\":[\"乡土资源融入数学课堂\"]}")),
                Map.entry("savedTopic", new FieldDoc("已保存的课题对象；save=false 时可能为空", null)),
                Map.entry("meta", new FieldDoc("课题元信息 JSON 字符串", "{\"level\":\"县级\"}")),
                Map.entry("extra", new FieldDoc("课题扩展信息 JSON 字符串", "{\"keywords\":[\"乡土资源\"]}")),
                Map.entry("applicationDraft", new FieldDoc("课题申报书草稿", "一、研究背景...")),
                Map.entry("assetId", new FieldDoc("课堂资源包 ID", "asset_math_001")),
                Map.entry("assetType", new FieldDoc("资源包类型", "interactive")),
                Map.entry("description", new FieldDoc("描述说明", "包含课件、练习和离线视频")),
                Map.entry("version", new FieldDoc("版本号", "v1.0.0")),
                Map.entry("demoUrl", new FieldDoc("在线演示地址", "https://example.com/demo")),
                Map.entry("offlinePackageUrl", new FieldDoc("离线包下载地址", "/uploads/assets/math.zip")),
                Map.entry("fileSize", new FieldDoc("文件大小，单位：字节", 52428800)),
                Map.entry("offlineReady", new FieldDoc("是否支持离线使用", true)),
                Map.entry("useCount", new FieldDoc("使用次数", 20)),
                Map.entry("rating", new FieldDoc("评分，建议 1-5", 5)),
                Map.entry("feedback", new FieldDoc("反馈内容", "课程案例实用，适合本校课堂")),
                Map.entry("progress", new FieldDoc("学习进度百分比，范围 0-100", 80)),
                Map.entry("audience", new FieldDoc("课程适用教师阶段：novice/mid/senior", "novice")),
                Map.entry("hours", new FieldDoc("课程学时", 2)),
                Map.entry("enrollmentStatus", new FieldDoc("报名状态，如 not_enrolled/enrolled/completed", "enrolled")),
                Map.entry("lessons", new FieldDoc("课时列表", null)),
                Map.entry("videoUrl", new FieldDoc("视频地址", "/uploads/courses/lesson1.mp4")),
                Map.entry("sortOrder", new FieldDoc("排序号，越小越靠前", 1)),
                Map.entry("read", new FieldDoc("是否已读", false)),
                Map.entry("type", new FieldDoc("类型", "system")),
                Map.entry("counters", new FieldDoc("工作台统计键值对", Map.of("lessons", 3, "resources", 5))),
                Map.entry("recentEvents", new FieldDoc("最近成长事件列表", null))
        );
    }

    private record FieldDoc(String description, Object example) {
    }
}
