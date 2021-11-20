#language: de

Funktionalität: Bands hinzufügen

  Szenario: Eine Band zu bestehendem Festival hinzufügen.
    Gegeben sei ein Festival
    Wenn eine Band zu dem Festival hinzugefügt wird
    Dann wurde die Band zum Festival hinzugefügt.

  Szenario: Mehrere Bands zu bestehendem Festival hinzufügen.
    Gegeben sei ein Festival
    Wenn mehrere Bands zu dem Festival hinzugefügt werden
    Dann wurden die Bands zum Festival hinzugefügt.

  Szenario: Die selbe Band wird mehrmals zum Festival hinzugefügt.
    Gegeben sei ein Festival mit der Band KISS
    Wenn die Band KISS hinzugefügt wird
    Dann wurde die Band nicht erneut hinzugefügt.