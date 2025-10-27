# Minecraft 1.21.1 Terrain Generation - Complete Guide

## 📚 Table of Contents
1. [How Minecraft Generates Terrain](#how-minecraft-generates-terrain)
2. [Your Current Implementation](#your-current-implementation)
3. [Key Components to Modify](#key-components-to-modify)
4. [Advanced Modifications](#advanced-modifications)

---

## How Minecraft Generates Terrain

### The Generation Pipeline

```
1. Dimension Settings (overworld.json)
   ↓
2. Noise Generator (NoiseChunkGenerator)
   ↓
3. Biome Source (MultiNoiseBiomeSource) → Determines biome placement
   ↓
4. Noise Settings (vast_overworld.json) → Controls terrain shape
   ↓
5. Density Functions → 3D noise that shapes terrain
   ↓
6. Surface Rules → Determines what blocks appear on surface
   ↓
7. Carvers → Creates caves and canyons
   ↓
8. Features → Adds trees, ores, structures
```

### Core Classes (From Yarn Mappings)

**Main Generation:**
- `NoiseChunkGenerator` - The main terrain generator
- `ChunkGenerator` - Base class for all generators
- `NoiseRouter` - Routes density functions together

**Density Functions:**
- `DensityFunction` - Interface for 3D noise
- `DensityFunctions` - Factory methods
- Types: add, mul, min, max, cache, noise, y_clamped_gradient, etc.

**Biomes:**
- `MultiNoiseBiomeSource` - Places biomes using noise parameters
- `Climate` - Contains temperature, humidity, continentalness, erosion, depth, weirdness

---

## Your Current Implementation

### ✅ What You Already Have

**4 Custom Biomes:**
```
1. Mega Mountains  - Extreme peaks (temp: -1.0 to -0.3)
2. Endless Plains  - Flat grasslands (erosion: 0.3 to 1.0)
3. Deep Canyons    - Dramatic valleys (erosion: -1.0 to -0.4)
4. Sky Plateaus    - High plateaus (weirdness: 0.5 to 1.0)
```

**Custom Noise Settings:**
- Height range: -64 to 384 (448 total blocks)
- Sea level: 63
- Aquifers: Enabled
- Ore veins: Enabled

**Custom Carvers:**
- `vast_canyon` - Creates large canyons
- `vast_cave` - Creates cave systems

---

## Key Components to Modify

### 1. Height and Scale

**File:** `data/vastworlds/worldgen/noise_settings/vast_overworld.json`

```json
{
  "noise": {
    "min_y": -64,      // Lower = deeper caves
    "height": 448,     // Total height (can go up to 512)
    "size_horizontal": 1,  // Increase = smoother terrain
    "size_vertical": 2     // Increase = more gradual slopes
  }
}
```

**Effect:**
- `size_horizontal: 2` = Terrain 2x smoother horizontally
- `size_vertical: 4` = Gentler vertical changes (fewer cliffs)

### 2. Terrain Amplification

**Location:** `initial_density_without_jaggedness` section

```json
{
  "type": "minecraft:mul",
  "argument1": 6,  // ← INCREASE THIS for more extreme terrain
  "argument2": { ... }
}
```

**Values:**
- `4` = Gentle rolling hills
- `6` = Default (current)
- `10` = Extreme mountains
- `15` = Sky-high peaks

### 3. Biome Placement

**File:** `data/vastworlds/worldgen/multi_noise_biome_source_parameter_list/vast_biomes.json`

**6 Climate Parameters:**
```json
"parameters": {
  "temperature": [-1.0, 1.0],     // Cold to hot
  "humidity": [-1.0, 1.0],        // Dry to wet
  "continentalness": [-1.0, 1.0], // Ocean to inland
  "erosion": [-1.0, 1.0],         // Flat to eroded
  "weirdness": [-1.0, 1.0],       // Normal to strange
  "offset": 0.0                    // Priority (higher = more common)
}
```

**Example - Make Mega Mountains MORE Common:**
```json
{
  "biome": "vastworlds:mega_mountains",
  "parameters": {
    "temperature": [-1.0, 0.0],      // Expand range
    "humidity": [-0.5, 1.0],         // Expand range
    "continentalness": [0.0, 1.0],   // More inland
    "erosion": [-1.0, 0.2],          // More eroded
    "weirdness": [-1.0, 1.0],        // Any weirdness
    "offset": 0.5                     // Higher priority
  }
}
```

### 4. Surface Blocks

**File:** `vast_overworld.json` → `surface_rule` section

**Current Rules:**
```json
"surface_rule": {
  "type": "minecraft:sequence",
  "sequence": [
    {
      "if_true": { "type": "minecraft:biome", "biome_is": ["vastworlds:mega_mountains"] },
      "then_run": { "result_state": { "Name": "minecraft:snow_block" } }
    }
  ]
}
```

**Add Custom Surfaces:**
```json
{
  "if_true": {
    "type": "minecraft:above_preliminary_surface"
  },
  "then_run": {
    "type": "minecraft:condition",
    "if_true": {
      "type": "minecraft:stone_depth",
      "offset": 0,
      "surface_type": "floor",
      "add_surface_depth": false,
      "secondary_depth_range": 0
    },
    "then_run": {
      "result_state": { "Name": "minecraft:packed_ice" }
    }
  }
}
```

### 5. Canyon and Cave Modifications

**File:** `data/vastworlds/worldgen/configured_carver/vast_canyon.json`

```json
{
  "type": "minecraft:canyon",
  "config": {
    "probability": 0.03,  // ← Increase for more canyons (max 1.0)
    "y": {
      "min_inclusive": { "absolute": 10 },    // Depth
      "max_inclusive": { "absolute": 120 }    // Height
    },
    "yScale": 4.0,  // ← Vertical stretch (higher = taller canyons)
    "horizontal_radius_factor": {
      "value": {
        "min_inclusive": 0.75,
        "max_exclusive": 2.0  // ← Increase for wider canyons
      }
    }
  }
}
```

---

## Advanced Modifications

### Creating Floating Islands

**Add to density function:**
```json
{
  "type": "minecraft:add",
  "argument1": {
    "type": "minecraft:mul",
    "argument1": {
      "type": "minecraft:noise",
      "noise": "minecraft:jagged",
      "xz_scale": 0.05,
      "y_scale": 0.1
    },
    "argument2": {
      "type": "minecraft:y_clamped_gradient",
      "from_y": 100,
      "to_y": 150,
      "from_value": 1.0,
      "to_value": -1.0
    }
  },
  "argument2": "existing_density_function"
}
```

### Extreme Vertical Variation

**Modify in `noise_router`:**
```json
"final_density": {
  "type": "minecraft:squeeze",
  "argument": {
    "type": "minecraft:mul",
    "argument1": 0.8,  // ← Increase to 1.2 for more variation
    "argument2": { ... }
  }
}
```

### Custom Ore Distribution

**Create:** `data/vastworlds/worldgen/placed_feature/custom_ore.json`

```json
{
  "feature": "minecraft:ore_diamond",
  "placement": [
    {
      "type": "minecraft:count",
      "count": 20
    },
    {
      "type": "minecraft:height_range",
      "height": {
        "type": "minecraft:uniform",
        "min_inclusive": { "absolute": -64 },
        "max_inclusive": { "absolute": 200 }
      }
    }
  ]
}
```

---

## 🎯 Quick Modification Recipes

### Recipe 1: Extreme Mountains (500+ blocks high)
```
1. Set noise.height = 512
2. Set amplification multiplier = 15
3. Set size_vertical = 1 (sharp cliffs)
4. Expand mega_mountains biome parameters
```

### Recipe 2: Massive Flat Plains
```
1. Set erosion = [0.8, 1.0] for plains
2. Set amplification multiplier = 2
3. Set size_horizontal = 4 (very smooth)
```

### Recipe 3: Underground Empire (huge caves)
```
1. Set cave probability = 0.8
2. Set horizontal_radius_factor max = 5.0
3. Set yScale = 8.0
4. Set depth range = -64 to 100
```

### Recipe 4: Sky Islands World
```
1. Add floating island density function
2. Set y_clamped_gradient from 150 to 300
3. Remove surface blocks at y < 100
4. Add void below y=50
```

---

## 📖 Using Yarn Mapped Sources in IDE

**In IntelliJ IDEA:**
1. Ctrl+Click (Cmd+Click on Mac) on any Minecraft class
2. IDE opens decompiled source with Yarn names
3. Example classes to explore:
   - `NoiseChunkGenerator.java`
   - `DensityFunction.java`
   - `MultiNoiseBiomeSource.java`
   - `NoiseRouter.java`

**Key Methods to Study:**
- `NoiseChunkGenerator.populateNoise()` - Main terrain generation
- `DensityFunctions.ap ply()` - How density functions work
- `Climate.findBiome()` - Biome selection logic

---

## 🔧 Testing Your Changes

```bash
# 1. Build the mod
./gradlew build

# 2. Run Minecraft
./gradlew runClient

# 3. Create new world with seed: 0
#    (Always test with same seed for consistency)

# 4. Use F3+G to see chunk boundaries
# 5. Use F3 to see biome and coordinates
```

---

## 📝 Pro Tips

1. **Always backup** your JSON files before major changes
2. **Start small** - modify one parameter at a time
3. **Use consistent seeds** for testing
4. **Check logs** - Minecraft logs errors in JSON files
5. **Reference vanilla** - Look at `minecraft:overworld` in IDE
6. **Combine techniques** - Mix multiple modifications
7. **Test performance** - Extreme values can lag generation

---

## 🚀 Next Steps

1. Study vanilla `overworld` noise settings
2. Experiment with density function types
3. Create custom noise types
4. Add custom features (trees, structures)
5. Implement custom chunk generator (Java code)

---

Generated for Vast Worlds Mod - Minecraft 1.21.1 with Yarn Mappings
