# 使用官方的 OpenJDK 21 作为基础镜像
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 JAR 文件到容器中
COPY target/*.jar app.jar

# 暴露应用程序端口
EXPOSE 8080

# 设置 JVM 参数并启动应用程序
ENTRYPOINT ["java", "-jar", "/app/app.jar"] 