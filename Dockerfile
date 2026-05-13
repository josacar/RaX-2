# RaX-2 Build & Runtime Dockerfile
# Uses Gradle for dependency management.
# RaX-2 is a desktop GUI app — X11 forwarding is required for the GUI.
# On Wayland compositors (KDE, GNOME), this works via XWayland.

# Build stage
FROM gradle:8.12-jdk21 AS builder
WORKDIR /app

# Copy Gradle files for dependency caching
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts
COPY gradle/wrapper/ gradle/wrapper/
COPY gradlew gradlew
RUN gradle dependencies --no-daemon || true

# Copy source and build
COPY src/ /app/src/
RUN gradle build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jdk

# Install X11 libs for GUI
RUN apt-get update && apt-get install -y --no-install-recommends \
    libxext6 libxrender1 libxtst6 libxi6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /app/build/libs/RaX2-all.jar /app/RaX2.jar

CMD ["java", "-jar", "/app/RaX2.jar"]
