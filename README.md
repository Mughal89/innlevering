# Avansert Java PGR200
# Mappeinnlevering 13.11.2018

## Navn: Mohammad Usman Mughal
## Feide-ID: mughmoh15

### Hvordan oppgaven er bygget?
I denne videoen vil jeg forklare, og vise hvordan oppgaven er bygget, hvordan jeg kjører programmet med ulike spørringer og hvordan jeg tester.

LINK ->

### Hvordan teste programmet?
Funksjoner som jeg har med er:

  •	Hente ut alle talks

  •	Legge til en ny talk

  •	Oppdatere en talk

  •	Hente ut en spesifikk talk

Oppgaven antar at databasemodellen ser sånn ut:

![bilde1](https://user-images.githubusercontent.com/31854673/48369924-2ee60e80-e6b8-11e8-984f-9d70d29fbddd.png)

UML diagram:

![uml](https://user-images.githubusercontent.com/31854673/48370797-a321b180-e6ba-11e8-9128-8aff9526ee3b.png)


1.	Start serveren:

  •	Gå inn på Terminalen

  •	Gå til undermappen til «server/»

  •	Kjør «mvn package» (Vent til Maven viser Build Sucess)

  •	Gå til undermappen til «server/target/»

  •	Åpne innlevering.properties i din teksteditor, og legg til din brukernavn og passord samt navnet på tabellen og url til databasen

  •	Skriv dette i terminalen:

    o	java -jar server-1.0-SNAPSHOT.jar

  •	Serveren er startet


2.	Klient:

  •	Gå inn på Terminalen

  •	Gå til undermappen til «httpclient/»

  •	Kjør «mvn package» (Vent til Maven viser Build Sucess)

  •	Gå til undermappen til «httpclient/target/»

  •	For å kunne liste opp alle talks
    o	java -jar client-1.0-SNAPSHOT.jar list

  •	For å kunne legge til ny talk
    
    o	java -jar client-1.0-SNAPSHOT.jar add -Title «Her skriver du inn tittel» -Description «Her skriver du inn beskrivelse» -Topic «Her skriver du inn emne»

  • Etter at du har kjørt denne linjen, vil terminalen vise ressurs URL´en til talk

  •	For å vise et talk ved å søke via ID
    
    o	java -jar client-1.0-SNAPSHOT.jar show -id 1

  •	For å kunne oppdatere et talk (Du kan også oppdatere hver enkelt felt, om du vil kun oppdatere tittel, skriver du inn kun tittel feltet, osv)

    o	java -jar client-1.0-SNAPSHOT.jar update -id 1 -title «Ny tittel» -description «ny beskrivelse» -topic «ny emne»

### Egenevaluering:
Jeg er veldig fornøyd med det jeg har klart å få til på den siste uken. Jeg har jobbet alene, da jeg allerede er i fulltidsjobb, og har ikke hatt mulighet til å være med på forumet og finne meg en gruppe til tide. Jeg har heller ikke hatt mulighet til å evaluere en annen gruppes innlevering, da jeg har sittet med denne oppgaven 24/7 i tillegg til arbeid ifra jobben. Det har vært både hektisk og lærerikt den siste uken. 

Jeg har fått til å 
