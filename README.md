# 🚀 Space Shooter

A fast-paced 2D arcade shooter for Android, written in **Kotlin** with a custom game loop on a `SurfaceView` — no game engine.

Fly your ship anywhere on screen, tap to fire, and survive waves of enemies dropping from above.

## Features

- **Custom 60 FPS game loop** running on its own thread, drawing directly to a `SurfaceView` canvas
- **Touch controls** – drag to steer the ship in any direction, tap to shoot
- **Enemy spawning with spacing rules** so new enemies don't stack on top of each other
- **Collision detection** between bullets, enemies and the player
- **Score counter**, pause button, game-over screen with final score and restart
- **Thread-safe updates** – enemy and bullet lists are synchronized between the game thread and the UI thread

## Tech stack

| | |
|---|---|
| Language | Kotlin |
| Platform | Android (Android Studio, Gradle Kotlin DSL) |
| Rendering | `SurfaceView` + `Canvas`, custom game loop |

## Project structure

```
app/src/main/java/com/example/spaceshootergame/
├── GameActivity.kt   # hosts the game view, pauses/resumes with the app lifecycle
├── GameView.kt       # game loop, spawning, collisions, input, drawing
├── player.kt         # player ship movement and bounds
├── Enemy.kt          # enemy movement and hitboxes
└── Bullet.kt         # projectile movement and hitboxes
```

## Run it

1. Clone the repo
   ```bash
   git clone https://github.com/PeashSalek/SpaceShooterGame.git
   ```
2. Open the folder in **Android Studio** and let Gradle sync.
3. Run on a device or emulator.

## How to play

- **Drag** on the screen to move your ship
- **Tap** to shoot
- Don't let an enemy hit you — one hit and it's game over
- Tap **Start** on the game-over screen to play again

## Roadmap

- [ ] Levels that speed up as your score grows
- [ ] Saved high score
- [ ] Sound effects and music
- [ ] More enemy types and movement patterns (art for these is already in `res/drawable`)

## Author

**Peash Salek** — MSc student in Software Engineering, LUT University, Finland
[GitHub](https://github.com/PeashSalek) · [LinkedIn](https://www.linkedin.com/in/piash-salek-4a2916262)
