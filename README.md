# SimpleAFK

![AFK Tab List](https://github.com/MagnusHJensen/SimpleAFK/blob/1.21.1/images/afk-tab-list.png?raw=true "Tab list with an AKF tag added to the Dev player")

### This mod enables simple AFK management for servers.

It provides the ability to configure:
- when a player is marked as afk.
- when (if at all) a player is kicked for being afk too long.
- Formatting options to format the afk message, kick message and afk name in tab list.
- Includes a bypass list that can be configured via commands.

It skips players that are AFK when checking for the percentage of players sleeping.

_This overrides the vanilla `setIdleTimeout` command, and discards any values set in there._

![AFK Messages](https://github.com/MagnusHJensen/SimpleAFK/blob/1.21.1/images/afk-messages.png?raw=true "Chat messages of a player going AFK and then no longer being marked as AFK")

<details>
<summary><b>Permission Nodes</b></summary>

Simple AFK introduces some standard permission nodes if you are using a permission system.

_**NOTE:** The default defined in () only matters if you are **NOT** using a permission mod._
- `simpleafk.toggle` - Allows the player to toggle their AFK status
- `simpleafk.toggle.target` - Allows the player to toggle another player's AFK status
- `simpleafk.bypass` - Allows the player to bypass AFK status (Meaning they won't get marked as AFK and won't get kicked)
- `simpleafk.bypass_sleep` - Allows the player to bypass the [sleep required percentage](https://minecraft.fandom.com/wiki/Game_rule) check (Default: Only OP's have this permission)

</details>

## Wiki

Check out the [wiki](https://github.com/magnushjensen/simpleafk/wiki) for detailed documentation.

If you think anything is missing from the wiki that is unclear, send a message in the discord or open an [improve documentation issue](https://github.com/MagnusHJensen/simpleafk/issues/new?template=3.Improve_docs.md).

## Roadmap

Check out the version [milestones](https://github.com/MagnusHJensen/simpleafk/milestones)

## Links

Join my [Discord](https://discord.gg/PHu8k32M3q) for support and updates! (Just created, so barebones)
- [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/simpleafk)
- [Modrinth page](https://modrinth.com/mod/simpleafk)


## Thanks to

- [jaredlll08](https://github.com/jaredlll08) for creating [`Multiloader-Template`](https://github.com/jaredlll08/MultiLoader-Template)