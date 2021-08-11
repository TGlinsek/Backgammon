package logika;

import java.util.Arrays;

public class IgralnaPlosca {
	
	// ne vsebuje informacije o kockah
	// vsebuje informacije o barieri in cilju
	// public Trikotnik[] plosca = new Trikotnik[24];
	
	/*
	 bariera je pri backgammonu sredinski del plošče, torej tam, kjer igralca na začetku hranita figure
	 
	 */
	
	public Trikotnik[] plosca;  // indeksi od 0 do 23
	
	public Trikotnik belaBariera = new Trikotnik(Figura.PRAZNA, 0);  // na začetku so vse bele figure v barieri belega igralca
	public Trikotnik crnaBariera = new Trikotnik(Figura.PRAZNA, 0);  // bariera črnega
	public Trikotnik beliCilj = new Trikotnik(Figura.PRAZNA, 0);  // figure belega igralca, ki so že prišle na cilj
	public Trikotnik crniCilj = new Trikotnik(Figura.PRAZNA, 0);  // cilj črnega igralca
	
	public boolean beliLahkoGreNaCilj;
	public boolean crniLahkoGreNaCilj;
	
	public boolean beliImaPrazenZacetek;
	public boolean crniImaPrazenZacetek;
	
	private static Trikotnik[] vrniEnoPolovicoPlosce(Figura zacetna, Figura koncna) {  // pomožna metoda za ploščo
		if (zacetna == Figura.PRAZNA || koncna == Figura.PRAZNA) throw new java.lang.RuntimeException("Figura ne more biti prazna tukaj!");
		if (zacetna == koncna) throw new java.lang.RuntimeException("Figuri morata biti različni!");
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
	
	
	public IgralnaPlosca() {  // vrne ploščo, ki je tabela 24-ih trikotnikov
		// grajenje plošče razdelimo na dva dela, saj sta oba dela (anti)simetrična (samo barvo figur je treba zamenjati)
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
		beliLahkoGreNaCilj = false;
		crniLahkoGreNaCilj = false;
		
		beliImaPrazenZacetek = true;
		crniImaPrazenZacetek = true;
	}  // popravil sem, da zdaj igralnaPlosca začne vedno na črnem štartu, ne glede na to, kje črni začne
	
	
	// mislim, da tega ne bomo rabili
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
	
	
	@Override
	public String toString() {  // črni začne desno spodaj, beli pa desno zgoraj
		String str = "__________________________________________\n";
		for (int i = 12; i < 24; i++) {
			str += "\\" + plosca[i] + "/";  // lahko dodamo toString(), ni pa treba
			if (i == 17) {
				str += " || ";
			}
		}
		str += "\n\n";
		for (int j = 11; j >= 0; j--) {
			str += "/" + plosca[j] + "\\";
			if (j == 6) {
				str += " || ";
			}
		}
		str += "\n";
		str += "__________________________________________\n";
		str += "bela bariera: " + belaBariera + "\n";
		str += "črna bariera: " + crnaBariera + "\n";
		str += "beli cilj: " + beliCilj + "\n";
		str += "črni cilj: " + crniCilj;
		return str;  // čist desno spodnji trikotnik ima indeks 1, ne glede na to, kateri igralec smo
	}
	
	
	private Trikotnik pridobiTrikotnik(int poljePoteze, Figura igralecNaVrsti) {
		// poljePoteze polje je lahko število med (vključno) 0-25 (skupaj 26 različnih možnosti). 0 pomeni bariero, 25 pomeni cilj, ostalih 24 pa predstavljajo trikotnike na plošči
		if (poljePoteze <= 0) {  // recimo, če ima beli igralec potezo, ki gre preko cilja, takrat bo poljePoteze negativno
			if (igralecNaVrsti == Figura.CRNA) {
				return crnaBariera;
			} else {
				// return belaBariera;
				return beliCilj;
			}
		} else if (poljePoteze >= 25) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crniCilj;
			} else {
				// return beliCilj;
				return belaBariera;
			}
		} else {
			return this.plosca[poljePoteze - 1];  // pri plosci ne upoštevamo bariere (plošča ima 24 trikotnikov, torej bariere in cilja ni zraven), zato se vse za eno zamakne
		}
		// pretvorili smo polje iz poteze v dejanski trikotnik s plošče
	}
	
	
	public Trikotnik vrniTrikotnik(int poljePoteze, Figura igralec) {
		Trikotnik trikotnik = pridobiTrikotnik(poljePoteze, igralec);
		return trikotnik;
	}
	
	
	public boolean potezaNiVeljavna(Poteza poteza) {  // vrne true le, če poteza ni veljavna (torej, tja ne moremo prestaviti, saj so tam nasprotnikove figure)
		// System.out.println(poteza);
		
		// ne dovolimo potez, ki gredo na cilj
		if (poteza.vrniIgralca() == Figura.BELA && !beliLahkoGreNaCilj) {
			if (poteza.vrniCilj() <= 0) return true;
		}
		
		if (poteza.vrniIgralca() == Figura.CRNA && !crniLahkoGreNaCilj) {
			if (poteza.vrniCilj() >= 25) return true;
		}
		
		
		// ne dovolimo potez, ki ne spravijo figur iz bariere, če so tam kakšne figure
		if (poteza.vrniIgralca() == Figura.CRNA && !crniImaPrazenZacetek) {
			if (poteza.izhodisce != 0) return true;
		}
		
		if (poteza.vrniIgralca() == Figura.BELA && !beliImaPrazenZacetek) {
			if (poteza.izhodisce != 25) return true;
		}
		
		
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		return ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1;  // stevilo je število figur na trikotniku
	}  // poteza je veljavna tudi, če gre čez cilj (tj. vržemo preveliko vrednost na kocki, zato gremo preko cilja)
	
	
	public boolean potezaGreDoCilja(Poteza poteza) {  // vrne true čee poteza popelje figuro do cilja
		int cilj = poteza.vrniCilj();
		return cilj <= 0 || cilj >= 25;
	}
	
	
	public boolean igrajPotezo(Poteza poteza) {  // vrne true iff je bila poteza veljavna in torej izpeljana
		Trikotnik izhodiscniTrikotnik = pridobiTrikotnik(poteza.vrniIzhodisce(), poteza.vrniIgralca());
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		
		// System.out.println(izhodiscniTrikotnik);
		// System.out.println(ciljniTrikotnik);
		
		// System.out.println(poteza.vrniIzhodisce());
		// System.out.println(poteza.vrniCilj());
		
		if (izhodiscniTrikotnik.barvaFigur != poteza.vrniIgralca()) return false;  // če na izhodiščnem trikotniku sploh niso ta prave figure (a bi blo boljš kr throwat kak exception?)
		// if (ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1) return false;  // ne moremo prestaviti tja (tam so nasprotnikove figure)
		if (potezaNiVeljavna(poteza)) return false;  // sicer zdej dvakrat definiramo "ciljniTrikotnik", ampak nima veze
		
		izhodiscniTrikotnik.odstraniFiguro();
		boolean zbijanje = ciljniTrikotnik.dodajFiguro(poteza.vrniIgralca());
		if (zbijanje) {
			if (poteza.vrniIgralca().pridobiNasprotnika() == Figura.BELA) {
				belaBariera.dodajFiguro(Figura.BELA);
			} else if (poteza.vrniIgralca().pridobiNasprotnika() == Figura.CRNA) {
				crnaBariera.dodajFiguro(Figura.CRNA);
			}
		}
		
		return true;  // poteza je bila uspešno odigrana
	}
	
	
	public boolean crniImaVseVHomeBoardu() {
		for (int i = 0; i < 18; i++) {  // od 18 do 23 je home board od črnega
			Trikotnik trikotnik = plosca[i];
			if (trikotnik.barvaFigur == Figura.CRNA) return false;
		}
		return true;
	}
	
	
	public boolean beliImaVseVHomeBoardu() {
		for (int i = 6; i < 24; i++) {  // od 0 do 5 je home board od belega
			Trikotnik trikotnik = plosca[i];
			if (trikotnik.barvaFigur == Figura.BELA) return false;
		}
		return true;
	}
	
	
	public boolean beliImaPrazenZacetek() {
		return belaBariera.stevilo == 0;
	}
	
	
	public boolean crniImaPrazenZacetek() {
		return crnaBariera.stevilo == 0;
	}
	
	
	public boolean beliImaVseNaCilju() {
		if (beliCilj.stevilo > 15) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		return beliCilj.stevilo == 15;
	}
	
	
	public boolean crniImaVseNaCilju() {
		if (crniCilj.stevilo > 15) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		return crniCilj.stevilo == 15;
	}
	
	
	static public int relativnoVPoljePoteze(int relativno, Figura igralec) {  // poljePoteze je v nekem smislu 'absolutno'
		// relativno:
		// 0 je vedno bariera, 1 je trikotnik takoj zanjo, ..., 25 je cilj
		// ne glede na barvo figure
		if (igralec == Figura.CRNA) {
			return relativno;
		} else if (igralec == Figura.BELA) {
			return 25 - relativno;
		} else {
			throw new java.lang.RuntimeException("Napačen argument!");
		}
	}
	
	
	// inverzna metoda metodi relativnoVPoljePoteze
	static public int poljePotezeVRelativno(int poljePoteze, Figura igralec) {
		// pri poljuPoteze je 1 vedno prvi trikotnik od črnega, 0 pa ali bariera črnega ali pa cilj belega
		if (igralec == Figura.CRNA) {
			return poljePoteze;
		} else if (igralec == Figura.BELA) {
			return 25 - poljePoteze;
		} else {
			throw new java.lang.RuntimeException("Napačen argument!");
		}
	}
	
	
	public Trikotnik relativnoVTrikotnik(int relativno, Figura naseFigure) {
		int poljePoteze = IgralnaPlosca.relativnoVPoljePoteze(relativno, naseFigure);
		Trikotnik trikotnik = vrniTrikotnik(poljePoteze, naseFigure);
		return trikotnik;
	}
}
