# Skyways Airport Dispatch Tycoon (Team 24)

Real-time airport ground operations simulation built with Java and JavaFX for CSCI 1202 PP2 Term Project.

## Features

- Automatic flight generation every 3 seconds
- FIFO queue for incoming flights
- 3 aircraft types using inheritance:
  - `CommercialJet`
  - `CargoFreighter`
  - `PrivateCharter`
- Polymorphic ground service processing via `IGroundService`:
  - `FuelingTruck`
  - `CateringVan`
  - `BaggageHandler`
- Encapsulated resource and budget management via `DepotManager`
- Save/Load state to `airport_state.csv`
- 4 required GUI zones:
  - Queue panel
  - Resource state panel
  - Supply purchase panel
  - System log panel

## Project Structure

- `src/app`
  - `App.java` (entry point)
  - `MainController.java` (GUI + handlers)
- `src/model`
  - `Aircraft.java` and subclasses
  - `SupplyItem.java`
  - `DepotManager.java`
- `src/service`
  - `SimulationEngine.java`
  - `IGroundService.java` and service implementations
  - `SaveLoadManager.java`
- `airport_state.csv` (save/load file)
- `lib/` (JavaFX jars/native libraries)

## Requirements

- Java 21 (recommended)
- JavaFX SDK matching your Java version

## Compile

Run from project root (`term-project-team24`):

### macOS / Linux

```bash
mkdir -p out
javac --module-path lib --add-modules javafx.controls,javafx.fxml -d out $(find src -name "*.java")
```

### Windows (PowerShell)

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac --module-path lib --add-modules javafx.controls,javafx.fxml -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

## Run

### macOS / Linux

```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp out app.App
```

### Windows (PowerShell)

```powershell
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp out app.App
```

## Save/Load

- Click `Save State` to write current budget, supplies, and queue to `airport_state.csv`.
- Click `Load State` to restore simulation state from `airport_state.csv`.

## Notes

- Ensure JavaFX libraries in `lib/` are compatible with your OS and Java version.
- If JavaFX is missing, compile/run commands will fail with module errors.
