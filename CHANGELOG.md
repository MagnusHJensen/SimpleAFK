## 1.20.1 - 1.4.0
- Take into account the players look angle - to avoid AFK pools.
  - To avoid going AFK, a player must both move and look around within the `secondsBeforeAfk` configuration value.
- Add more player events to check for AFK status.
  - Left click and right click.
  - Right click item
  - Right click entity
  - Attack entity