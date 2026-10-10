# Explodee Revamped

Explodee changes the Minecraft explosion code to propagate in a spherical formation instead of a cubic formation. This results in less raycasts being used for smaller explosions *(TNT uses 100 raycasts instead of ~1000)*.

Explodee can work alongside Lithium on Fabric and NeoForge (taking advantage of its explosion optimizations), and will behave nearly identically to vanilla Explodee.

**Note:** Explodee explosions do not work __exactly__ like vanilla explosions (i.e. there is a possibility that some neighboring TNT may not ignite if it is 3 blocks away). They are also visually very different, especially at larger explosion radii.

## Changes
The most important change between the old Explodee codebase and Explodee Revamped is the use of a much more extensive build system. This was done entirely for fun as Explodee is a simple mod that targets code that has barely changed in the past 15 years.

I tried to keep it fairly simple to understand, and I hope it might be useful in your own stonecutter multi-version mods.
