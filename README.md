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
    * `java -cp lib/secretApp.jar com.secretservice.SecretApp ftpserver uli kennwort`
    * `java -jar lib/secretApp.jar ftpserver uli kennwort`
    * `./secretApp-0.1.sh ftpserver uli kennwort`

Das Testprogramm gibt seine Aufrufparameter in der Konsole aus und wartet dann 60 Sekunden bevor es
sich beendet. Seine Ausgabe sieht bspw. so aus:

```sh
$ java -cp lib/secretApp.jar com.secretservice.SecretApp ftpserver uli kennwort`

My Secret Application
---------------------

arg[0]='ftpserver'
arg[1]='uli'
arg[2]='kennwort'
```

Wenn man während der Wartezeit die Prozessliste anzeigen lässt, sieht man Einträge wie diesen:

```sh
$ ps waux
...
uli      15853  1.5  0.1 3204452 14260 pts/0   Sl+  08:51   0:00 java -cp lib/secretApp.jar com.secretservice.SecretApp ftpserver uli kennwort
...
```

Man sieht: Das Kennwort erscheint im Klartext in der Prozessliste!

Verschlüsselungsprogramm
------------------------

Zunächst wird eine kleine Klasse geschrieben, die Zeichenketten ver- und entschlüsseln kann.

* Klassenname: org.wrapper.Crypton
* Jar-Datei: lib/encrypt.jar
* Aufruf:
    * `java -cp lib/encrypt.jar org.wrapper.Crypton  ftpserver uli kennwort`
    * `java -jar lib/enrypt.jar ftpserver uli kennwort`
    * `./encrypt-0.1.sh ftpserver uli kennwort`

Das Verschlüsselungsprogramm liefert Ausgaben wie diese:

```sh
$ java -cp lib/encrypt.jar org.wrapper.Crypton  ftpserver uli kennwort
ftpserver -> 3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4=
uli -> TSR64jmzcmD2OB3ob8AMRQ==
kennwort -> sd5AMTvWrO6eMmIsp7NHtWC174JXd6WwT4Z0bKFraDw=
```

Aufrufhülle
-----------

Nun schreiben wir eine zweite Klasse, die die verschlüsselten Zeichenketten entgegennimmt.
Die Zeichenketten werden entschlüsselt und an das unveränderte Anwendungsprogramm weitergereicht.

* Klassenname: org.wrapper.WrappedApp
* Jar-Datei: lib/wrappedSecretApp.jar
* Aufruf:
    * `java -cp lib/wrappedSecretApp.jar org.wrapper.WrappedApp  com.secretservice.SecretApp '3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4=' ...`
    * `java -jar lib/wrappedSecretApp.jar  com.secretservice.SecretApp '3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4=' ...`
    * `./wrappedApp-0.1.sh  com.secretservice.SecretApp '3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4=' ...`

Hier nun der Aufruf des unveränderten Testprogramms über die Aufrufhülle:

```sh
$ java -cp lib/wrappedSecretApp.jar org.wrapper.WrappedApp com.secretservice.SecretApp \
  '3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4=' \
  'TSR64jmzcmD2OB3ob8AMRQ==' \
  'sd5AMTvWrO6eMmIsp7NHtWC174JXd6WwT4Z0bKFraDw='

 ...`
```

Die Prozessliste sieht so aus:

```sh
$ ps waux
...
uli      16672  3.4  0.3 3211112 31144 pts/0   Sl+  09:24   0:00 java -cp lib/wrappedSecretApp.jar org.wrapper.WrappedApp com.secretservice.SecretApp 3FwSB8kJfD5saH41gp+I28Iflc82GdMjVEJsrv5fCl4= TSR64jmzcmD2OB3ob8AMRQ== sd5AMTvWrO6eMmIsp7NHtWC174JXd6WwT4Z0bKFraDw=
...
```

Man erkennt: Die Aufrufparameter erscheinen verschlüsselt in der Prozessliste.

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
