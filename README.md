# SimpleAFK

![AFK Tab List](https://github.com/MagnusHJensen/SimpleAFK/blob/1.20.x/images/afk-tab-list.png?raw=true "Tab list with an AKF tag added to the Dev player")

### This mod enables simple AFK management for servers.

It provides the ability to configure:
- when a player is marked as afk.
- when (if at all) a player is kicked for being afk too long.
- Formatting options to format the afk message, kick message and afk name in tab list.

It skips players that are AFK when checking for the percentage of players sleeping.

_This overrides the vanilla `setIdleTimeout` command, and discards any values set in there._

![AFK Messages](https://github.com/MagnusHJensen/SimpleAFK/blob/1.20.x/images/afk-messages.png?raw=true "Chat messages of a player going AFK and then no longer being marked as AFK")

<details>
<summary><b>Permission Nodes</b></summary>

Simple AFK introduces some standard permission nodes if you are using a permission system.

_**NOTE:** The default defined in () only matters if you are **NOT** using a permission mod._
- `simpleafk.toggle` - Allows the player to toggle their AFK status (Default: Everyone has this permission)
- `simpleafk.toggle.target` - Allows the player to toggle another player's AFK status (Default: Only OP's have this permission)
- `simpleafk.bypass` - Allows the player to bypass AFK status (Meaning they won't get marked as AFK and won't get kicked) (Default: Only OP's have this permission)
- `simpleafk.bypass_sleep` - Allows the player to bypass the [sleep required percentage](https://minecraft.fandom.com/wiki/Game_rule) check (Default: Only OP's have this permission)

</details>