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
		napolniSlovarIgralcevaKocka();
		napolniSlovarPridobiKocko();
		
		this.crniGreVSmeriUrinegaKazalca = crniGreVSmeriUrinegaKazalca;
		this.crniZacneSpodaj = crniZacneSpodaj;
		
		igralnaPlosca = new IgralnaPlosca(crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);  // èrni gre v smeri urinega kazalca po defaultu
		igralecNaVrsti = igralecKiZacne;
		
		// napaka = null;
		kocka1 = new Kocka();
		kocka2 = new Kocka();
		dvojnaKocka = new Kocka();
		
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
	
	public List<Poteza> vrniVeljavnePotezeTePlosce(LinkedList<Kocka> seznamKock) {
		return vrniVeljavnePoteze(seznamKock, this.igralnaPlosca, this.igralecNaVrsti);
	}
	

	// @SuppressWarnings("unchecked")
	private static LinkedList<Kocka> odstraniElementIzSeznama(LinkedList<Kocka> seznam, Kocka element) {
		LinkedList<Kocka> kopija = new LinkedList<Kocka>();
		kopija = (LinkedList<Kocka>) seznam.clone();  // shallow copy, ampak to je vse kar rabimo
		kopija.remove(element);
		return kopija;
	}
	
	public static List<Poteza> vrniVeljavnePoteze(LinkedList<Kocka> seznamKock, IgralnaPlosca igralnaPlosca, Igralec igralecNaVrsti) // glede na trenutne kocke in igralcaNaVrsti
	// ne vrne parov potez (èe sta paè dve potezi možni, bo najprej vrnil eno, nato drugo)
	{
		List<Poteza> seznamVsehPotez = new LinkedList<Poteza>();
		/*
		Trikotnik[] seznam = new Trikotnik[25];  // 25, ker gledamo še bariero (nulto polje) (ampak le od enega igralca, zato ni 26)
		
		if (igralecNaVrsti == Igralec.BELI) {
			seznam[0] = belaBariera;
		} else {
			seznam[0] = crnaBariera;
		}
		*/  // tega ne rabimo, kr smo itak drugaè šli iterirat po trikotnikih
		for (Kocka kocka : seznamKock) {
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
					preverimoPotezo = kopija.igrajPotezo(new Poteza(trikotnik, kocka.vrniVrednost()));
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
						seznamVsehPotez.add(new Poteza(trikotnik, kocka.vrniVrednost()));  // seznam ni odvisen od kocke
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
	

}
