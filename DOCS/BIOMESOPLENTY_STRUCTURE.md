# Vast Worlds - BiomesOPlenty-Style Implementation

## Overview
This mod follows **BiomesOPlenty's proven architecture** for biome generation in Minecraft 1.21.1 using NeoForge.

## Architecture Breakdown

### 1. **Datagen System** (Key Innovation!)

BiomesOPlenty doesn't create biomes at runtime - they use **datagen** to generate JSON files at build time.

**How it works:**
```bash
./gradlew runData  # Generates JSON files from programmatic definitions
./gradlew build    # Includes generated files in the mod
./gradlew runClient # Game loads the generated JSON files
```

**Files:**
- `src/main/java/com/vastworlds/datagen/DataGenerationHandler.java` - Event listener for datagen
- `src/generated/resources/data/vastworlds/worldgen/biome/*.json` - Generated biome files

### 2. **Programmatic Biome Creation**

**Factory Pattern** (BiomesOPlenty-style)

`src/main/java/com/vastworlds/biome/VastWorldsOverworldBiomes.java`
```java
public static Biome megaMountains(HolderGetter<PlacedFeature> features,
                                   HolderGetter<ConfiguredWorldCarver<?>> carvers) {
    // Create MobSpawnSettings
    // Create BiomeGenerationSettings
    // Create BiomeSpecialEffects
    // Return complete Biome object
}
```

**Benefits:**
- Full programmatic control over biome properties
- Type-safe biome creation
- Easy to add/modify biomes
- No manual JSON editing

### 3. **Bootstrap System**

`src/main/java/com/vastworlds/biome/ModBiomes.java`
```java
public static void bootstrapBiomes(BootstrapContext<Biome> context) {
    // Called ONLY during datagen
    // Registers biomes to be written as JSON
    register(context, MEGA_MOUNTAINS, megaMountains(...));
    register(context, ENDLESS_PLAINS, endlessPlains(...));
    // etc.
}
```

### 4. **TerraBlender Integration**

**Biome Distribution Using Climate Parameters**

`src/main/java/com/vastworlds/biome/VastWorldsBiomeBuilder.java`
```java
// 5x5 Temperature/Humidity matrix
protected final Climate.Parameter[] temperatures = {...}; // Frozen to Hot
protected final Climate.Parameter[] humidities = {...};   // Arid to Very Wet
protected final Climate.Parameter[] erosions = {...};     // Jagged to Flat

// Terrain slices (following BOP pattern)
addPeaks(...)      // Extreme mountain peaks
addHighSlice(...)  // Elevated terrain
addMidSlice(...)   // Standard elevation
addLowSlice(...)   // Lower terrain
addValleys(...)    // Rivers and lowlands
```

**Region System**

`src/main/java/com/vastworlds/worldgen/VastWorldsRegion.java`
```java
public class VastWorldsRegion extends Region {
    public VastWorldsRegion(int weight) {
        super(LOCATION, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry,
                          Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        new VastWorldsBiomeBuilder().addBiomes(registry, mapper);
    }
}
```

### 5. **Directory Structure**

```
src/main/java/com/vastworlds/
├── VastWorldsMod.java                      # Main mod class
├── biome/
│   ├── ModBiomes.java                      # Biome registration & bootstrap
│   ├── VastWorldsBiomes.java               # Biome ResourceKeys
│   ├── VastWorldsBiomeBuilder.java         # Climate-based placement
│   └── VastWorldsOverworldBiomes.java      # Biome factory methods
├── worldgen/
│   ├── VastWorldsRegion.java               # TerraBlender region
│   ├── VastWorldsSurfaceRules.java         # Surface rule registration
│   └── VastWorldsSurfaceRuleData.java      # Surface rules per biome
└── datagen/
    └── DataGenerationHandler.java          # Datagen event handler

src/main/resources/data/vastworlds/worldgen/
├── biome/                                   # OLD: Manual JSON files (now deprecated)
├── density_function/                        # Terrain shape functions
└── noise_settings/                          # Noise configuration

src/generated/resources/data/vastworlds/worldgen/
└── biome/                                   # NEW: Generated from factory methods
    ├── mega_mountains.json
    ├── endless_plains.json
    ├── deep_canyons.json
    └── sky_plateaus.json
```

## Workflow

### Adding a New Biome (BiomesOPlenty Way)

