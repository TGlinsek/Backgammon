package logika;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tekstovni_vmesnik.VrstaIgralca;

public class Igra {
	
	private IgralnaPlosca igralnaPlosca;
	public Igralec igralecNaVrsti;
	
	private Kocka kocka1;
	private Kocka kocka2;
	private Kocka dvojnaKocka;
	
	public boolean trenutnoJeDvojnaKocka;
	
	public String napaka;  // èe je to null, ni napake
	
	public StanjeIgre trenutnoStanje;
	
	private Map<ImeKocke, Kocka> pridobiKocko;  // to bo važno samo, èe izbiramo zaèetnega igralca z metanjem kock
	private Map<ImeKocke, Igralec> igralcevaKocka;  // to bo važno samo, èe izbiramo zaèetnega igralca z metanjem kock
	
	public final boolean crniGreVSmeriUrinegaKazalca;
	public final boolean crniZacneSpodaj;
	
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
		
		igralnaPlosca = new IgralnaPlosca();  // èrni gre v smeri urinega kazalca po defaultu
		igralecNaVrsti = igralecKiZacne;
		
		// napaka = null;
		
		trenutnoStanje = StanjeIgre.IZBIRA_ZACETNEGA_IGRALCA;
	}
	
	/*
	public Igra() {
		this(null, true, true);  // to pomeni, da se igralec doloèi z metanjem kocke
	}
	*/
	
	public void vrziKocki(boolean zrebanjeZacetnegaIgralca) {
		kocka1.vrziKocko();
		kocka2.vrziKocko();
		if (!zrebanjeZacetnegaIgralca) this.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
		else this.trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	
	public int primerjajKocki(ImeKocke prvaKocka, ImeKocke drugaKocka) {  // to morda ne bo uporabno, èe bomo itak morali vsako vrednost posebej v vodji dobit
		return Integer.compare(pridobiVrednostKocke(prvaKocka), pridobiVrednostKocke(drugaKocka));
	}
	
	public void vrziDvojnoKocko() {
		dvojnaKocka.vrziKocko();
	}
	
	public Trikotnik pridobiTrikotnik(int mesto) {
		return igralnaPlosca.plosca[mesto];
	}
	
	// vrne false, èe je poteza neveljavna
	public boolean igraj(Poteza poteza) {
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
			for (int i = 0; i < 4; i++) {
				seznam.add(dvojnaKocka.vrniVrednost());
			}
		} else {
			seznam.add(kocka1.vrniVrednost());
			seznam.add(kocka2.vrniVrednost());
		}
		return seznam;
	}
	
	public List<Poteza> vrniVeljavnePotezeTePlosce() {
		// return vrniVeljavnePoteze(this.vrniSeznamKock(), this.igralnaPlosca, this.igralecNaVrsti);
		return vrniVeljavnePoteze2(this.vrniSeznamKock(), this.igralecNaVrsti);
	}
	
	private List<Poteza> vrniVeljavnePotezeZaEnoKocko(int kocka, Igralec igralecNaVrsti) {
		List<Poteza> seznamVsehPotez = new LinkedList<Poteza>();
		for (int i = 0; i <= igralnaPlosca.plosca.length; i++) {
			
			Poteza poteza = new Poteza(i, kocka, BarvaIgralca.barva(igralecNaVrsti));
			
			Trikotnik trikotnik;
			if (i == igralnaPlosca.plosca.length) {  // poleg ostalih 24 trikotnikov na konec dodamo še bariero od igralca
				if (igralecNaVrsti == Igralec.BELI) {
					trikotnik = igralnaPlosca.belaBariera;
				} else {
					trikotnik = igralnaPlosca.crnaBariera;
				}
			} else {
				trikotnik = igralnaPlosca.plosca[i];
			}
			
			
			if (trikotnik.barvaFigur == BarvaIgralca.barva(igralecNaVrsti)) {
				
				boolean preverimoPotezo;

				preverimoPotezo = !igralnaPlosca.potezaNiVeljavna(poteza);

				if (preverimoPotezo) {
					seznamVsehPotez.add(poteza);
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
	
	private List<Poteza> vrniVeljavnePoteze2(LinkedList<Integer> seznamKock, Igralec igralecNaVrsti) {
		if (seznamKock.size() > 2) throw new java.lang.RuntimeException("To še ni implementirano za veè kot dve kocki.");  // in tudi ne bo
		
		List<Poteza> prvaKocka = vrniVeljavnePotezeZaEnoKocko(kocka1.vrniVrednost(), igralecNaVrsti);
		List<Poteza> drugaKocka = vrniVeljavnePotezeZaEnoKocko(kocka2.vrniVrednost(), igralecNaVrsti);
		
		if (prvaKocka.size() == 1) {
			Poteza poteza = prvaKocka.get(0);
			int stevec = 0;
			while (stevec < drugaKocka.size()) {
				Poteza p = drugaKocka.get(stevec);
				if (igralnaPlosca.potezaNiVeljavna(Poteza.pristejDvePotezi(poteza, p))) {
					drugaKocka.remove(stevec);
				}
				stevec += 1;
			}
		} else if (drugaKocka.size() == 1) {
			
		}
		return vrniUnijoSeznamov(prvaKocka, drugaKocka);
	}
	
	private List<Poteza> vrniVeljavnePoteze(LinkedList<Integer> seznamKock, IgralnaPlosca igralnaPlosca, Igralec igralecNaVrsti) // glede na trenutne kocke in igralcaNaVrsti
	// ne vrne parov potez (èe sta paè dve potezi možni, bo najprej vrnil eno, nato drugo)
	

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
				if (i == igralnaPlosca.plosca.length) {  // poleg ostalih 24 trikotnikov na konec dodamo še bariero od igralca
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
		if (this.trenutnoStanje != StanjeIgre.PREMIKANJE_FIGUR) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi!");
		this.trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	

}
