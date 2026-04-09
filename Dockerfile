# RaX-2 Build & Runtime Dockerfile
# Uses Maven for dependency management.
# RaX-2 is a desktop GUI app — X11 forwarding is required for the GUI.
# On Wayland compositors (KDE, GNOME), this works via XWayland.

FROM eclipse-temurin:21-jdk

# Install Maven + X11 libs for GUI
RUN apt-get update && apt-get install -y --no-install-recommends maven \
    libxext6 libxrender1 libxtst6 libxi6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml pom.xml

# Download dependencies (cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code and NetBeans config (still needed for local Ant builds)
COPY src/ /app/src/
COPY nbproject/ /app/nbproject/
COPY build.xml /app/build.xml
COPY manifest.mf /app/manifest.mf

# Build the project (Maven handles compilation + packaging)
RUN mvn package -DskipTests -B

CMD ["java", "-jar", "/app/target/RaX2.jar"]
