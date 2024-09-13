# Explodee

Explodee changes the Minecraft explosion code to propagate in a spherical formation instead of a cubic formation. This results in less raycasts being used for smaller explosions *(TNT uses 100 raycasts instead of ~1000)*.

**Note:** Explodee explosions do not work __exactly__ like vanilla explosions (i.e. there is a possibility that some neighboring TNT may not ignite if it is 3 blocks away). They are also visually very different, especially at larger explosion radii.

Explodee works with Lithium (taking advantage of its explosion optimizations), and will behave nearly identically to in vanilla Explodee.
