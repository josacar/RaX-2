# RaX-2

A Java SE desktop GUI application for remotely controlling an **rssani** server via XML-RPC.

## Overview

RaX-2 is a Swing-based desktop client that provides a graphical interface for managing an rssani backend — a server-side RSS feed monitoring system that handles torrent trackers, regex-based expression matching, email notifications, and scheduled operations.

**Version:** 0.7.1  
**Author:** selu  
**Java Version:** 21

## Features

- View and manage regex expressions from the rssani server
- Manage tracker authentication credentials
- View paginated server logs
- Configure server options (email, paths)
- System tray integration with minimize-to-tray support
- Multiple host profiles with saved credentials
- FlatLaf Light Look & Feel

## Prerequisites

- **Java 21** or higher (JDK)
- **Apache Ant** (for building from CLI)
- **NetBeans IDE** (optional, for GUI editing and IDE integration)

## Dependencies

All dependencies are stored in the `lib/` directory (git-ignored):

| Library | Version | Purpose |
|---------|---------|---------|
| Apache XML-RPC Client | 3.1.3 | XML-RPC client communication |
| Apache XML-RPC Common | 3.1.3 | XML-RPC common types |
| WS-Commons Util | 1.0.2 | XML-RPC utility |
| Commons Logging | 1.1 | Logging framework |
| FlatLaf | 3.7 | Modern Look & Feel |

> **Note:** For Ant builds, dependencies are not tracked in git. Ensure `lib/apache-xmlrpc/` and `lib/flatlaf/` contain the required JARs before building. Maven builds download dependencies automatically.

## Building

### Using Maven

```bash
# Build the project
mvn package

# Run the application
java -jar target/RaX2.jar
```

### Using Docker / Podman

```bash
# Build the image
podman build -t rax2 .
# or: docker build -t rax2 .

# Run (requires X11 socket + .Xauthority for authentication)
podman run --rm \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v $HOME/.Xauthority:/root/.Xauthority:ro \
  -e XAUTHORITY=/root/.Xauthority \
  rax2
```

> **Note:** Swing uses X11 internally. On Wayland compositors, XWayland provides the compatibility layer automatically.

### Using Ant (CLI)

```bash
# Clean and build
ant

# Run the application
ant run

# Clean build artifacts
ant clean

# Debug the application
ant debug
```

The output JAR is generated at `dist/RaX2.jar`.

### Using NetBeans

1. Open the project folder in NetBeans
2. Use standard Run/Debug/Build actions
3. GUI forms (`.form` files) can be edited with the NetBeans Matisse GUI Builder

## Running

```bash
ant run
```

Or run the JAR directly:

```bash
java -jar dist/RaX2.jar
```

## Configuration

Connection settings are persisted using the Java Preferences API:

- **Hosts:** Stored as `item1`, `item2`, etc.
- **Last connected host:** Stored as `lastItem`
- **Per-host credentials:** Stored as `rpcUser{host}` and `rpcPass{host}`

Settings are managed through the application's Options dialog.

## Project Structure

```
RaX-2/
├── src/
│   └── rax2/
│       ├── RaX2App.java          # Application entry point (main class)
│       ├── RaX2View.java         # Main application window (JFrame)
│       ├── Log.java              # Log viewer dialog
│       ├── Opciones.java         # Settings/options dialog
│       ├── Trackers.java         # Tracker auth management dialog
│       ├── NewJFrame.java        # Unused/splash JFrame
│       └── resources/            # UI icon files (PNGs)
├── lib/                          # Dependencies (git-ignored)
│   ├── apache-xmlrpc/
│   ├── flatlaf/
│   └── nblibraries.properties
├── nbproject/                    # NetBeans project configuration
├── build.xml                     # Ant build script
├── manifest.mf                   # JAR manifest
└── build/                        # Build output
    ├── classes/
    └── generated-sources/
```

## XML-RPC API

The client communicates with the rssani server at `http://{host}:{port}/RPC2` using these remote methods:

| Method | Purpose |
|--------|---------|
| `rssani.listaExpresiones` | List all regex expressions |
| `rssani.verTimer` | Get timer interval |
| `rssani.verUltimo` | Get last RSS fetch timestamp |
| `rssani.listaAuths` | List tracker authentications |
| `rssani.verLog` | Fetch log entries (paginated) |
| `rssani.verOpciones` | Get server options |
| `rssani.ponerOpciones` | Set server options |
| `rssani.ponerCredenciales` | Update credentials |
| `rssani.anadirAuth` | Add tracker auth |
| `rssani.borrarAuth` | Remove tracker auth |

## Testing

No automated testing framework is currently configured. The project does not include test source directories or test files.

## Development

- **IDE:** NetBeans is recommended for GUI form editing (Matisse GUI Builder)
- **Build Tool:** Apache Ant via `nbproject/build-impl.xml`
- **Java Level:** Source and target compatibility set to Java 21
- **Look & Feel:** FlatLaf Light theme

### Adding Dependencies

Place required JARs in the appropriate `lib/` subdirectories:

- `lib/apache-xmlrpc/` — XML-RPC libraries
- `lib/flatlaf/` — FlatLaf library

Then update `lib/nblibraries.properties` if needed.

## License

See project for licensing information.
