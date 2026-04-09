# RaX-2 Build & Runtime Dockerfile
# Uses Maven for dependency management.
# RaX-2 is a desktop GUI app — X11 forwarding is required for the GUI.
# On Wayland compositors (KDE, GNOME), this works via XWayland.

# Build stage
FROM maven:3-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml for dependency caching
COPY pom.xml pom.xml
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src/ /app/src/
RUN mvn package -DskipTests -B

# Runtime stage
FROM eclipse-temurin:21-jdk

# Install X11 libs for GUI
RUN apt-get update && apt-get install -y --no-install-recommends \
    libxext6 libxrender1 libxtst6 libxi6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/target/RaX2.jar /app/RaX2.jar
COPY --from=builder /app/target/lib/ /app/lib/

CMD ["java", "-jar", "/app/RaX2.jar"]
