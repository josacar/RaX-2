# AGENTS.md

## Project Overview

**RaX-2** is a Java SE 11 desktop GUI application for remotely controlling an **rssani** server via XML-RPC. It's a NetBeans Ant-based project with Swing UI components.

## Build Commands

### Docker / Podman

```bash
# Build the image
podman build -t rax2 .

# Run (requires X11 socket + .Xauthority for authentication)
podman run --rm \
  -e DISPLAY=$DISPLAY \
  -v /tmp/.X11-unix:/tmp/.X11-unix \
  -v $HOME/.Xauthority:/root/.Xauthority:ro \
  -e XAUTHORITY=/root/.Xauthority \
  rax2
```

> Swing uses X11 internally. On Wayland, XWayland provides compatibility.

### Local (Ant)

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

**Output:** `dist/RaX2.jar`

## Testing

No automated testing framework is configured. There are no test source directories or test files.

## Development Guidelines

### Project Structure

- **Source code:** `src/rax2/`
- **Dependencies:** `lib/` (git-ignored — must be manually present)
- **Build config:** `nbproject/` + `build.xml`
- **GUI forms:** `.form` files alongside `.java` files (NetBeans Matisse)

### Dependencies

Dependencies are **not** tracked in git. Ensure these directories contain the required JARs before building:

- `lib/apache-xmlrpc/` — Apache XML-RPC 3.1.3 (client, common, server), WS-Commons Util 1.0.2, Commons Logging 1.1
- `lib/jgoodies/` — JGoodies Looks 2.5.3, JGoodies Common 1.8.1

### Code Conventions

- **Package:** Single flat package `rax2` (no sub-packages)
- **Language:** Java 11
- **UI strings:** Mixed Spanish/English (Spanish is intentional per author locale)
- **Look & Feel:** JGoodies Plastic3D with `ExperienceBlue` theme
- **Entry point:** `rax2.RaX2App` (main class)

### XML-RPC Communication

The app communicates with the rssani server at `http://{host}:{port}/RPC2`. All remote method calls use the `rssani.*` namespace. See README.md for the full method list.

### Configuration

Runtime settings use the Java Preferences API (`java.util.prefs.Preferences`). Hosts and credentials are stored per-host and persisted across sessions.

### Key Files

| File | Description |
|------|-------------|
| `src/rax2/RaX2App.java` | Application entry point |
| `src/rax2/RaX2View.java` | Main window (JFrame, ~1145 lines) |
| `src/rax2/Log.java` | Log viewer dialog |
| `src/rax2/Opciones.java` | Settings/options dialog |
| `src/rax2/Trackers.java` | Tracker auth management dialog |
| `Dockerfile` | Multi-stage Docker build (builder + runtime) |
| `build.xml` | Ant build script |
| `nbproject/project.properties` | Project configuration |

### NetBeans

NetBeans is the recommended IDE. The project uses:

- **Matisse GUI Builder** for `.form` files
- **CopyLibs** task for bundling dependencies
- Standard NetBeans project structure

### Known Issues

- `RaX2App.main()` creates two instances of `RaX2App` (one is unused)
- `NewJFrame.java` appears to be an unused/splash JFrame template
