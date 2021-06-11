# Titel

Oliver Lundqvist  
2021-06-01

## Inledning

Här beskriver du kortfattat arbetets syfte/mål, arbetssätt, genomförande.

Syftet och målet med detta arbete var att, från början, designa och planera något 
typ av projekt som använder sig av alla de komponenter vi jobbat med sedan tidigare.  
Planeringen skedde simpelt nog genom text och bilder och själva koden skrevs 100% i java genom Intellij.  
I mitt fall så var planen att göra en egen kopia av spelet "Battleship". Från början hade
jag bara tänkt göra en singeplayer version men insåg senare att det både borde vara ganska 
simpelt och att jag hade gått om tid så det blev både en singleplayer och 1v1 version.

## Bakgrund

Redovisa arbetets olika delar. Så att läsaren förstår vad du gjort och hur.

Använd gärna bilder för att illustrera.

För att lägga till bilder i markdown. Bilderna kan du ladda upp med Git som vanligt, länka dem med url eller filnamnet.

```
![GitHub Logo](/images/logo.png)
Format: ![Alt Text](url)
```

![NTI Gymnasiet Umeå Logo](https://raw.githubusercontent.com/jensnti/Webbprojekt/master/mallar/nti_logo_white_umea.svg)

### Planering

Som sagt så gjordes allt genom bilder och text. Det var inte någon grov planering utan 
mer grundläggande saker såsom hur jag ville att spelet ska fungera och vad som ska hända när.

### Genomförande
#### Spelplanen och båtarna
Som i ett vanligt spel av Battleships var spelplanen uppbyggd av en grid-layout, i det här fallet 8x8.  
--Picture here--  
Alla rektanglar gjordes inom en array uppgjord av en 'gridSpace' klass som jag skapat. 
Jag skapade klassen för att senare kunna bestämma specifikationer om rektanglarna, 
såsom ifall en båt bör finnas där eller om spelaren har skjutit på den rektangeln. 
 
Båtarna skapades genom att slumpmässigt välja en rektangel och sedan skapa en båt i den 
rektangeln och 1-3 till båtar åt ett slumpmässigt håll beroende på båtens längd.  
En check gick igenom för att se till att inga båtar overlappar eller hamnar utanför spelplanen 
och ifall det händer så görs processen om tills att en 'giltig' båt kan skapas.

#### Att skjuta
Siktet skapades som en rektangel med positionen av de olika rektanglarna på spelplanen. 
Att siktet använde samma koordinater ser till att siktet alltid ör ovanför specifikt en 
rektangel och inte flera. Från början ändrade man position med wasd, då ändrades 
helt enkelt värdet på playerPos för att stämma med uppåt, neråt, höger eller vänster.  
Senare implementerade jag även musen, den fungerar genom att konstant flytta en rektangel 
på musens position och siktet flyttas till den rektangel på spelplanen som överskrider 
musrektangeln. 

Metoden shoot kollar helt enkelt om rektangeln som siktet är på redan har blivit skjuten 
eller inte och skjuter beroende på svaret. I singleplayer versionen kollar den även 
så att man har nog med skott kvar.

#### Att rita ut allt
Efter man skjutit så måste spelet ge någon feedback över vad man träffade. 
Den feedbacken gavs i form av ett par variabler i gridSpace klassen.
En variabel bestämde om det var en båt, om det inte var en båt och rektangeln blivit 
beskjuten så ritas ett kryss, om det var en båt ritas en explosion.
En variabel som bestämde om rektangeln blivit skjuten bestämde även om någonting 
skulle ritas över rektangeln till att börja med.

## Positiva erfarenheter

Här beskriver du vad som har gått bra i ditt projekt och analyserar varför. Hur ska du upprepa framgångarna.

Jag skulle säga att nästan allt gick väldigt bra, jag har löst det mesta som jag ville i programmet och
det som inte blivit löst är mest för tidsbrist. Samtidigt är battleships inte alldelles för komplicerat 
vilket förmodligen bidrar till varför det gick så bra som det gjorde.


## Negativa erfarenheter

Här beskriver du det som du anser har gått mindre bra med ditt projekt och analyserar hur du kan undvika detta i framtida projekt.

Som sagt gick inte någonting alldelles för dåligt.  
Det enda som gick "dåligt" är att vissa saker skulle kunna lösas på ett bättre sätt, specifikt två saker.
Först så hade jag metoden "creatBoats" som skapade båtarna och såg till att dom inte overlappade eller gick 
utanför banan. Metoden fungerar precis som den ska men den är nog alldelles för lång och överkomplicerad.
Jag kan tänka mig lite bättre sätt, t.ex att göra hela båten som en lång rektangel och sedan kolla intersect 
mellant dom men det jag har nu fungerar och jag orkar inte skriva om hela saken.

Den andra grejen är hur jag fixade till titlescreenen. Just nu har jag tre separata java program, en för 
titlescreen, en för 1v1 och en för singeplayer och allting körs igenom titlescreen genom att helt enkelt sätta 
frame.setVisible till false eller true beroende på vad som ska köras. Det skulle nog absolut gå att köra 
allting från en och samma java fil men då skulle jag behöva skriva om stora delar av den. 
Ett sätt att undvika det är att helt enkelt planera bättre. Nu så skrev och först bara singleplayer 
och skrev titlescreenen och 1v1 som en eftertanke men om jag planerade alltihop från första början skulle 
det nog blivit bättre.

En annan sak som gick mindre bra är hur stabilt/laggfritt spelet är. Det största problemet 
var gällande båtbilderna som visar vilka som sprängts. Eftersom spelet ritar om allting 
varje frame börjar det lagga när jag har stora bilder. Jag löste det först genom att göra 
så att bilderna specifikt bara ritades när de behövdes men då dog spelet på min pc men 
fungerade fint på laptop. I slutändan så tog jag smällen och lät det fungera på båda 
enheterna men med smått lagg på laptopen.
Det här skulle kanske fungera fint om jag skrivit om programmet på det nya sättet som 
du visade med bättre prestanda och fullscreen.

## Sammanfattning

Här redovisar du dina slutsatser, erfarenheter och lärdomar. Reflektera över din produkt och dess/dina utvecklingsmöjligheter.
Vad kan vidareutvecklas och finns det utrymme att bygga vidare på projektet.