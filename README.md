# SimpleAFK

![AFK Tab List](https://github.com/MagnusHJensen/SimpleAFK/blob/1.20.x/images/afk-tab-list.png?raw=true "Tab list with an AKF tag added to the Dev player")

### This mod enables simple AFK management for servers.

It provides the ability to configure:
- when a player is marked as afk.
- when (if at all) a player is kicked for being afk too long.
- Formatting options to format the afk message, kick message and afk name in tab list.

![AFK Messages](https://github.com/MagnusHJensen/SimpleAFK/blob/1.20.x/images/afk-messages.png?raw=true "Chat messages of a player going AFK and then no longer being marked as AFK")

<details>
<summary><b>Permission Nodes</b></summary>

Simple AFK introduces some standard permission nodes if you are using a permission system.
- `simpleafk.toggle` - Allows the player to toggle their AFK status (Default: Everyone has this permission)
- `simpleafk.toggle.target` - Allows the player to toggle another player's AFK status (Default: Only OP's have this permission)
- `simpleafk.bypass` - Allows the player to bypass AFK status (Meaning they won't get marked as AFK and won't get kicked) (Default: Only OP's have this permission)

</details>