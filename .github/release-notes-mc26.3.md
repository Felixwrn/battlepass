WRN BattlePass 1.0 for Paper 26.3 and Java 25.

Install `wrnpass-1.0.jar` in the server's `plugins` directory and restart with Java 25. `/bp` opens the BattlePass; `/bp setup` opens the existing reward configuration. No additional plugin dependency is declared.

The release workflow builds the tagged source with `mvn clean verify` and publishes the final JAR with `SHA256SUMS`. The repository has no automated tests. In-game behavior has not been tested; verify XP, rewards and menus on a separate server before production use.
