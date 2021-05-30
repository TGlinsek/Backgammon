package logika;

import java.util.Arrays;

public class IgralnaPlosca {
	// ne kock, vseeno pa opazuj, koliko jih je še na zaèetku oz. na koncu
	// public Trikotnik[] plosca = new Trikotnik[24];
	/*
	public Trikotnik[] plosca = {
		new Trikotnik(Figura.PRAZNA, 0),
		new Trikotnik(Figura.PRAZNA, 0)
	};
	 */
	public Trikotnik[] plosca;  // indeksi od 0 do 23
	
	public Trikotnik belaBariera = new Trikotnik(Figura.BELA, 0);
	public Trikotnik crnaBariera = new Trikotnik(Figura.CRNA, 0);
	public Trikotnik beliCilj = new Trikotnik(Figura.BELA, 0);
	public Trikotnik crniCilj = new Trikotnik(Figura.CRNA, 0);
	
	
	private static Trikotnik[] vrniEnoPolovicoPlosce(Figura zacetna, Figura koncna) {
		if (zacetna == Figura.PRAZNA || koncna == Figura.PRAZNA) throw new java.lang.RuntimeException("Figura ne more biti prazna tukaj!");
		if (zacetna == koncna) throw new java.lang.RuntimeException("Figuri morata biti razlièni!");
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
	
	
	public IgralnaPlosca() {
		plosca = zdruziDvePolovici(vrniEnoPolovicoPlosce(Figura.CRNA, Figura.BELA), vrniObrnjenoPolovicoPlosce(Figura.BELA, Figura.CRNA));
	}  // popravil sem, da zdaj igralnaPlosca zaène vedno na èrnem štartu, ne glede na to, kje èrni zaène
	
	
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
		if (relativnoPolje == 0) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crnaBariera;
			} else {
				return belaBariera;
			}
		} else if (relativnoPolje >= 25) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crniCilj;
			} else {
				return beliCilj;
			}
		} else {
			return this.plosca[relativnoPolje - 1];  // pri plosci ne upoštevamo bariere, zato se vse za eno zamakne
		}
	}
	
	
	public boolean potezaNiVeljavna(Poteza poteza) {
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		return ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1;
	}
	
	
	public boolean igrajPotezo(Poteza poteza) {  // vrne true iff je bila poteza veljavna in torej izpeljana
		Trikotnik izhodiscniTrikotnik = pridobiTrikotnik(poteza.vrniIzhodisce(), poteza.vrniIgralca());
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(poteza.vrniCilj(), poteza.vrniIgralca());
		
		if (izhodiscniTrikotnik.barvaFigur != poteza.vrniIgralca()) return false;  // èe na izhodišènem trikotniku sploh niso ta prave figure (a bi blo boljš kr throwat kak exception?)
		if (ciljniTrikotnik.barvaFigur == poteza.vrniIgralca().pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1) return false;  // ne moremo prestaviti tja
		
		izhodiscniTrikotnik.odstraniFiguro();
		ciljniTrikotnik.dodajFiguro(poteza.vrniIgralca());
		
		return true;  // poteza je bila uspešno odigrana
	}
}
