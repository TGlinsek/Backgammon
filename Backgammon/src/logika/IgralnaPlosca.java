package logika;

import java.util.Arrays;

public class IgralnaPlosca {
	
	// ne kock, vseeno pa opazuj, koliko jih je ï¿½e na zaï¿½etku oz. na koncu
	// public Trikotnik[] plosca = new Trikotnik[24];
	/*
	public Trikotnik[] plosca = {
		new Trikotnik(Figura.PRAZNA, 0),
		new Trikotnik(Figura.PRAZNA, 0)
	};
	 */
	
	
	/*
	 bariera je pri backgammonu sredinski del ploï¿½ï¿½e, torej tam, kjer igralca na zaï¿½etku hranita figure
	 
	 */
	
	public Trikotnik[] plosca;  // indeksi od 0 do 23
	
	public Trikotnik belaBariera = new Trikotnik(Figura.BELA, 0);  // na zaï¿½etku so vse bele figure v barieri belega igralca
	public Trikotnik crnaBariera = new Trikotnik(Figura.CRNA, 0);  // bariera ï¿½rnega
	public Trikotnik beliCilj = new Trikotnik(Figura.BELA, 0);  // figure belega igralca, ki so ï¿½e priï¿½le na cilj
	public Trikotnik crniCilj = new Trikotnik(Figura.CRNA, 0);  // cilj ï¿½rnega igralca
	
	
	private static Trikotnik[] vrniEnoPolovicoPlosce(Figura zacetna, Figura koncna) {  // pomoï¿½na metoda za ploï¿½ï¿½o
		if (zacetna == Figura.PRAZNA || koncna == Figura.PRAZNA) throw new java.lang.RuntimeException("Figura ne more biti prazna tukaj!");
		if (zacetna == koncna) throw new java.lang.RuntimeException("Figuri morata biti razliï¿½ni!");
		return new Trikotnik[] {
				new Trikotnik(zacetna, 2),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(koncna, 5),
				// bar
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(koncna, 3),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(zacetna, 5)	
		};
	}
	
	
	private static Trikotnik[] vrniObrnjenoPolovicoPlosce(Figura zacetna, Figura koncna) {  // samo obrnemo polovico
		Trikotnik[] polovica = vrniEnoPolovicoPlosce(zacetna, koncna);
		
		/*
		https://www.geeksforgeeks.org/reverse-an-array-in-java/
		Collections.reverse(Arrays.asList(polovica));
		
		return Arrays.asList(polovica);
		*/
		Trikotnik[] novaPolovica = new Trikotnik[12];
		for (int i = 0; i < 12; i++) {
			novaPolovica[i] = new Trikotnik(polovica[12 - i - 1]);
		}
		return novaPolovica;
	}
	
	
	private static Trikotnik[] zdruziDvePolovici(Trikotnik[] prva, Trikotnik[] druga) {
		// https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
		Trikotnik[] celota = Arrays.copyOf(prva, prva.length + druga.length);
		System.arraycopy(druga, 0, celota, prva.length, druga.length);
		return celota;
	}
	
	
	public IgralnaPlosca() {  // vrne ploï¿½ï¿½o, ki je tabela 24-ih trikotnikov
		// grajenje ploï¿½ï¿½e razdelimo na dva dela, saj sta oba dela (anti)simetriï¿½na (samo barvo figur je treba zamenjati)
		plosca = zdruziDvePolovici(vrniEnoPolovicoPlosce(Figura.CRNA, Figura.BELA), vrniObrnjenoPolovicoPlosce(Figura.BELA, Figura.CRNA));
		/*
		 Alternativno:
		 
		 plosca = new Trikotnik[] {
		 	new Trikotnik(Figura.CRNA, 2),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.BELA, 5),
			// bar
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.BELA, 3),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.CRNA, 5),
			
			new Trikotnik(Figura.BELA, 5),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.CRNA, 3),
			new Trikotnik(Figura.PRAZNA, 0),
			// bar
			new Trikotnik(Figura.CRNA, 5),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.PRAZNA, 0),
			new Trikotnik(Figura.BELA, 2)
		 }
		 */
	}  // popravil sem, da zdaj igralnaPlosca zaï¿½ne vedno na ï¿½rnem ï¿½tartu, ne glede na to, kje ï¿½rni zaï¿½ne
	
	
	public IgralnaPlosca(IgralnaPlosca igralnaPlosca) {  // kopiranje
		int velikost = igralnaPlosca.plosca.length;
		this.plosca = new Trikotnik[velikost];
		for (int i = 0; i < velikost; i++) {
			this.plosca[i] = new Trikotnik(igralnaPlosca.plosca[i]);
		}
		this.belaBariera = new Trikotnik(igralnaPlosca.belaBariera);
		this.crnaBariera = new Trikotnik(igralnaPlosca.crnaBariera);
		this.beliCilj = new Trikotnik(igralnaPlosca.beliCilj);
		this.crniCilj = new Trikotnik(igralnaPlosca.crniCilj);
	}
	
	
	private Trikotnik pridobiTrikotnik(int relativnoPolje, Figura igralecNaVrsti) {
		// relativno polje je lahko število med (vkljuèno) 0-25 (skupaj 26 razliènih možnosti). 0 pomeni bariero, 25 pomeni cilj, ostalih 24 pa predstavljajo trikotnike na plošèi
		if (relativnoPolje == 0) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crnaBariera;
			} else {
				// return belaBariera;
				return beliCilj;
			}
		} else if (relativnoPolje >= 25) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crniCilj;
			} else {
				// return beliCilj;
				return belaBariera;
			}
		} else {
			return this.plosca[relativnoPolje - 1];  // pri plosci ne upoï¿½tevamo bariere (ploï¿½ï¿½a ima 24 trikotnikov, torej bariere in cilja ni zraven), zato se vse za eno zamakne
		}
	}
	
	
	public boolean potezaNiVeljavna(Poteza poteza) {  // vrne true le, ï¿½e poteza ni veljavna (torej, tja ne moremo prestaviti, saj so tam nasprotnikove figure)
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		return ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1;  // stevilo je ï¿½tevilo figur na trikotniku
	}
	
	
	public boolean igrajPotezo(Poteza poteza) {  // vrne true iff je bila poteza veljavna in torej izpeljana
		Trikotnik izhodiscniTrikotnik = pridobiTrikotnik(poteza.vrniIzhodisce(), poteza.vrniIgralca());
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		
		if (izhodiscniTrikotnik.barvaFigur != poteza.vrniIgralca()) return false;  // ï¿½e na izhodiï¿½ï¿½nem trikotniku sploh niso ta prave figure (a bi blo boljï¿½ kr throwat kak exception?)
		if (ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1) return false;  // ne moremo prestaviti tja (tam so nasprotnikove figure)
		
		
		izhodiscniTrikotnik.odstraniFiguro();
		ciljniTrikotnik.dodajFiguro(poteza.vrniIgralca());
		
		return true;  // poteza je bila uspeï¿½no odigrana
	}
}
