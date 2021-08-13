package logika;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Igra {
	
	public IgralnaPlosca igralnaPlosca;
	public Igralec igralecNaVrsti;
	
	public Kocka kocka1;
	public Kocka kocka2;
	
	
	// npr., če je uporabnik hotel prestaviti figuro, ki je ni možno prestaviti, se bo na zaslonu prikazalo sporočilo, ki bo vsebovalo tale string:
	public String napaka;  // če je to null, ni napake, drugače pa je
	
	private LinkedList<Integer> seznamKock;
	
	public StanjeIgre trenutnoStanje;
	
	private Map<ImeKocke, Kocka> pridobiKocko;  // to bo važno samo, če izbiramo začetnega igralca z metanjem kock
	private Map<ImeKocke, Igralec> igralcevaKocka;  // to bo važno samo, če izbiramo začetnega igralca z metanjem kock
	
	
	// naslednji dve metodi se kličeta samo v konstruktorju, saj samo ustvarita slovar (map)
	private void napolniSlovarPridobiKocko() {
		pridobiKocko = new EnumMap<ImeKocke, Kocka>(ImeKocke.class);
		pridobiKocko.put(ImeKocke.PRVA_KOCKA, kocka1);
		pridobiKocko.put(ImeKocke.DRUGA_KOCKA, kocka2);
	}
	
	private void napolniSlovarIgralcevaKocka() {
		igralcevaKocka = new EnumMap<ImeKocke, Igralec>(ImeKocke.class);
		igralcevaKocka.put(ImeKocke.PRVA_KOCKA, Igralec.BELI);
		igralcevaKocka.put(ImeKocke.DRUGA_KOCKA, Igralec.CRNI);
	}
	// enum map moraš uporabiti tako, da so ključi res enum tipa
	// hash mapa pa tut ne moremo uporabiti na razredu Kocka, saj ta razred ni nespremenljiv (immutable)
	// zato ne moremo direktno narediti slovarja Kocka -> Igralec (oz. vsaj js ne znam naredit), zato imamo dva slovarja
	
	
	public int pridobiVrednostKocke(ImeKocke kocka) {
		return pridobiKocko.get(kocka).vrniVrednost();
	}
	
	public Igralec pridobiLastnikaKocke(ImeKocke kocka) {
		return igralcevaKocka.get(kocka);
	}
	
	public Igra() {
		igralecNaVrsti = Igralec.BELI;  // vedno začne beli
		
		seznamKock = new LinkedList<Integer>();
		
		kocka1 = new Kocka();
		kocka2 = new Kocka();
		
		this.vrziKocki();  // vržemo kocke in jih damo v seznam
		/*
		// kocke smo ravnokar vrgli, zato jih kar damo v seznam 
		seznamKock.add(kocka1.vrniVrednost());
		seznamKock.add(kocka2.vrniVrednost());
		*/
		napolniSlovarIgralcevaKocka();
		napolniSlovarPridobiKocko();
		
		
		igralnaPlosca = new IgralnaPlosca();  // črni gre v smeri urinega kazalca po defaultu
		
		// napaka = null;  // itak bo na začetku null, tako da tega ni treba napisati
		
		trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	
	/*  // default konstruktor, pač če bi igralec želel default nastavitve
	public Igra() {
		this(null, true, true);  // to pomeni, da se igralec določi z metanjem kocke
	}
	*/
	
	
//	!!popravi, da bo vodja dal vrednost kock: TODO
	public void vrziKocki() {
		this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		System.out.println("test");
		seznamKock.clear();
		seznamKock.add(kocka1.vrniVrednost());
		seznamKock.add(kocka2.vrniVrednost());
		
		if (kockiImataEnakoVrednost()) {
			seznamKock.add(kocka1.vrniVrednost());  // samo dvakrat še dodamo kocke, da imamo na koncu štiri enake vrednosti
			seznamKock.add(kocka2.vrniVrednost());
		}
		return;
	}
	
	public int primerjajKocki(ImeKocke prvaKocka, ImeKocke drugaKocka) {  // to morda ne bo uporabno, če bomo itak morali vsako vrednost posebej v vodji dobit
		return Integer.compare(pridobiVrednostKocke(prvaKocka), pridobiVrednostKocke(drugaKocka));
	}
	
	/*
	public void vrziDvojnoKocko() {
		dvojnaKocka.vrziKocko();
		
		this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		
		seznamKock.clear();
		for (int i = 0; i < 4; i++) seznamKock.add(dvojnaKocka.vrniVrednost());
	}
	*/
	
	public Trikotnik pridobiTrikotnik(int mesto) {
		return igralnaPlosca.plosca[mesto];
	}
	
	
	// ali je poteza veljavna?
	public boolean potezaJeVeljavna(Poteza poteza) {
		return this.vrniVeljavnePotezeTePlosce().contains(poteza);
	}
	
	
	// vrne false, če je poteza neveljavna
	public boolean odigraj(Poteza poteza) {  // a naj bo tu boolean al void?
		// if (!potezaJeVeljavna(poteza)) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		boolean potezaJeBilaUspesnoOdigrana = this.igralnaPlosca.igrajPotezo(poteza);
		if (!potezaJeBilaUspesnoOdigrana) return false;
		
		seznamKock.remove((Integer) poteza.premik);  // vrednost kocke, ki smo jo uporabili, odstranimo iz seznama
		
		if (igralnaPlosca.beliImaVseNaCilju()) {
			this.trenutnoStanje = StanjeIgre.ZMAGA_BELI;
			return true;
		} else if (igralnaPlosca.crniImaVseNaCilju()) {
			this.trenutnoStanje = StanjeIgre.ZMAGA_CRNI;
			return true;
		}
		
		this.igralnaPlosca.crniLahkoGreNaCilj = this.igralnaPlosca.crniImaVseVHomeBoardu();
		this.igralnaPlosca.beliLahkoGreNaCilj = this.igralnaPlosca.beliImaVseVHomeBoardu();
		
		this.igralnaPlosca.beliImaPrazenZacetek = this.igralnaPlosca.beliImaPrazenZacetek();
		this.igralnaPlosca.crniImaPrazenZacetek = this.igralnaPlosca.crniImaPrazenZacetek();
		
		return true;
	}
	
	/*
	public Trikotnik vrniTrikotnik(int relativniIndeks) {
		// v odvisnosti od this.crniGreVSmeriUrinegaKazalca in this.crniZacneSpodaj
		
		if (relativniIndeks == 0) {
			if (BarvaIgralca.barva(this.igralecNaVrsti) == Figura.CRNA) {
				
			} else {
				
			}
		} else if (relativniIndeks == 25) {
			if (BarvaIgralca.barva(this.igralecNaVrsti) == Figura.CRNA) {
				
			} else {
				
			}
		} else {
			
		}
	}
	*/
	

	// @SuppressWarnings("unchecked")
	private static LinkedList<Integer> odstraniElementIzSeznama(LinkedList<Integer> seznam, Integer element) {
		LinkedList<Integer> kopija = new LinkedList<Integer>();
		kopija = (LinkedList<Integer>) seznam.clone();  // shallow copy, ampak to je vse kar rabimo
		kopija.remove(element);
		return kopija;
	}
	
	/*
	public LinkedList<Integer> vrniSeznamKock() {
		LinkedList<Integer> seznam = new LinkedList<Integer>();
		if (trenutnoJeDvojnaKocka) {
			for (int i = 0; i < 4; i++) {  // dvojna kocka predstavlja štiri mete
				seznam.add(dvojnaKocka.vrniVrednost());
			}
		} else {
			seznam.add(kocka1.vrniVrednost());
			seznam.add(kocka2.vrniVrednost());
		}
		return seznam;
	}
	*/
	
	public LinkedList<Integer> vrniSeznamKock() {  // kopira seznamKock, ker seznama kock itak ne bomo spreminjali tukaj
		LinkedList<Integer> kopija = new LinkedList<Integer>();
		kopija = (LinkedList<Integer>) this.seznamKock.clone();
		return kopija;
	}
	
	public List<Poteza> vrniVeljavnePotezeTePlosce() {  // metoda je odvisna od spremenljivke trenutnoJeDvojnaKocka. Ta metoda se bo torej izvajala tik po koncu metanja kock
		// return vrniVeljavnePoteze(this.vrniSeznamKock(), this.igralnaPlosca, this.igralecNaVrsti);
		return vrniVeljavnePoteze(this.vrniSeznamKock(), this.igralecNaVrsti);
	}
	
	private List<Poteza> vrniVeljavnePotezeZaEnoKocko(int kocka, Igralec igralecNaVrsti) {
		List<Poteza> seznamVsehPotez = new LinkedList<Poteza>();
		for (int i = 0; i <= igralnaPlosca.plosca.length; i++) {
			
			Poteza poteza = new Poteza(i + 1, kocka, BarvaIgralca.barva(igralecNaVrsti));  // i + 1 bo kvečjemu 25, in sicer takrat, ko bo to bela bariera
			
			Trikotnik trikotnik;  // predstavlja izhodiščni trikotnik poteze (od tam, od koder jemljemo figure)
			
			// za zadnji obhod for zanke pogledamo še primer, ko poteza vzame figuro iz bariere (namesto iz trikotnika)
			if (i == igralnaPlosca.plosca.length) {  // poleg ostalih 24 trikotnikov na konec dodamo še bariero od igralca
				if (igralecNaVrsti == Igralec.BELI) {
					trikotnik = igralnaPlosca.belaBariera;
				} else {
					trikotnik = igralnaPlosca.crnaBariera;
					poteza = new Poteza(0, kocka, BarvaIgralca.barva(igralecNaVrsti));  // izhodišče je 0 za črno bariero
				}
			} else {  // če jemljemo figuro iz trikotnika in ne bariere
				trikotnik = igralnaPlosca.plosca[i];
			}
			
			if (trikotnik.barvaFigur == BarvaIgralca.barva(igralecNaVrsti)) {
				
				int kolicinaPotez = trikotnik.stevilo;  // stevilo potez
				
				if (trikotnik.stevilo == 0) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
				
				boolean preverimoPotezo;

				preverimoPotezo = !igralnaPlosca.potezaNiVeljavna(poteza);  // true, če je poteza veljavna

				if (preverimoPotezo) {
					for (int j = 0; j < kolicinaPotez; j++) {
						seznamVsehPotez.add(poteza);  // če je poteza veljavna, jo dodamo med veljavne poteze
					}
				}
			}
		}
		return seznamVsehPotez;
	}
	
	
	private List<Poteza> vrniUnijoSeznamov(List<Poteza> prvi, List<Poteza> drugi) {  // predpostavljamo, da prvi nima dvojnikov
		List<Poteza> unija = new LinkedList<Poteza>();
		for (Poteza i : prvi) {
			unija.add(i);
		}
		for (Poteza j : drugi) {
			if (!unija.contains(j)) {
				unija.add(j);
			}
		}
		return unija;
	}
	
	
	/*
	private int vrniPotezeDvojneKocke(int vrednostKocke, List<Poteza> poteze) {
		int stevec = 0;
		for (Poteza p : poteze) {
			Poteza zacasnaPoteza = new Poteza(p);  // naredi kopijo
			int stevecPotezKiGredoDoCilja = 0;
			while (true) {
				if (igralnaPlosca.potezaNiVeljavna(zacasnaPoteza) || stevecPotezKiGredoDoCilja == 1) {
					break;
				} else {
					if (igralnaPlosca.potezaGreDoCilja(zacasnaPoteza)) {
						stevecPotezKiGredoDoCilja += 1;  // samo eno potezo, ki gre do cilja, hočemo šteti. Zato breakamo ko pridemo do druge take (ki bo kar naslednja poteza)
					}
					stevec += 1;
					// izhodišče od zacasnaPoteza in p bo vedno isto
					zacasnaPoteza.dodaj(p);  // tk ko zacasnaPoteza += p
				}
			}
		}
		return stevec;
	}
	*/
	
	
	// popravil to metodo: prej sem predpostavljal, da če ne moreš vseh kock porabit, potem nobene ne smeš porabit. Ampak to dejansko ni res: porabit moraš maksimalno število potez
	private List<Poteza> vrniVeljavnePoteze(LinkedList<Integer> seznamKock, Igralec igralecNaVrsti) {
		// if (seznamKock.size() > 2) throw new java.lang.RuntimeException("To še ni implementirano za več kot dve kocki.");  // saj še bo
		// if (seznamKock.size() != 4 && seznamKock.size() != 2) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		// if (seznamKock.size() == 4) {
		
		System.out.println("Seznam kock: " + seznamKock);  // izpiše seznam trenutnih vrednosti na kocki (seznam se krajša z vsako že porabljeno kocko/potezo)
		
		if (seznamKock.size() > 2) {
			if (!kockiImataEnakoVrednost()) throw new java.lang.RuntimeException("Na tem mestu bi vrednosti morali biti enaki.");
			
			
			List<Poteza> dvojnaKockaPoteze = vrniVeljavnePotezeZaEnoKocko(kocka1.vrniVrednost(), igralecNaVrsti);
			/*
			int stPotez = vrniPotezeDvojneKocke(dvojnaKocka.vrniVrednost(), dvojnaKockaPoteze);
			// if (stPotez < 4) {
			if (stPotez < seznamKock.size()) {
				dvojnaKockaPoteze.clear();  // zato, da bomo returnali prazen seznam
			}
			*/
			return dvojnaKockaPoteze;
		}
		
		if (seznamKock.size() == 1) {
			return this.vrniVeljavnePotezeZaEnoKocko(seznamKock.get(0), igralecNaVrsti);  // pridobi edini element iz seznama
		}
		
		// pridobi sezname veljavnih potez za vsako kocko posebej
		List<Poteza> prvaKocka = vrniVeljavnePotezeZaEnoKocko(kocka1.vrniVrednost(), igralecNaVrsti);
		List<Poteza> drugaKocka = vrniVeljavnePotezeZaEnoKocko(kocka2.vrniVrednost(), igralecNaVrsti);
		
		System.out.println("Prva kocka size: " + prvaKocka.size());
		System.out.println("Druga kocka size: " + drugaKocka.size());
		System.out.println("Prva kocka: " + prvaKocka);
		System.out.println("Druga kocka: " + drugaKocka);
		
		/*
		 Ko naredimo prvo potezo, ne moremo ustvariti novih potez za drugo kocko, razen če isto figuro premaknemo.
		 Prav tako ne moremo izgubiti potez, razen če bi isto figuro premaknili.
		 */
		/*
		 Ta metoda samo vrne veljavne poteze za prvi premik (od dveh premikov skupno). Za ta drugi premik bomo spet klicali toisto metodo.
		 */
		Kocka manjsaKocka;
		Kocka vecjaKocka;  // te spremenljivke ne bomo rabli, ampak je tkle vseen lepš
		List<Poteza> manjPotez;
		List<Poteza> vecPotez;
		if (prvaKocka.size() < drugaKocka.size()) {
			manjsaKocka = kocka1;
			vecjaKocka = kocka2;
			manjPotez = prvaKocka;
			vecPotez = drugaKocka;
		} else {
			manjsaKocka = kocka2;
			vecjaKocka = kocka1;
			manjPotez = drugaKocka;
			vecPotez = prvaKocka;
		}
		
		/*
		Psevdokoda (ni pomembno):
		
		if (manjšakocka == 0) {
			if (večjakocka == 0) {
				return večja U manjša;
			}
			else {
				for i in večjepoteze:
					if (i + manjša ustrezna poteza) {
						dodaj i v poteze
					}
				if poteze prazen:
					return večjepoteze
				return poteze
						
				alternativno:
				for i in večjepoteze:
					if (i + manjša is not ustrezna poteza) {
						odstrani i iz potez
					}
				if poteze prazno:
					return večjepoteze (ta prvotne, preden zbrišemo katerokoli potezo)
				return večja U manjša
			}
		} 
		elif (manjšakocka == 1) {
			// potem so neveljavne le tiste, kjer z večjo kocko pokvarimo potezo manjše
			// da se to zgodi, moramo z večjo kocko premakniti tisto figuro, ki jo premakne tudi edina poteza prve kocke
			if (večjakocka.size() == 1):
				if večja.izhodisce == manjša.izhodisce:
					if ((manjša + večja) is ustrezna poteza):
						return večja U manjša
					else:
						return potezaZVečjoVrednostjo  // baje je pravilo v backgammonu, da če ima vsaka kocka le eno možno potezo, ampak med seboj nista združljivi (kompatibilni), potem mora igralec igrati tisto izmed obeh potez, ki ima daljši premik
						// če imata obe potezi enak premik, potem sta to ena in ista poteza (saj se ujemata tako v izhodišču kot v premiku)
				else:
					return večja U manjša
			else:
				for i in večjakocka:
					if i.izhodisce == manjša.izhodisce:
						if (i + manjša is not ustrezna poteza):
							i remove from večjaKockaPoteze
				// tu se ne more zgoditi, da bi večja.size == 0.
				return večja U manjša
				
				
		}
		*/
		// če sta obe kocki vsaj 2, damo kr vse poteze od obeh
		
		
		/*
		Skrajšana psevdokoda zgornje psevdokode (ni popolna):
		
		for i in večjaKocka:
			if večja.izhodisce == manjša.izhodisce:
				if (i + manjša is not ustrezna poteza):
					i remove from večjaPotezaKocke
		if večjakocka == 0: manjše.clear()  // oz. return prazno
		return manjše U večje
		*/
		// System.out.println("manjPotez: " + manjPotez);
		// System.out.println("večPotez: " + vecPotez);
		if (manjPotez.size() <= 1) {  // če obe kocki data vsaj dve možni potezi, potem lahko kot prvo potezo izberemo katerokoli izmed potez
			List<Poteza> novePoteze = new LinkedList<Poteza>(vecPotez);  // kopiramo
			for (Poteza poteza : vecPotez) {
				Poteza skupnaPoteza;
				if (manjPotez.size() == 0 || poteza.izhodisce == manjPotez.get(0).izhodisce) {  // short-circuit
					skupnaPoteza = new Poteza(poteza.vrniIzhodisce(), poteza.premik + manjsaKocka.vrniVrednost(), poteza.igralec);
					if (igralnaPlosca.potezaNiVeljavna(skupnaPoteza)) {
						if (vecPotez.size() == 1) {  // vrnemo maksimalno izmed obeh potez
							if (manjPotez.size() == 0 || vecPotez.get(0).premik >= manjPotez.get(0).premik) {  // če sta premika enaka, potem je vseeno, katero izmed obeh potez vrnemo, saj sta enaki
								return vecPotez;
							}
							return manjPotez;
						}
						novePoteze.remove(poteza);
					}
				}
			}
			/*  // ta zakomentirana koda tule nič ne spremeni, saj smo zgoraj dodali dodaten pogoj: if (vecPotez.size() == 1) ...
			if (novePoteze.size() == 0) {  // če je ta pogoj izpolnjen in manjPotez.size() == 1, potem je prej morala vecPotez vsebovati natanko eno potezo in izhodišče obeh potez je isto 
				manjPotez.clear();  // to že pomeni, da bomo returnali prazen seznam
			}
			*/
			
			
			/* psevdokoda.
			if (novePoteze.size() == 0) {  // se lahko zgodi le če manj.size == 0, zaradi pogoja zgoraj: if (vecPotez.size() == 1) ...
				return vecPotez;
			} else
				return novepoteze U manjpotez;
			*/
			if (novePoteze.size() != 0) {
				vecPotez = novePoteze;
			}
		}
		List<Poteza> unija = vrniUnijoSeznamov(vecPotez, manjPotez);
		System.out.println("Unija: " + unija);
		// če so vse naše figure v zadnji četrtini (v home boardu)
		if ((igralecNaVrsti == Igralec.CRNI && igralnaPlosca.crniLahkoGreNaCilj) || (igralecNaVrsti == Igralec.BELI && igralnaPlosca.beliLahkoGreNaCilj)) {
			int najvecjaRazdalja = igralnaPlosca.najvecjaRazdaljaDoCilja(igralecNaVrsti.pridobiFiguro());
			// gledati moramo največjo razdaljo vseh naših žetonov, ne le tistih, ki jih lahko premaknemo (to sem se prej zmotil)
			for (int vrednostPosamezneKocke : seznamKock) {
				for (Poteza poteza : unija) {  // druga zanka, kjer pa odstranjujemo poteze, ki niso veljavne
					if (poteza.premik == vrednostPosamezneKocke) {
						// razdalja figure od cilja
						int razdalja = IgralnaPlosca.poljePotezeVRelativno(poteza.vrniIzhodisce(), igralecNaVrsti.pridobiNasprotnika().pridobiFiguro());  // vrne recimo 1, če smo eno polje stran od cilja
						if (razdalja < poteza.premik && razdalja < najvecjaRazdalja) {  // oz. če je razdalja manjša od minimuma teh dveh
							unija.remove(poteza);
						}
					}
				}
			}
		}
		return unija;
		
		/*
		if (prvaKocka.size() == 1) {  // le ena veljavna poteza za prvo kocko
			Poteza poteza = prvaKocka.get(0);  // vzemi edino potezo
			int stevec = 0;
			while (stevec < drugaKocka.size()) {
				Poteza p = drugaKocka.get(stevec);
				if (igralnaPlosca.potezaNiVeljavna(Poteza.pristejDvePotezi(poteza, p))) {
					drugaKocka.remove(stevec);
				}
				stevec += 1;
			}
		}  // pristejDvePotezi ne rabi met skupnih ciljev oz. izhodisc. Lahk kerikol potezi damo skup, razen če gre izven plošče
		
		return vrniUnijoSeznamov(prvaKocka, drugaKocka);
		*/
	}
	
	
	public void zamenjajIgralca() {
		this.igralecNaVrsti = this.igralecNaVrsti.pridobiNasprotnika();
		// if (this.trenutnoStanje != StanjeIgre.PREMIKANJE_FIGUR) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi!" + this.trenutnoStanje);  // le v stanju PREMIKANJE_FIGUR se bo lahko igralec zamenjal
		this.trenutnoStanje = StanjeIgre.METANJE_KOCK;  // spremenimo stanje
	}
	
	
	public boolean kockiImataEnakoVrednost() {
		return kocka1.vrniVrednost() == kocka2.vrniVrednost();
	}
}
