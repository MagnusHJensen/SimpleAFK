## 1.20.1 - 1.3.0
- Add support for exempting specific players from the AFK plugin.
  - Includes config option to not count exempt players in the sleep vote. _(Defaults to not including them)_
  - Use `/afk bypass-list add/remove <playerUUID>` to add or remove players from the exempt list.
    - Requires the `bypass.modify` permission, which defaults to OP's.