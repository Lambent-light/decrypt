# 阶段1：构建项目（使用Maven镜像，若用Gradle可替换为gradle镜像）
FROM maven:3.8.8-openjdk-17-slim AS build
# 设置工作目录
WORKDIR /app
# 复制pom.xml和依赖文件，先缓存依赖（提升构建速度）
COPY pom.xml ./
# 下载所有依赖（离线缓存）
RUN mvn dependency:go-offline -B
# 复制项目所有源码
COPY src ./src
# 打包项目（跳过测试，减少构建时间）
RUN mvn package -DskipTests
# 提取打包后的jar包（注意：这里要对应你的项目artifactId和version，可在pom.xml中查看）
RUN mkdir -p /app/target/dependency && (cd /app/target/dependency; jar -xf ../*.jar)

# 阶段2：运行项目（使用轻量的OpenJDK镜像，减小容器体积）
FROM openjdk:17-slim
# 定义环境变量，指定jar包路径
ENV SPRING_PROFILES_ACTIVE=prod
# 设置工作目录
WORKDIR /app
# 从构建阶段复制依赖和jar包
COPY --from=build /app/target/dependency ./
# 暴露项目端口（对应Spring Boot的server.port，默认8080）
EXPOSE 8080
# 启动命令（注意：路径要与上面的复制路径一致，对应BOOT-INF等目录）
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]