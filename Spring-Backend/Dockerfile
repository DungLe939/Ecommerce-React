# ========================
# Stage 1: Build
# ========================
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper và pom.xml trước để tận dụng Docker cache
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Tải dependencies trước (layer này sẽ được cache nếu pom.xml không đổi)
RUN chmod +x mvnw && ./mvnw dependency:resolve -B

# Copy source code và build
COPY src/ src/
RUN ./mvnw clean package -DskipTests -B

# ========================
# Stage 2: Run
# ========================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Tạo user không có quyền root để chạy app (bảo mật hơn)
RUN addgroup -S spring && adduser -S spring -G spring

# Copy file JAR từ stage build
COPY --from=build /app/target/ecommerce-0.0.1-SNAPSHOT.jar app.jar

# Chuyển sang user không có quyền root
USER spring:spring

# Expose port 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
