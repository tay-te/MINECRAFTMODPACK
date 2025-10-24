# Vast Worlds - Terrain Generation Overhaul

**Version:** 1.0.0
**Minecraft:** 1.21.1
**Mod Loader:** NeoForge

## Overview

Vast Worlds is a comprehensive terrain generation mod that dramatically transforms Minecraft's landscape into truly epic, massive environments. Experience worlds that feel genuinely vast with extreme elevation differences, sprawling biomes, and breathtaking vistas.

## Features

### Custom Biomes

The mod introduces 4 unique biomes, each with dramatically different terrain characteristics:

1. **Mega Mountains**
   - Towering peaks reaching toward build limit
   - Snow-covered summits with exposed stone
   - Spawns goats and mountain-dwelling creatures
   - Perfect for epic builds and challenging climbs

2. **Endless Plains**
   - Vast, rolling grasslands that stretch for thousands of blocks
   - Gentle hills and open spaces
   - Abundant passive mobs (horses, cows, sheep)
   - Ideal for large-scale farms and settlements

3. **Deep Canyons**
   - Massive ravines and valley systems
   - Exposed terracotta and sandstone layers
   - Dangerous terrain with exposed caves
   - Rich in ores due to exposed layers

4. **Sky Plateaus**
   - High-elevation flat areas and mesas
   - Dramatic cliff faces
   - Unique elevated ecosystems
   - Perfect for sky bases and observation points

### Terrain Features

- **Extended Height Range**: Utilizes the full 448-block height range (Y: -64 to 384)
- **Amplified Terrain Scale**: Much larger horizontal and vertical terrain variations
- **Custom Noise Generation**: Specially tuned noise functions create more dramatic landscapes
- **Vast Scale**: Biomes feel truly enormous compared to vanilla Minecraft

## Installation

1. Install [NeoForge](https://neoforged.net/) 21.1.78 or newer for Minecraft 1.21.1
2. Place `vastworlds-1.0.0.jar` in your `.minecraft/mods` folder
3. Launch Minecraft and create a new world

## Building from Source

```bash
# Clone the repository
cd MINECRAFTMODPACK

# Build the mod
./gradlew build

# The compiled jar will be in build/libs/
```

## Development

### Project Structure

```
src/main/
├── java/com/vastworlds/
│   ├── VastWorldsMod.java          # Main mod class
│   ├── biomes/
│   │   └── VastBiomes.java         # Biome registry
│   ├── worldgen/
│   │   ├── VastWorldGen.java       # World generation setup
│   │   └── BiomeManager.java       # Biome configuration
│   └── mixin/
│       └── NoiseChunkGeneratorMixin.java  # Terrain generation tweaks
└── resources/
    └── data/vastworlds/worldgen/
        ├── biome/                  # Biome JSON definitions
        ├── noise_settings/         # Custom noise configurations
        └── density_function/       # Terrain shaping functions
```

### Requirements

- Java 21+
- Gradle 8.10+
- Minecraft 1.21.1
- NeoForge 21.1.78+
- ModDevGradle 2.0.115

## Usage Tips

1. **Finding Biomes**: Use `/locate biome vastworlds:<biome_name>` to find specific biomes
2. **Best for Creative**: The dramatic terrain is perfect for large-scale creative builds
3. **Survival Challenge**: Vastly different than vanilla - expect longer travels and more extreme environments
4. **Performance**: The mod uses custom noise generation which may impact performance on lower-end systems

## Customization

You can modify biome generation by editing the JSON files in `data/vastworlds/worldgen/`:

- `biome/` - Modify biome features, colors, and mob spawns
- `noise_settings/` - Adjust terrain height and shape
- `density_function/` - Fine-tune terrain amplitude and variation

## Compatibility

- **Data Packs**: Fully compatible with custom data packs
- **Other Mods**: Should work with most mods; biome mods may require compatibility patches
- **Shaders**: Fully compatible with OptiFine and Iris shaders

## Known Issues

- None currently reported

## Planned Features

- Additional biome variants
- Custom structures for each biome
- Floating island generation
- Underground mega-cavern biomes
- Enhanced ore distribution for vast terrain

## Credits

- Built with NeoForge
- Terrain noise system based on vanilla Minecraft's multi-noise system
- Created for Minecraft 1.21.1

## License

MIT License - Feel free to use, modify, and distribute

## Support

For issues, suggestions, or contributions, please visit the GitHub repository.

---

**Enjoy your vast adventures!**
