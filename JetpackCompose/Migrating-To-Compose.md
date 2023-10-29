### Migrating From View/XML to Compose:
The recommendation is - an `incremental migration` where Compose and View co-exist in same codebase until the app is fully in Compose.

### Migration Strategy:
1. Build New Screen in Compose: 
View and Compose can co-exists on the same screen. Ex. If there is a existing `RecyclerView`, a Composable can be added there while keeping the other item same. Then migrate slowly.
2. Build Library of common UI components:
identify reusable components to promote reuse across the app so that shared components have a single source of truth. 
3. Replace existing features one screen at a time:
- Simple Screen: This is the easiest part.
- Mixed View and Compose: Start migrating elements mixed screen piece-by-piece. If there is a screen with only a `subtree` in Compose, continue migrating other parts of the `tree` until the entire UI is in Compose. This is called the `bottom-up` approach of migration.