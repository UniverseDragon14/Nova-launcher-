# Universal Dragon Launcher for Android

Native Android portfolio/launcher prototype built with Kotlin, Jetpack Compose, Material 3, and Navigation Compose.

## Current screens

- Home control-room style dashboard
- Projects
- EVE chat mockup
- NOVA chat mockup
- Askutty approval/status mockup

Navigation is implemented locally in MainActivity. The UI uses bundled image assets and Compose components.

## Current boundary

The current default branch does not implement:

- a network AI backend
- persistent chat or project storage
- speech recognition or text-to-speech
- camera, GPIO, robot, or Raspberry Pi control
- physical quantum-computer access
- production authentication

EVE/NOVA replies and status cards are local demonstration content. Labels such as “listening” or system status are UI presentation, not live microphone/device evidence.

## Build

Project configuration:

- compile/target SDK 35
- minimum SDK 26
- Java 21
- application module: app

Open the repository in a compatible Android Studio version, sync Gradle, and run the app module. A Gradle wrapper is not currently committed, so command-line builds require a compatible local Gradle installation or adding the wrapper first.

## Repository hygiene

Generated .gradle cache/build artifacts should stay out of source control. Before a release, add tests, accessibility checks, network/security design, signed release configuration, privacy disclosures, and verified backend integration.
