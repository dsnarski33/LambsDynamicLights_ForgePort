# LambDynamicLights 1.20.1 Mod - Forge Port

This is a port from Lambs Dynamic Lights mod, see:
- https://www.curseforge.com/minecraft/mc-mods/lambdynamiclights
- https://github.com/LambdAurora/LambDynamicLights

This was ported for use with TerraFirmaCraft[^1] and working on my own additions to it,
where I wanted a playback involving dynamic lights. I removed the menus and only support
a config file. I also added support for an ItemStack tag lighting mode. This tag is
expected to be on the ItemStack as an integer in the 0 to 15 range. It is modeled
after the BlockItem mode in the json setup, where instead you would use:
```
"luminance": "IntensityDynamic"
```

[^1]: See: see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft

---
>`Proverbs 3:5-6` Trust in the LORD with all your heart,
and lean not on your own understanding;
in all your ways acknowledge Him,
and He will make your paths straight.