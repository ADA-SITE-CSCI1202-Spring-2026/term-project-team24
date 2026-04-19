<<<<<<< HEAD
Skyways Airport Dispatch Tycoon

This project simulates an airport ground operations system.

What I have done so far

- Created the project structure in Java (VS Code)
- Set up Git and connected the project to GitHub
- Created a separate branch for development work
- Implemented the Aircraft class hierarchy:
  - Abstract Aircraft class
  - CommercialJet class
  - CargoFreighter class
  - PrivateCharter class
- Added basic attributes like:
  - flight number
  - fuel required
  - turnaround time
- Tested the classes using a simple main method
=======
[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/TDA47i3L)

# Skyways — Ground Operations Dashboard

A real-time airport dispatch simulation built with Java and JavaFX.

---

## How to Run the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/ADA-SITE-CSCI1202-Spring-2026/term-project-team24.git
cd term-project-team24
```

---

### Step 2: Install Java 21

Check if you have Java 21 already:
```bash
java -version
```

If it does not say `openjdk 21`, install it:

**Mac:**
```bash
brew install --cask temurin@21
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 21)' >> ~/.zshrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.zshrc
source ~/.zshrc
```

**Windows:**
Download and install from: https://adoptium.net
Select: Java 21, Windows, your architecture (x64 for most PCs)

Verify:
```bash
java -version
```
Should say `openjdk 21`.

---

### Step 3: Download JavaFX 21

Go to: https://gluonhq.com/products/javafx/

Download with these exact settings:
- Version: **21**
- Operating System: **your OS** (macOS / Windows / Linux)
- Architecture:
  - Mac M1/M2/M3 → **aarch64**
  - Mac Intel → **x64**
  - Windows → **x64**
- Type: **SDK**

---

### Step 4: Set Up the lib Folder

After downloading, extract the JavaFX SDK zip file.
Inside it you will find a folder called `lib`.

**Mac:**
```bash
cp ~/Downloads/javafx-sdk-21*/lib/*.jar term-project-team24/lib/
cp ~/Downloads/javafx-sdk-21*/lib/*.dylib term-project-team24/lib/
```

**Windows:**
Copy all `.jar` files from the extracted `javafx-sdk-21\lib\` folder
into the `lib\` folder inside your project. You can do this manually
in File Explorer.

---

### Step 5: Compile the Project

Make sure you are inside the project folder, then run:

**Mac / Linux:**
```bash
javac --module-path lib --add-modules javafx.controls,javafx.fxml -d out src/*.java
```

**Windows:**
```bash
javac --module-path lib --add-modules javafx.controls,javafx.fxml -d out src\*.java
```

If you see no output, it compiled successfully.

---

### Step 6: Run the App

**Mac / Linux:**
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp out App
```

**Windows:**
```bash
java --module-path lib --add-modules javafx.controls,javafx.fxml -cp out App
```

The Skyways Ground Operations Dashboard window will open.

---

## Project Structure

```
term-project-team24/
├── src/
│   ├── App.java               ← entry point, launches the window
│   ├── MainController.java    ← GUI (Person C)
│   ├── DepotManager.java      ← resource management (Person B)
│   └── SupplyItem.java        ← supply enum (Person B)
├── lib/                       ← JavaFX jars (NOT on GitHub, add locally)
├── out/                       ← compiled .class files (auto-generated)
├── .gitignore
└── README.md
```

---

## Important Notes

- The `lib/` folder is **not on GitHub** — every team member adds it locally
- Never commit the `lib/` or `out/` folders
- Always compile before running after pulling new changes
- If you get a JavaFX version mismatch error, make sure your JavaFX version matches your Java version (both must be 21)
>>>>>>> features/jala
