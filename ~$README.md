# Avansert Java PGR200
# Mappeinnlevering 13.11.2018

# Navn: Mohammad Usman Mughal
# Feide-ID: mughmoh15

# Hvordan oppgaven er bygget?
I denne videoen vil jeg forklare, og vise hvordan oppgaven er bygget, hvordan jeg kjører programmet med ulike spørringer og hvordan jeg tester.

# Hvordan teste programmet?
Funksjoner som jeg har med er:
•	Hente ut alle events
•	Legge til en ny event
•	Oppdatere en event

1.	Start serveren:
  •	Gå inn på Terminalen
  •	Gå til undermappen til «server/target/»
  •	Skriv dette i terminalen:
    o	java -jar server-1.0-SNAPSHOT.jar
  •	Serveren er startet

2.	Klient:
  •	Gå inn på Terminalen
  •	Gå til undermappen til «httpclient/target/»
  •	For å kunne liste opp alle events
    o	java -jar client-1.0-SNAPSHOT.jar list
  •	For å kunne legge til ny event
    o	java -jar client-1.0-SNAPSHOT.jar add -Title «Her skriver du inn tittel» -Description «Her skriver du inn beskrivelse» -  Topic «Her skriver du inn emne»
    o	Etter at du har kjørt denne linjen, vil terminalen vise ID´en til eventet.
  •	For å vise et event ved å søke via ID
    o	java -jar client-1.0-SNAPSHOT.jar show -id 1
  •	For å kunne oppdatere et event (Du kan også oppdatere hver enkelt felt, om du vil kun oppdatere tittel, skriver du inn kun tittel feltet, osv)
    o	Java -jar client-1.0-SNAPSHOT.jar update -id 1 -title «Ny tittel» -description «ny beskrivelse» -topic «ny emne»

# Egenevaluering:
Jeg er veldig fornøyd med det jeg har klart å få til på den siste uken. Jeg har jobbet alene, da jeg allerede er i fulltidsjobb, og har ikke hatt mulighet til å være med på forumet og finne meg en gruppe til tide. Jeg har heller ikke hatt mulighet til å evaluere en annen gruppes innlevering, da jeg har sittet med denne oppgaven 24/7 i tillegg til arbeid ifra jobben. Det har vært både hektisk og lærerikt den siste uken. 

Jeg har fått til å 
