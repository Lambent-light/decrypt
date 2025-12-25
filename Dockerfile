# 阶段1：构建项目
FROM maven:3.8.8-openjdk-17-slim AS build
# 工作目录改为根目录，然后进入 decrypt 子目录
WORKDIR /app
COPY decrypt/pom.xml ./decrypt/
RUN cd decrypt && mvn dependency:go-offline -B
COPY decrypt/src ./decrypt/src
RUN cd decrypt && mvn package -DskipTests
RUN mkdir -p /app/decrypt/target/dependency && (cd /app/decrypt/target/dependency; jar -xf ../*.jar)

# 阶段2：运行项目
FROM openjdk:17-slim
WORKDIR /app
# 复制子目录的构建产物
COPY --from=build /app/decrypt/target/dependency ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]