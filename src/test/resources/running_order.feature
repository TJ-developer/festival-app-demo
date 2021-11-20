#language: de

Funktionalität: Running-Order erstellen
  Als Festivalbetreiber
  möchte ich zu Bands Spielzeiten und Bühnen hinzufügen,
  um einen Spielablauf zu planen.

  Grundlage:
    Gegeben sei ein Festival
    Und die Bühne Main Stage
    Und die Bühne Side Stage

  Szenario: Metallica darf nur im Dunkeln spielen.
    Gegeben sei die Band Metallica
    Wenn Metallica die Spielzeit von 21 Uhr bis 22:30 Uhr zugeordnet wird
    Dann wurde die Spielzeit erfolgreich zugeordnet.

  Szenario: Metallica darf nur im Dunkeln spielen.
    Gegeben sei die Band Metallica
    Wenn Metallica die Spielzeit von 16 Uhr bis 17:30 Uhr zugeordnet wird
    Dann wurde die Spielzeit nicht zugeordnet.