ULI-KENNWORT
============

Einleitung
----------

Diese Woche bin ich über ein Kennwort-Problem gestolpert. Wir haben eine Anwendung, die
diverse Aufrufparameter erwartet. Gestartet wird sie also bspw. so:

```sh
java -cp myApp.jar com.itpros.FileTransfer ftpUser ftpPassword
```

Grundsätzlich funktioniert alles prima. Die Anwendung startet und überträgt wie gewünscht eine
Menge Dateien via FTP. Leider gibt es hierbei ein Problem mit der Geheimhaltung des Kennwortes:
Jeder Benutzer, der Zugang zum Rechner hat, auf dem die Anwendung läuft, kann sich die Prozessliste
anzeigen lassen und sieht hier den Namen des FTP-Benutzers und das zugehörige FTP-Kennwort.

Gesucht sind nun Verfahren, die dies unterbinden. Dies soll erfolgen, ohne dass die Anwendung
geändert werden muß!

Git
---

Gedächtnisstütze für mich:

```sh
git init uli-kennwort
cd uli-kennwort
...
git commit -m "was-auch-immer" .
# Neues Repo auf GitHub angelegt
git remote add origin git@github.com:uli-heller/uli-kennwort.git
git push -u origin master
```
