# 1. System Requirements
使用基于 Java 的技术栈，包括 Spring Boot、MySQL、Lombok 等。
后端数据库选型为 MSSQL，部署在 Azure 云平台上。

部署方式：容器化部署，需要执行以下步骤：
1. 构建 Docker 镜像。
2. 在 Azure 上创建容器实例。
3. 配置数据库连接字符串。

需要预留仓库地址，以供以后配置。

# 2. Database Schema
参考 FunctionSpec.md 中的数据库表结构。
Java 的数据库中间件选型为 MyBatis-Plus，用于简化数据库操作。

# 3. API Endpoints
参考 FunctionSpec.md 中的 API 文档。

# 4. 实现细节
1. 定时任务可以使用 Spring Boot 提供的 `@Scheduled` 注解来实现。运行时间则定义在数据库表中，每次运行时从数据库中读取。
2. 数据库连接字符串需要在应用配置文件中配置，避免硬编码。
3. 定时任务应根据配置项来触发，类似 feature flag 一样，默认开启。

# 5. 数据处理流程
1. 定时任务触发后，从数据库中读取配置项，判断是否开启。
2. 如果开启，从 SFTP 服务器中下载最新的 CSV 文件。SFTP 服务器地址存储在 docker 的 configmap 配置项中。
3. 解析 CSV 文件，将数据转换为 Java 对象。
4. 对每个对象进行校验，确保数据符合要求。
5. 校验通过后，将数据批量插入到 `reward_transactions` 表中。
6. 插入完成后，更新 `last_processed_date` 字段，记录最后处理的日期。
7. 如果处理过程中出错，则回滚这一条事务，记录错误信息到 operator_log 表中。
8. 文件处理完成后，将其移动到 backup 目录，避免重复处理。

# 6. 周报/月报发送流程
1. 每周一、月底自动触发。
2. 从 `reward_transactions` 表中查询本周/本月的奖励数据。
3. 生成 CSV 报告，包含奖励数据、统计信息等。
4. 将 CSV 报告上传到 SFTP 服务器。