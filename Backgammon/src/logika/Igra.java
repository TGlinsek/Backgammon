package logika;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Igra {
	
	public IgralnaPlosca igralnaPlosca;
	public Igralec igralecNaVrsti;
	
	private Kocka kocka1;
	private Kocka kocka2;
	private Kocka dvojnaKocka;
	
	public boolean trenutnoJeDvojnaKocka;  // je true, če trenutni igralec uporablja dvojno kocko
	
	// npr., če je uporabnik hotel prestaviti figuro, ki je ni možno prestaviti, se bo na zaslonu prikazalo sporočilo, ki bo vsebovalo tale string:
	public String napaka;  // če je to null, ni napake, drugače pa je
	
	public LinkedList<Integer> seznamKock;
	
	public StanjeIgre trenutnoStanje;
	
	private Map<ImeKocke, Kocka> pridobiKocko;  // to bo važno samo, če izbiramo začetnega igralca z metanjem kock
	private Map<ImeKocke, Igralec> igralcevaKocka;  // to bo važno samo, če izbiramo začetnega igralca z metanjem kock
	
	// spremenljivke, ki določajo postavitev plošče glede na platno
	public final boolean crniGreVSmeriUrinegaKazalca;
	public final boolean crniZacneSpodaj;
	
	
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
	
	public Igra(Igralec igralecKiZacne, boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj) {
		kocka1 = new Kocka();
		kocka2 = new Kocka();
		dvojnaKocka = new Kocka();
		
		napolniSlovarIgralcevaKocka();
		napolniSlovarPridobiKocko();
		
		this.crniGreVSmeriUrinegaKazalca = crniGreVSmeriUrinegaKazalca;
		this.crniZacneSpodaj = crniZacneSpodaj;
		
		igralnaPlosca = new IgralnaPlosca();  // črni gre v smeri urinega kazalca po defaultu
		igralecNaVrsti = igralecKiZacne;
		
		// napaka = null;  // itak bo na začetku null, tako da tega ni treba napisati
		
		trenutnoStanje = StanjeIgre.IZBIRA_ZACETNEGA_IGRALCA;
		
		seznamKock = new LinkedList<Integer>();
	}
	
	/*  // default konstruktor, pač če bi igralec želel default nastavitve
	public Igra() {
		this(null, true, true);  // to pomeni, da se igralec določi z metanjem kocke
	}
	*/
	
	
//	!!popravi, da bo vodja dal vrednost kock: TODO
	public int[] vrziKocki(boolean zrebanjeZacetnegaIgralca) {  // parameter true -> izbira igralca s kockami, false -> navadno metanje kock v teku igre
		kocka1.vrziKocko();
		kocka2.vrziKocko();
		if (!zrebanjeZacetnegaIgralca) this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		else this.trenutnoStanje = StanjeIgre.METANJE_KOCK;
		
		seznamKock.clear();
		seznamKock.add(kocka1.vrniVrednost());
		seznamKock.add(kocka2.vrniVrednost());
		
		return new int[] {kocka1.vrniVrednost(), kocka2.vrniVrednost()};  // morda ne bomo rabili nič returnat
	}
	
	public int primerjajKocki(ImeKocke prvaKocka, ImeKocke drugaKocka) {  // to morda ne bo uporabno, če bomo itak morali vsako vrednost posebej v vodji dobit
		return Integer.compare(pridobiVrednostKocke(prvaKocka), pridobiVrednostKocke(drugaKocka));
	}
	
	public void vrziDvojnoKocko() {
		dvojnaKocka.vrziKocko();
		
		this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		
		seznamKock.clear();
		for (int i = 0; i < 4; i++) seznamKock.add(dvojnaKocka.vrniVrednost());
	}
	
	public Trikotnik pridobiTrikotnik(int mesto) {
		return igralnaPlosca.plosca[mesto];
	}
	
	
	// ali je poteza veljavna?
	public boolean potezaJeVeljavna(Poteza poteza) {
		return this.vrniVeljavnePotezeTePlosce().contains(poteza);
	}
	
	
	// vrne false, če je poteza neveljavna
	public boolean odigraj(Poteza poteza) {  // a naj bo tu boolean al void?
		if (!potezaJeVeljavna(poteza)) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		return this.igralnaPlosca.igrajPotezo(poteza);
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
				
				boolean preverimoPotezo;

				preverimoPotezo = !igralnaPlosca.potezaNiVeljavna(poteza);  // true, če je poteza veljavna

				if (preverimoPotezo) {
					seznamVsehPotez.add(poteza);  // če je poteza veljavna, jo dodamo med veljavne poteze
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
	
	
	private int vrniPotezeDvojneKocke(int vrednostKocke, List<Poteza> poteze) {
		int stevec = 0;
		for (Poteza p : poteze) {
			Poteza zacasnaPoteza = p;
			while (true) {
				if (igralnaPlosca.potezaNiVeljavna(zacasnaPoteza)) {
					break;
				} else {
					stevec += 1;
					// izhodišče od zacasnaPoteza in p bo vedno isto
					zacasnaPoteza.dodaj(p);  // tk ko zacasnaPoteza += p
				}
			}
		}
		return stevec;
	}
	
	
	private List<Poteza> vrniVeljavnePoteze(LinkedList<Integer> seznamKock, Igralec igralecNaVrsti) {
		// if (seznamKock.size() > 2) throw new java.lang.RuntimeException("To še ni implementirano za več kot dve kocki.");  // saj še bo
		// if (seznamKock.size() != 4 && seznamKock.size() != 2) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		// if (seznamKock.size() == 4) {
		
		System.out.println(seznamKock);
		if (trenutnoJeDvojnaKocka) {
			List<Poteza> dvojnaKockaPoteze = vrniVeljavnePotezeZaEnoKocko(dvojnaKocka.vrniVrednost(), igralecNaVrsti);
			int stPotez = vrniPotezeDvojneKocke(dvojnaKocka.vrniVrednost(), dvojnaKockaPoteze);
			// if (stPotez < 4) {
			if (stPotez < seznamKock.size()) {
				dvojnaKockaPoteze.clear();  // zato, da bomo returnali prazen seznam
			}
			return dvojnaKockaPoteze;
		}
		if (seznamKock.size() > 2) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		
		if (seznamKock.size() == 1) {
			return this.vrniVeljavnePotezeZaEnoKocko(seznamKock.get(0), igralecNaVrsti);  // pridobi edini element iz seznama
		}
		
		// pridobi sezname veljavnih potez za vsako kocko posebej
		List<Poteza> prvaKocka = vrniVeljavnePotezeZaEnoKocko(kocka1.vrniVrednost(), igralecNaVrsti);
		List<Poteza> drugaKocka = vrniVeljavnePotezeZaEnoKocko(kocka2.vrniVrednost(), igralecNaVrsti);
		
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
				return poteze
						
				alternativno:
				for i in večjepoteze:
					if (i + manjša is not ustrezna poteza) {
						odstrani i iz potez
					}
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
						return prazno
				else:
					return večja U manjša
			else:
				for i in večjakocka:
					if i.izhodisce == manjša.izhodisce:
						if (i + manjša is not ustrezna poteza):
							i remove from večjaKockaPoteze
				
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
		if (manjPotez.size() <= 1) {  // če obe kocki data vsaj dve možni potezi, potem lahko kot prvo potezo izberemo katerokoli izmed potez
			
			for (Poteza poteza : vecPotez) {
				Poteza skupnaPoteza;
				if (manjPotez.size() == 0 || poteza.izhodisce == manjPotez.get(0).izhodisce) {  // short-circuit
					skupnaPoteza = new Poteza(poteza.vrniIzhodisce(), poteza.premik + manjsaKocka.vrniVrednost(), poteza.igralec);
					if (igralnaPlosca.potezaNiVeljavna(skupnaPoteza)) {
						vecPotez.remove(poteza);
					}
				} else {
					continue;
				}
			}
			if (vecPotez.size() == 0) {
				manjPotez.clear();  // to že pomeni, da bomo returnali prazen seznam
			}
		}
		return vrniUnijoSeznamov(vecPotez, manjPotez);
		
		
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
		if (this.trenutnoStanje != StanjeIgre.PREMIKANJE_FIGUR) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi!");  // le v stanju PREMIKANJE_FIGUR se bo lahko igralec zamenjal
		this.trenutnoStanje = StanjeIgre.METANJE_KOCK;  // spremenimo stanje
	}
	

}
