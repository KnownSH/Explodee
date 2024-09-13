# Explodee

Explodee changes the Minecraft explosion code to propogate in a spherical formation instead of a cubic formation. This ends up leading to fewer raycasts being used on smaller explosions *(TNT uses 100 raycasts instead of ~1000)*.

**Note:** Explodee explosions do not work __exactly__ like vanilla explosions, (i.e. there is a possibility that some neighbouring TNT may not be ignited if its 3 blocks away)
