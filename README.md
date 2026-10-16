# WRN BattlePass

Minecraft-Plugin für Paper 26.3 mit Java 25. Die Paper-API ist auf
`26.3.build.200-stable` festgelegt.

Zum Bauen werden JDK 25 und Maven 3.9.11 oder neuer benötigt:

```sh
mvn --batch-mode --no-transfer-progress clean verify
```

Die Plugin-Datei `target/wrnpass-1.0.jar` in das `plugins`-Verzeichnis eines
Paper-26.3-Servers kopieren und den Server mit Java 25 starten. `/bp` öffnet den
BattlePass; `/bp setup` öffnet die vorhandene Einrichtung der Belohnungen.
Für dieses Repository sind keine automatisierten Tests vorhanden; vor dem
produktiven Einsatz XP, Belohnungen und Menüs auf einem Testserver prüfen.
