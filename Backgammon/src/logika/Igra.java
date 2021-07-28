package logika;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Igra {
	
	public IgralnaPlosca igralnaPlosca;
	public Igralec igralecNaVrsti;
	
	private Kocka kocka1;
	private Kocka kocka2;
	private Kocka dvojnaKocka;
	
	public boolean trenutnoJeDvojnaKocka;  // je true, ï¿½e trenutni igralec uporablja dvojno kocko
	
	public String napaka;  // ï¿½e je to null, ni napake, drugaï¿½e pa je
	
	public StanjeIgre trenutnoStanje;
	
	private Map<ImeKocke, Kocka> pridobiKocko;  // to bo vaï¿½no samo, ï¿½e izbiramo zaï¿½etnega igralca z metanjem kock
	private Map<ImeKocke, Igralec> igralcevaKocka;  // to bo vaï¿½no samo, ï¿½e izbiramo zaï¿½etnega igralca z metanjem kock
	
	// spremenljivke, ki doloï¿½ajo postavitev ploï¿½ï¿½e glede na platno
	public final boolean crniGreVSmeriUrinegaKazalca;
	public final boolean crniZacneSpodaj;
	
	
	// naslednji dve metodi se kliï¿½eta samo v konstruktorju, saj samo ustvarita slovar (map)
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
		
		igralnaPlosca = new IgralnaPlosca();  // ï¿½rni gre v smeri urinega kazalca po defaultu
		System.out.println(igralnaPlosca);
		igralecNaVrsti = igralecKiZacne;
		
		// napaka = null;  // itak bo na zaï¿½etku null, tako da tega ni treba napisati
		
		trenutnoStanje = StanjeIgre.IZBIRA_ZACETNEGA_IGRALCA;
	}
	
	/*
	public Igra() {
		this(null, true, true);  // to pomeni, da se igralec doloï¿½i z metanjem kocke
	}
	*/
	
	public void vrziKocki(boolean zrebanjeZacetnegaIgralca) {  // èe je parameter true, potem je to zaèetek igre
		kocka1.vrziKocko();
		kocka2.vrziKocko();
		if (!zrebanjeZacetnegaIgralca) this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		else this.trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	
	public int primerjajKocki(ImeKocke prvaKocka, ImeKocke drugaKocka) {  // to morda ne bo uporabno, ï¿½e bomo itak morali vsako vrednost posebej v vodji dobit
		return Integer.compare(pridobiVrednostKocke(prvaKocka), pridobiVrednostKocke(drugaKocka));
	}
	
	public void vrziDvojnoKocko() {
		dvojnaKocka.vrziKocko();
	}
	
	public Trikotnik pridobiTrikotnik(int mesto) {
		return igralnaPlosca.plosca[mesto];
	}
	
	// vrne false, ï¿½e je poteza neveljavna
	public boolean odigraj(Poteza poteza) {
		return false;  // TODO
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
	
	public LinkedList<Integer> vrniSeznamKock() {
		LinkedList<Integer> seznam = new LinkedList<Integer>();
		if (trenutnoJeDvojnaKocka) {
			for (int i = 0; i < 4; i++) {  // dvojna kocka predstavlja ï¿½tiri mete
				seznam.add(dvojnaKocka.vrniVrednost());
			}
		} else {
			seznam.add(kocka1.vrniVrednost());
			seznam.add(kocka2.vrniVrednost());
		}
		return seznam;
	}
	
	public List<Poteza> vrniVeljavnePotezeTePlosce() {  // metoda je odvisna od spremenljivke trenutnoJeDvojnaKocka. Ta metoda se bo torej izvajala tik po koncu metanja kock
		// return vrniVeljavnePoteze(this.vrniSeznamKock(), this.igralnaPlosca, this.igralecNaVrsti);
		return vrniVeljavnePoteze2(this.vrniSeznamKock(), this.igralecNaVrsti);
	}
	
	private List<Poteza> vrniVeljavnePotezeZaEnoKocko(int kocka, Igralec igralecNaVrsti) {
		List<Poteza> seznamVsehPotez = new LinkedList<Poteza>();
		for (int i = 0; i <= igralnaPlosca.plosca.length; i++) {
			
			Poteza poteza = new Poteza(i, kocka, BarvaIgralca.barva(igralecNaVrsti));
			
			Trikotnik trikotnik;  // predstavlja izhodiï¿½ï¿½ni trikotnik poteze (od tam, od koder jemljemo figure)
			
			// za zadnji obhod for zanke pogledamo ï¿½e primer, ko poteza vzame figuro iz bariere (namesto iz trikotnika)
			if (i == igralnaPlosca.plosca.length) {  // poleg ostalih 24 trikotnikov na konec dodamo ï¿½e bariero od igralca
				if (igralecNaVrsti == Igralec.BELI) {
					trikotnik = igralnaPlosca.belaBariera;
				} else {
					trikotnik = igralnaPlosca.crnaBariera;
				}
			} else {  // ï¿½e jemljemo figuro iz trikotnika in ne bariere
				trikotnik = igralnaPlosca.plosca[i];
			}
			
			
			if (trikotnik.barvaFigur == BarvaIgralca.barva(igralecNaVrsti)) {
				
				boolean preverimoPotezo;

				preverimoPotezo = !igralnaPlosca.potezaNiVeljavna(poteza);

				if (preverimoPotezo) {
					seznamVsehPotez.add(poteza);  // ï¿½e je poteza veljavna, jo dodamo med veljavne poteze
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
					zacasnaPoteza = zacasnaPoteza + p;
				}
			}
		}
		return stevec;
	}
	
	
	private List<Poteza> vrniVeljavnePoteze2(LinkedList<Integer> seznamKock, Igralec igralecNaVrsti) {
		if (seznamKock.size() > 2) throw new java.lang.RuntimeException("To ï¿½e ni implementirano za veï¿½ kot dve kocki.");  // saj ï¿½e bo
		
		if (seznamKock.size() == 4) {
			List<Poteza> dvojnaKockaPoteze = vrniVeljavnePotezeZaEnoKocko(dvojnaKocka.vrniVrednost(), igralecNaVrsti);
			int stPotez = vrniPotezeDvojneKocke(dvojnaKocka.vrniVrednost());
			if (stPotez < 4) {
				dvojnaKockaPoteze.clear();  // zato, da bomo returnali prazen seznam
			}
			return dvojnaKockaPoteze;
		}
		
		// pridobi sezname veljavnih potez za vsako kocko posebej
		List<Poteza> prvaKocka = vrniVeljavnePotezeZaEnoKocko(kocka1.vrniVrednost(), igralecNaVrsti);
		List<Poteza> drugaKocka = vrniVeljavnePotezeZaEnoKocko(kocka2.vrniVrednost(), igralecNaVrsti);
		
		/*
		 Ko naredimo prvo potezo, ne moremo ustvariti novih potez za drugo kocko, razen ï¿½e isto figuro premaknemo.
		 Prav tako ne moremo izgubiti potez, razen ï¿½e bi isto figuro premaknili.
		 */
		/*
		 Ta metoda samo vrne veljavne poteze za prvi premik (od dveh premikov skupno). Za ta drugi premik bomo spet klicali toisto metodo.
		 */
		Kocka manjsaKocka;
		Kocka vecjaKocka;  // te spremenljivke ne bomo rabli, ampak je tkle vseen lepï¿½
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
		
		if (manjï¿½akocka == 0) {
			if (veï¿½jakocka == 0) {
				return veï¿½ja U manjï¿½a;
			}
			else {
				for i in veï¿½jepoteze:
					if (i + manjï¿½a ustrezna poteza) {
						dodaj i v poteze
					}
				return poteze
						
				alternativno:
				for i in veï¿½jepoteze:
					if (i + manjï¿½a is not ustrezna poteza) {
						odstrani i iz potez
					}
				return veï¿½ja U manjï¿½a
			}
		} 
		elif (manjï¿½akocka == 1) {
			// potem so neveljavne le tiste, kjer z veï¿½jo kocko pokvarimo potezo manjï¿½e
			// da se to zgodi, moramo z veï¿½jo kocko premakniti tisto figuro, ki jo premakne tudi edina poteza prve kocke
			if (veï¿½jakocka.size() == 1):
				if veï¿½ja.izhodisce == manjï¿½a.izhodisce:
					if ((manjï¿½a + veï¿½ja) is ustrezna poteza):
						return veï¿½ja U manjï¿½a
					else:
						return prazno
				else:
					return veï¿½ja U manjï¿½a
			else:
				for i in veï¿½jakocka:
					if i.izhodisce == manjï¿½a.izhodisce:
						if (i + manjï¿½a is not ustrezna poteza):
							i remove from veï¿½jaKockaPoteze
				
		}
		*/
		// ï¿½e sta obe kocki vsaj 2, damo kr vse poteze od obeh
		
		
		/*
		Skrajï¿½ana psevdokoda zgornje psevdokode (ni popolna):
		
		for i in veï¿½jaKocka:
			if veï¿½ja.izhodisce == manjï¿½a.izhodisce:
				if (i + manjï¿½a is not ustrezna poteza):
					i remove from veï¿½jaPotezaKocke
		if veï¿½jakocka == 0: manjï¿½e.clear()  // oz. return prazno
		return manjï¿½e U veï¿½je
		*/
		if (manjPotez.size() <= 1) {  // ï¿½e obe kocki data vsaj dve moï¿½ni potezi, potem lahko kot prvo potezo izberemo katerokoli izmed potez
			
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
				manjPotez.clear();  // to ï¿½e pomeni, da bomo returnali prazen seznam
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
		}  // pristejDvePotezi ne rabi met skupnih ciljev oz. izhodisc. Lahk kerikol potezi damo skup, razen ï¿½e gre izven ploï¿½ï¿½e
		
		return vrniUnijoSeznamov(prvaKocka, drugaKocka);
		*/
	}
	
	
	// to metodo zbriï¿½i
	private List<Poteza> vrniVeljavnePoteze(LinkedList<Integer> seznamKock, IgralnaPlosca igralnaPlosca, Igralec igralecNaVrsti) // glede na trenutne kocke in igralcaNaVrsti
	// ne vrne parov potez (ï¿½e sta paï¿½ dve potezi moï¿½ni, bo najprej vrnil eno, nato drugo)
	

	{
		/*
		List<Integer> seznamKock = this.vrniSeznamKock();
		IgralnaPlosca igralnaPlosca = this.igralnaPlosca;
		Igralec igralecNaVrsti = this.igralecNaVrsti;
		*/
		List<Poteza> seznamVsehPotez = new LinkedList<Poteza>();
		
		for (Integer kocka : seznamKock) {
			// kocka1:
			for (int i = 0; i <= igralnaPlosca.plosca.length; i++) {
				
				Trikotnik trikotnik;
				if (i == igralnaPlosca.plosca.length) {  // poleg ostalih 24 trikotnikov na konec dodamo ï¿½e bariero od igralca
					if (igralecNaVrsti == Igralec.BELI) {
						trikotnik = igralnaPlosca.belaBariera;
					} else {
						trikotnik = igralnaPlosca.crnaBariera;
					}
				} else {
					trikotnik = igralnaPlosca.plosca[i];
				}
				
				
				if (trikotnik.barvaFigur == BarvaIgralca.barva(igralecNaVrsti)) {
					// kopija = igralnaPlosca.copy();
					IgralnaPlosca kopija = new IgralnaPlosca(igralnaPlosca);
					
					boolean preverimoPotezo;
					// preverimoPotezo = kopija.igrajPotezo(new Poteza(trikotnik, kocka));
					preverimoPotezo = kopija.igrajPotezo(new Poteza(i, kocka, BarvaIgralca.barva(igralecNaVrsti)));
					if (seznamKock.size() == 1) {  // manj ko 1 tk al tk ne more bit
						preverimoPotezo = preverimoPotezo;
					}
					else {
						if (preverimoPotezo) {
							preverimoPotezo = (
									vrniVeljavnePoteze(odstraniElementIzSeznama(seznamKock, kocka), kopija, igralecNaVrsti).size() > 0
									?
									true 
									: 
									false
							);
						}
					}
					if (preverimoPotezo) {
						// seznamVsehPotez.add(new Poteza(trikotnik, kocka));  // seznam ni odvisen od kocke
						seznamVsehPotez.add(new Poteza(i, kocka, BarvaIgralca.barva(igralecNaVrsti)));
					} 
					/*
					else {
						pass;
					}
					*/
				}
			}
		}
		return seznamVsehPotez;
		
	}
	
	public void zamenjajIgralca() {
		this.igralecNaVrsti = this.igralecNaVrsti.pridobiNasprotnika();
		if (this.trenutnoStanje != StanjeIgre.PREMIKANJE_FIGUR) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi!");  // le v stanju PREMIKANJE_FIGUR se bo lahko igralec zamenjal
		this.trenutnoStanje = StanjeIgre.METANJE_KOCK;  // spremenimo stanje
	}
	

}
