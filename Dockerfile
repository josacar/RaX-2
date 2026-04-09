# RaX-2 Build & Runtime Dockerfile
# Single-stage build using locally available eclipse-temurin:21-jdk.
# RaX-2 is a desktop GUI app — X11 forwarding is required for the GUI.
# On Wayland compositors (KDE, GNOME), this works via XWayland.

FROM eclipse-temurin:21-jdk

# Install Ant for the NetBeans Ant-based build
RUN apt-get update && apt-get install -y --no-install-recommends ant wget \
    libxext6 libxrender1 libxtst6 libxi6 \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy project build config and source first
COPY lib/ lib/
COPY src/ /app/src/
COPY nbproject/ /app/nbproject/
COPY build.xml /app/build.xml
COPY manifest.mf /app/manifest.mf

# Download dependencies into lib subdirectories (not tracked in git)
RUN cd lib/apache-xmlrpc && \
    wget -q https://repo1.maven.org/maven2/commons-logging/commons-logging/1.1/commons-logging-1.1.jar && \
    wget -q https://repo1.maven.org/maven2/org/apache/ws/commons/util/ws-commons-util/1.0.2/ws-commons-util-1.0.2.jar && \
    wget -q https://repo1.maven.org/maven2/org/apache/xmlrpc/xmlrpc-client/3.1.3/xmlrpc-client-3.1.3.jar && \
    wget -q https://repo1.maven.org/maven2/org/apache/xmlrpc/xmlrpc-common/3.1.3/xmlrpc-common-3.1.3.jar && \
    wget -q https://repo1.maven.org/maven2/org/apache/xmlrpc/xmlrpc-server/3.1.3/xmlrpc-server-3.1.3.jar && \
    cd ../jgoodies && \
    wget -q https://repo1.maven.org/maven2/com/jgoodies/jgoodies-common/1.8.1/jgoodies-common-1.8.1.jar && \
    wget -q https://repo1.maven.org/maven2/com/jgoodies/jgoodies-looks/2.5.3/jgoodies-looks-2.5.3.jar && \
    cd ../..

# Build the project (compiles, packages JAR, and generates javadoc)
RUN ant

CMD ["java", "-jar", "/app/dist/RaX2.jar"]
