# RaX-2

A Java SE desktop GUI application for remotely controlling an **rssani** server via **gRPC**.

## Overview

RaX-2 is a Swing-based desktop client that provides a graphical interface for managing an rssani backend — a server-side RSS feed monitoring system that handles torrent trackers, regex-based expression matching, email notifications, and scheduled operations.

**Version:** 0.8.0
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
- **Maven** (for building)
- **Podman / Docker** (optional, for containerized builds)

## Building

### Using Maven

```bash
# Build the project
mvn clean package -DskipTests

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

## Running

```bash
java -jar target/RaX2.jar
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
│   ├── rax2/
│   │   ├── RaX2App.java          # Application entry point (main class)
│   │   ├── RaX2View.java         # Main application window (JFrame)
│   │   ├── Log.java              # Log viewer dialog
│   │   ├── Opciones.java         # Settings/options dialog
│   │   ├── Trackers.java         # Tracker auth management dialog
│   │   ├── GrpcErrorHandler.java # gRPC exception handling utility
│   │   ├── Expresion.java        # Data model for RSS expression rules
│   │   ├── TrackerAuth.java      # Data model for tracker credentials
│   │   ├── ServerOptions.java    # Data model for server options
│   │   └── resources/            # UI icon files (PNGs)
│   └── main/proto/
│       └── rssani.proto          # Protocol Buffers service definition
├── nbproject/                    # NetBeans project configuration (legacy)
├── build.xml                     # Ant build script (legacy)
├── pom.xml                       # Maven build configuration
└── target/                       # Build output
    ├── classes/
    ├── generated-sources/        # Auto-generated gRPC/Protobuf code
    └── RaX2.jar
```

## gRPC API

The client communicates with the rssani server at `{host}:{port}` (default `50051`) using a gRPC blocking stub (`RssaniServiceGrpc.RssaniServiceBlockingStub`). Each connection creates a new `ManagedChannel` that is shut down on disconnect.

| Method | Request | Response | Purpose |
|--------|---------|----------|---------|
| `listaExpresiones` | `EmptyRequest` | `RegexpListResponse` | List all regex expressions |
| `verTimer` | `EmptyRequest` | `IntResponse` | Get timer interval (ms) |
| `verUltimo` | `EmptyRequest` | `StringResponse` | Get last RSS fetch timestamp |
| `listaAuths` | `EmptyRequest` | `AuthListResponse` | List tracker authentications |
| `verLog` | `LogRequest{ini, fin}` | `LogResponse{lines[]}` | Fetch log entries (paginated) |
| `verOpciones` | `EmptyRequest` | `OpcionesResponse` | Get server options |
| `ponerOpciones` | `PonerOpcionesRequest` | `BoolResponse` | Set server options |
| `ponerCredenciales` | `PonerCredencialesRequest` | `BoolResponse` | Update credentials |
| `anadirAuth` | `AnadirAuthRequest` | `BoolResponse` | Add tracker auth |
| `borrarAuth` | `BorrarAuthRequest` | `BoolResponse` | Remove tracker auth |
| `anadirRegexp` | `AnadirRegexpRequest` | `BoolResponse` | Add regex expression |
| `editarRegexpI` | `EditarRegexpIRequest` | `BoolResponse` | Edit regex by index |
| `activarRegexp` | `ActivarRegexpRequest` | `BoolResponse` | Toggle regex active state |
| `moverRegexp` | `MoverRegexpRequest` | `BoolResponse` | Move regex in list |
| `borrarRegexpS` | `BorrarRegexpSRequest` | `BoolResponse` | Delete regex by name |
| `guardar` | `EmptyRequest` | `BoolResponse` | Save server state |
| `shutdown` | `EmptyRequest` | `BoolResponse` | Shut down server |

## Testing

No automated testing framework is currently configured. The project does not include test source directories or test files.

## Development

- **Build Tool:** Maven (primary), Ant (legacy)
- **Java Level:** Java 21
- **Look & Feel:** FlatLaf Light theme
- **Proto code generation:** `protobuf-maven-plugin` generates Java stubs from `rssani.proto` at compile time

## Migration from XML-RPC (v0.7.x → v0.8.0)

- Replaced Apache XML-RPC 3.1.3 with gRPC Java (1.70.0)
- `XmlRpcClient` → `ManagedChannel` + `RssaniServiceGrpc.RssaniServiceBlockingStub`
- `client.execute("rssani.method", params)` → `stub.method(request)`
- HashMap response parsing → strongly-typed Protobuf getters
- `XmlRpcErrorHandler` → `GrpcErrorHandler` (handles `StatusRuntimeException`)
- Server port changed from `8080` (XML-RPC) to `50051` (gRPC)

## License

See project for licensing information.