1. **Create ResourceKey** in `VastWorldsBiomes.java`
```java
public static final ResourceKey<Biome> FLOATING_ISLANDS = register("floating_islands");
```

2. **Create Factory Method** in `VastWorldsOverworldBiomes.java`
```java
public static Biome floatingIslands(HolderGetter<PlacedFeature> features,
                                    HolderGetter<ConfiguredWorldCarver<?>> carvers) {
    MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
    // Configure spawns...

    BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(...);
    // Configure features, ores, vegetation...

    return biome(true, 0.5F, 0.5F, spawns, generation, MUSIC);
}
```

3. **Register in Bootstrap** in `ModBiomes.java`
```java
register(context, VastWorldsBiomes.FLOATING_ISLANDS,
        VastWorldsOverworldBiomes.floatingIslands(features, carvers));
```

4. **Add to Biome Placement** in `VastWorldsBiomeBuilder.java`
```java
// In pickMiddleBiome() or addPeaks() etc.
if (temperatureIndex == 2 && humidityIndex == 3) {
    return VastWorldsBiomes.FLOATING_ISLANDS;
}
```

5. **Run Datagen**
```bash
./gradlew runData  # Generates the JSON file automatically!
```

## Key Differences from Manual JSON Approach

| Aspect | Manual JSON | BiomesOPlenty/Datagen |
|--------|-------------|----------------------|
| Biome Creation | Hand-write JSON | Factory methods in Java |
| Feature Management | Manual feature lists | BiomeDefaultFeatures + custom |
| Type Safety | None (JSON) | Full (Java) |
| Validation | Runtime errors | Compile-time checks |
| Duplication | High (copy/paste JSON) | Low (reusable methods) |
| Maintenance | Tedious | Easy refactoring |

## Benefits of This Approach

1. ✅ **No Runtime JSON Errors** - Everything validated at compile time
2. ✅ **DRY Principle** - Reusable helper methods for common patterns
3. ✅ **Full IDE Support** - Autocomplete, refactoring, find usages
4. ✅ **Version Control Friendly** - Java diffs are clearer than JSON diffs
5. ✅ **BiomesOPlenty Compatible** - Same patterns, easy to learn from their code
6. ✅ **Generated Files Are Perfect** - Complete, valid JSON with all features

## Commands

### Development Workflow
```bash
# Make changes to factory methods
vim src/main/java/com/vastworlds/biome/VastWorldsOverworldBiomes.java

# Regenerate JSON files
./gradlew runData

# Build and test
./gradlew build
./gradlew runClient
```

### Build Tasks
```bash
./gradlew build        # Compile and package
./gradlew runClient    # Run Minecraft client
./gradlew runServer    # Run dedicated server
./gradlew runData      # Run datagen (regenerate JSON files)
```

## Future Enhancements (BiomesOPlenty Pattern)

### Multiple Regions
BiomesOPlenty uses 3 regions with different weights:
- **Primary Region** (weight: 10) - Common biomes
- **Secondary Region** (weight: 5) - Uncommon biomes
- **Rare Region** (weight: 2) - Rare biomes

To implement:
```java
public class VastWorldsRegionPrimary extends Region { /* weight 10 */ }
public class VastWorldsRegionSecondary extends Region { /* weight 5 */ }
public class VastWorldsRegionRare extends Region { /* weight 2 */ }
```

### Custom Features & Placements
Create custom placed features and configured features, then bootstrap them:
```java
// In DataGenerationHandler
private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
    .add(Registries.CONFIGURED_FEATURE, CustomFeatures::bootstrap)
    .add(Registries.PLACED_FEATURE, CustomPlacements::bootstrap)
    .add(Registries.BIOME, ModBiomes::bootstrapBiomes);
```

### Surface Rules Per Biome
Customize block surfaces for each biome in `VastWorldsSurfaceRuleData.java`

## Resources

- BiomesOPlenty Source: `BiomesOPlenty-1.21.10/`
- TerraBlender Wiki: [GitHub](https://github.com/Glitchfiend/TerraBlender/wiki)
- Minecraft Wiki - Custom Biomes: [wiki.gg](https://minecraft.wiki)

## Conclusion

This implementation follows **BiomesOPlenty's battle-tested architecture**:
- Programmatic biome creation with full type safety
- Datagen to generate perfect JSON files at build time
- TerraBlender for sophisticated climate-based distribution
- Terrain slices for natural biome placement across elevations

**The result:** A maintainable, scalable biome system that's easy to extend and debug!
