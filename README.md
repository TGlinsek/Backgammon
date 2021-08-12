# Backgammon
Igra backgammon za projekt pri predmetu Programiranje 2.

## Opis igre
To je igra za dva igralca. Eden ima črne **figure** (tudi **žeton**), drugi bele.
Plošča je razdeljena na 24 trikotnikov, ki so razdeljeni v 4 četrtine/predele, vsak po 6 trikotnikov.
Na sredini plošče je t. i. **bariera**, kjer se zbirajo zbite figure.
Na koncu plošče sta dva prostorčka, ki predstavljata cilj za vsakega igralca posebej. Tista četrtina plošče, ki se nahaja tik pred ciljem določenega igralca, se imenuje **hiša** tega igralca.

## Pravila igre

### Žreb igralca
Začetnega igralca se določi z metanjem kock. Vsak igralec vrže kocko, zmaga pa tisti, ki vrže višjo vrednost. Če sta vrednosti enaki, se metanje ponovi.

### Metanje kock
Ko je igralec na vrsti, vrže dve kocki. Ti dve vrednosti predstavljata dve potezi, ki ju mora igralec opraviti, razen če to ni mogoče. V primeru, da sta vrednosti na obeh kockah enaki, se število potez, ki jih mora igralec opraviti, podvoji, kar pomeni, da mora opraviti štiri poteze (z isto vrednostjo).

### Premikanje potez
Vsak igralec ima svojo določeno smer premikanja figur. Če želimo opraviti potezo, moramo svojo figuro premakniti za toliko mest naprej, kolikor je vrednost na kocki, ki smo jo vrgli. Kot prvo potezo si lahko izberemo poljubno izmed obeh vrednosti, ki smo ju dobili s kockami, toda z drugo potezo bomo morali uporabiti še drugo kocko.
Figuro lahko premaknemo na:
1. prazen trikotnik,
2. trikotnik, ki že vsebuje kakšno našo figuro,
3. trikotnik, ki vsebuje natanko eno nasprotnikovo figuro.
V slednjem primeru nasprotnikovo figuro zbijemo in jo pošljemo nazaj na začetek oz. na bariero (tam se namreč zbirajo zbite figure).
Če trikotnik vsebuje več kot eno nasprotnikovo figuro, potem tja naše figure ne moremo postaviti.
Če imamo kako figuro na začetku (tj. na barieri), potem moramo najprej to spraviti nazaj na ploščo, preden lahko premikamo ostale figure.
Pri izbiranju potez velja še pravilo, da moramo opraviti čim več potez. Tj., če imamo na voljo dve potezi, potem ne moremo najprej opraviti take poteze, ki bi nam onemogočila opravljanje še druge poteze, razen če pač ne obstaja taka prva poteza, ki pa bi nam omogočila opravljanje še druge poteze.

### Konec igre
Cilj igre je spraviti vse naše figure na cilj, ki se nahaja na koncu plošče. Toda figur ne smemo spravljati na cilj, če niso vse naše figure v naši hiši, tj. zadnji četrtini pred ciljem. Tudi če so v nekem trenutku igre vsi naši žetoni že v hiši, ampak potem katerega izmed naših žetonov nasprotnik zbije, moramo najprej spet vrniti zbito figuro nazaj v hišo, preden lahko ostale figure spravimo na cilj.
Za spravljanje na cilj, potem ko so vsi naši žetoni že v hiši, veljajo naslednja pravila:
1. Če obstaja žeton v hiši, ki bi ga z določeno potezo lahko spravili direktno na cilj (tj., vrednost na kocki je enaka oddaljenosti žetona od cilja), potem lahko vedno to storimo.
2. Če obstaja poteza, ki ne premakne žetona na cilj, potem lahko tako potezo vedno opravimo.
3. Če za določeno vrednost kocke obstajajo žetoni, ki so vsaj toliko oddaljeni od cilja, kot je vrednost na kocki, potem za to vrednost ne moremo premakniti žetonov, ki so od cilja oddaljeni za manj kot je vrednost na kocki.
4. Če pa takih žetonov ni (takih, ki so vsaj toliko oddaljeni od cilja, kot je vrednost na kocki), potem pa moramo premakniti žeton, ki je najbolj oddaljen od cilja.
