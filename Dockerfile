# RaX-2 Build & Runtime Dockerfile
# Single-stage build using locally available eclipse-temurin:21-jdk.
# RaX-2 is a desktop GUI app — X11 forwarding is required for the GUI.
# On Wayland compositors (KDE, GNOME), this works via XWayland.

FROM eclipse-temurin:21-jdk

# Install Ant for the NetBeans Ant-based build
RUN apt-get update && apt-get install -y --no-install-recommends ant \
    libxext6 libxrender1 libxtst6 libxi6 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy dependency JARs first (these are git-ignored but required for the build)
COPY lib/ /app/lib/

# Copy project source and build files
COPY src/ /app/src/
COPY nbproject/ /app/nbproject/
COPY build.xml /app/build.xml
COPY manifest.mf /app/manifest.mf

# Build the project (compiles, packages JAR, and generates javadoc)
RUN ant

CMD ["java", "-jar", "/app/dist/RaX2.jar"]
