ULI-KENNWORT
============

Einleitung
----------

Diese Woche bin ich über ein Kennwort-Problem gestolpert. Wir haben eine Anwendung, die
diverse Aufrufparameter erwartet. Gestartet wird sie also bspw. so:

```sh
java -cp myApp.jar com.itpros.FileTransfer ftpserver ftpUser ftpPassword
```

Grundsätzlich funktioniert alles prima. Die Anwendung startet und überträgt wie gewünscht eine
Menge Dateien via FTP. Leider gibt es hierbei ein Problem mit der Geheimhaltung des Kennwortes:
Jeder Benutzer, der Zugang zum Rechner hat, auf dem die Anwendung läuft, kann sich die Prozessliste
anzeigen lassen und sieht hier den Namen des FTP-Benutzers und das zugehörige FTP-Kennwort.

Gesucht sind nun Verfahren, die dies unterbinden. Dies soll erfolgen, ohne dass die Anwendung
geändert werden muß!

Testprogramm
------------

Hier in diesem Projekt ist ein Testprogramm enthalten, welches grob die Rolle der Anwendung übernimmt.
Es wird kompiliert und verpackt über `ant`.

* Klassenname: com.secretservice.SecretApp
* Jar-Datei: lib/secretApp.jar
* Aufruf:
** `java -cp lib/secretApp.jar com.secretservice.SecretApp ftpserver uli kennwort`
** `java -jar lib/secretApp.jar ftpserver uli kennwort`
** `./secretApp-0.1.sh ftpserver uli kennwort`

Das Testprogramm gibt seine Aufrufparameter in der Konsole aus und wartet dann 60 Sekunden bevor es
sich beendet. Wenn man während der Wartezeit die Prozessliste anzeigen lässt, sieht man Einträge wie diesen:

```sh
$ ps waux
...
uli      15853  1.5  0.1 3204452 14260 pts/0   Sl+  08:51   0:00 java -cp lib/secretApp.jar com.secretservice.SecretApp ftpserver uli kennwort
...
```

Man sieht: Das Kennwort erscheint im Klartext in der Prozessliste!

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
