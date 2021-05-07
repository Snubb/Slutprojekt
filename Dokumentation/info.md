Här sprar du alla dokumentationsfiler. Skisser på Gui, grafik, flödesdiagram

Idé:
En form av singleplayer version av battleship.
![Battleship_example](https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/Battleship_game_board.svg/800px-Battleship_game_board.svg.png)
(bilden tagen från wikipedia)  
Spelet går ut på att helt enkelt skjuta ner alla skepp.  
Skeppen är från början osynliga och delar av ett skepp blir synligt när man träffar den.  
Spelet är gjort i en grid och man har en specifik mängd med skott innan man förlorar.  
På kanten får man se mängden skepp kvar, hur stora/långa dom är, och ifall man sänkt dom.  

Planering:
Börja med att skapa själva griden som man spelar på. Kanske dela in allting i rektanglar som båtarna ligger i.  
Tanken är att själva griden bara är en stor fyrkant sedan har jag en lista med alla individuella fyrkanter.  
När man "skjuter" på en fyrkant kommer spelet kolla musens nuvarande position och kolla om en båt ligger i det området.

EDIT 2021-05-07:  
Skrapade musen, nu används wasd istället. hasBoat variabel inuti fyrkanterna visar om det finns en båt eller inte.  

Mer planering:  
När man skjuter kollar spelet om den fyrkanten man är i har en båt. Om det har en så träffar man. Om den inte har en så visas det med ett kryss.  
Man kommer även ha ett begränsat nummer av skott så att man kan förlora.  
I slutändan kan jag kopiera det mesta och göra spelet till ett 2-player spel.
