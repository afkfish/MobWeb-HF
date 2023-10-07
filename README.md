# Házi feladat specifikáció

Információk [itt](https://viauac00.github.io/laborok/hf)

## Mobil- és webes szoftverek
### 2023.10.07.
### CinemaDB
### Kis Benedek Márton - (JOYAXJ)
### school@afkfish.com
### Laborvezető: Rigó Péter

## Bemutatás

Ez az alkalmazás egy mozi és-vagy film kereső. Egy adatbázisból egy REST-apin keresztül letölti a most 
játszott filmeket magyarországon és ez alapján lehet mozit keresni a közelben. Az ötlet az IMDB-ről jött,
ahol már van egy ilyen funkció de sajnos nem elérhető Magyarországon. A célközönség azok, akik szeretnének
moziba menni, de nem tudják, hogy milyen filmeket játszanak a közelben.

## Főbb funkciók

Filmek keresése egy előre lekérdezett adatbázisból. Az adatok egy saját REST apin keresztül érhetőek el.
A filmeket egy RecyclerView-ban jelenítem meg, amiket egy keresővel lehet szűrni. A filmekre kattintva
megjelenik egy részletes nézet, ahol a film plakátja, címe, leírása, stb. látható.

Mozi keresése a közelben. A mozikat egy Google Maps API-val jelenítem meg. A mozikat is a saját REST apin
keresztül érem el.

A filmekre jegy foglalására is van lehetőség. A film azonosítója alapján egy új böngésző ablakban megnyílik
a Cinema City oldala, ahol a jegyeket lehet megvásárolni.

## Választott technológiák:

- UI
- fragmentek
- RecyclerView
- hálózati kommunikáció