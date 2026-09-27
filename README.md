# Milestones

**Milestones** is a Minecraft 1.7.10 Forge mod that tracks configurable item and fluid milestones and records when players obtain them.

When a milestone is completed, the player receives a notification containing the milestone and their current playtime. Depending on configuration, the mod can also spawn a trophy representing the milestone and launch a firework celebration.

The milestone list is displayed through a custom ModularUI interface with support for categories, sections, completion times, item icons, and fluid icons.

## Gallery
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/c89b194e-b6e7-4bf2-be82-7939a78e40fb" />
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/10c0e34e-d044-4d31-9425-a9d0fd6416ca" />
<img width="1920" height="1200" alt="image" src="https://github.com/user-attachments/assets/6313d153-d8cb-4846-a155-a9d0d53c1fa3" />

## Features

### Milestone Tracking

Milestones are configured through the `milestones` configuration list.

The mod can detect milestones when items are:

* Added to a player's inventory
* Placed directly into an inventory slot
* Produced by GregTech machines
* Produced by GregTech multiblocks
* Inserted into an Applied Energistics 2 network

Fluid milestones are also supported by the same milestone checking system.

Each player has their own completion state. A milestone is only completed once for a player.

### Build

Linux/macOS:

```bash
./gradlew build
```

Windows:

```bat
gradlew.bat build
```

### Run the client

```bash
./gradlew runClient
```

### Run the server

```bash
./gradlew runServer
```

## Building From Source

Clone the repository and enter the project directory:

```bash
git clone https://github.com/Faotik/Milestones.git
cd Milestones
```

Then build with Gradle:

```bash
./gradlew build
```

The generated mod JAR is placed in:

```text
build/libs/
```

### Trophy System

Milestone completion can optionally create a trophy item.

The trophy stores:

* The completed milestone
* The player who completed it
* The player's playtime
* The completion date

Trophies can be placed as blocks in the world.

## Integrations

### GregTech

GregTech integration detects milestone items and fluids produced by:

* `MTEBasicMachine`
* `MTEMultiBlockBase`

The integration is implemented using late Mixins and is only applied when GregTech is loaded and the integration is enabled.

Configuration:

```text
GTIntegration = true
```

### Applied Energistics 2

AE2 integration detects items and fluids inserted into an AE2 network.

The implementation handles both:

* Player-originated actions
* Machine-originated actions

Machine actions are resolved back to the associated player through AE2's player mapping.

Configuration:

```text
AE2Integration = true
```

### ServerUtilities

When ServerUtilities integration is enabled, milestone completion can be propagated to the player's team.

Instead of completing a milestone only for the player that triggered it, the mod checks the player's ServerUtilities team and completes the milestone for the team members.

Configuration:

```text
SUIntegration = true
```

If ServerUtilities is not loaded, the normal per-player completion system is used.

## Configuration

Milestones uses GTNHLib's configuration system.

The configuration files are located under:

```text
config/Milestones/
```

### `milestones.cfg`

Contains the milestone definitions.

Example:

```text
S:items <
    $Minecraft,minecraft:grass
    ^Tier 1
    minecraft:diamond
    minecraft:gold_ingot
    ^Tier 2
    minecraft:apple
    minecraft:stick
    minecraft:stone
>
```

### Milestone entries

A normal entry represents an item or fluid identifier:

```text
minecraft:diamond
```

Metadata can be specified as an additional component:

```text
minecraft:wool:4
```

The mod resolves item identifiers through Minecraft's `GameRegistry` and fluid identifiers through Forge's `FluidRegistry`.

### Category tabs

Entries beginning with `$` create a new GUI tab.

Format:

```text
$Tab Name,identifier
```

Example:

```text
$Minecraft,minecraft:grass
```

The first part becomes the tab title, while the identifier determines the icon used for the tab.

### Section headings

Entries beginning with `^` create a section heading within the current tab.

Example:

```text
^Basic Resources
minecraft:stone
minecraft:dirt
minecraft:gravel

^Advanced Resources
minecraft:diamond
minecraft:emerald
```

### `server.cfg`

Server-side options include:

| Option            | Default | Description                                       |
| ----------------- | ------: | ------------------------------------------------- |
| `GTIntegration`   |  `true` | Enables GregTech milestone detection              |
| `AE2Integration`  |  `true` | Enables Applied Energistics 2 milestone detection |
| `SUIntegration`   |  `true` | Enables ServerUtilities team integration          |
| `enableTrophies`  |  `true` | Enables trophy registration and spawning          |
| `enableFireworks` |  `true` | Spawns a firework when a milestone is completed   |

### `client.cfg`

Client-side options include:

| Option                     | Default | Description                                                       |
| -------------------------- | ------: | ----------------------------------------------------------------- |
| `replaceAchievementButton` | `false` | Replaces the vanilla Achievements button with a Milestones button |

## Project Structure

```text
src/main/java/Milestones/
├── BlockContainer/
│   └── TrophyBlockContainer.java
├── Commands/
│   └── CommandMilestones.java
├── Configs/
│   ├── ConfigClient.java
│   ├── ConfigMilestones.java
│   ├── ConfigRegister.java
│   └── ConfigServer.java
├── Events/
│   ├── GuiScreenEventHandler.java
│   └── PlayerLoggedInEventHandler.java
├── GUI/
│   ├── GUIDataMilestones.java
│   └── GUIFactoryMilestones.java
├── ItemBlock/
│   └── TrophyItemBlock.java
├── ItemRenderer/
│   └── TrophyItemRenderer.java
├── Mixins/
│   ├── Early/
│   │   └── MixinInventoryPlayer.java
│   └── Late/
│       ├── MixinMTEBasicMachine.java
│       ├── MixinMTEMultiBlockBase.java
│       ├── MixinNetworkMonitor.java
│       └── MixinPlayerData.java
├── Models/
│   └── TrophyModel.java
├── Packets/
│   └── PacketOpenMilestones.java
├── SaveData/
│   └── CompletedMilestonesCacheSaveData.java
├── TESR/
│   └── TrophyTESR.java
├── TileEntity/
│   └── TrophyTileEntity.java
├── UI/
│   ├── HorizontalHiddenScrollData.java
│   └── VerticalHiddenScrollData.java
├── Milestones.java
├── CommonProxy.java
├── ClientProxy.java
└── Utils.java
```

## Dependencies

The project currently uses the following GTNH components:

* GTNHLib
* ModularUI2
* GregTech 5 Unofficial
* Applied Energistics 2 Unofficial
* ServerUtilities
* NotEnoughItems for the development runtime

GregTech, AE2, and ServerUtilities functionality is conditionally applied through the corresponding integration settings.

## License
[![License: MPL 2.0](https://img.shields.io/badge/License-MPL_2.0-brightgreen.svg)](https://opensource.org/licenses/MPL-2.0)
