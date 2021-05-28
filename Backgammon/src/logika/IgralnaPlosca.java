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
	
	
	public IgralnaPlosca(boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj) {  // prvi trikotniki so desno spodaj, nato levo spodaj, nato levo zgoraj, nato desno zgoraj
		boolean zacnemoNaDesni = crniGreVSmeriUrinegaKazalca == crniZacneSpodaj;  // xnor
		// crniGreVSmeriUrinegaKazalca = zacnemoNaDesni == crniZacneSpodaj;  èe hoèemo nazaj pretvorit
		/*
		plosca = new Trikotnik[] {
				new Trikotnik(Figura.PRAZNA, 0),
				new Trikotnik(Figura.PRAZNA, 0)
				// itd.
		};
		*/
		if (zacnemoNaDesni) {
			if (crniZacneSpodaj) {
				plosca = zdruziDvePolovici(vrniEnoPolovicoPlosce(Figura.CRNA, Figura.BELA), vrniObrnjenoPolovicoPlosce(Figura.BELA, Figura.CRNA));
			} else {
				plosca = zdruziDvePolovici(vrniEnoPolovicoPlosce(Figura.BELA, Figura.CRNA), vrniObrnjenoPolovicoPlosce(Figura.CRNA, Figura.BELA));
			}
		} else {
			if (crniZacneSpodaj) {
				plosca = zdruziDvePolovici(vrniObrnjenoPolovicoPlosce(Figura.CRNA, Figura.BELA), vrniEnoPolovicoPlosce(Figura.BELA, Figura.CRNA));
			} else {
				plosca = zdruziDvePolovici(vrniObrnjenoPolovicoPlosce(Figura.BELA, Figura.CRNA), vrniEnoPolovicoPlosce(Figura.CRNA, Figura.BELA));
			}
		}
	}
	
	
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
	
	/*
	private Figura vrniFiguro(Trikotnik trikotnik, Igralec igralecNaVrsti) {
		if (trikotnik.barvaFigur != BarvaIgralca.barva(igralecNaVrsti) && trikotnik.stevilo == 1) return Figura.PRAZNA;
		return trikotnik.barvaFigur;
	}
	
	public Figura[] vrniPoenostavljenoPlosco(Igralec igralecNaVrsti) {
		
		
		Figura[] seznam = new Figura[25];  // 25, ker gledamo še bariero (nulto polje) (ampak le od enega igralca, zato ni 26)
		
		if (igralecNaVrsti == Igralec.BELI) {
			seznam[0] = vrniFiguro(belaBariera, igralecNaVrsti);
		} else {
			seznam[0] = vrniFiguro(crnaBariera, igralecNaVrsti);
		}
		/*
		for (Trikotnik : this.plosca) {
			
		}
		*//*
	}
	*/
	
	
	private Trikotnik pridobiTrikotnik(int absolutnoPolje, Figura igralecNaVrsti) {
		if (absolutnoPolje == 0) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crnaBariera;
			} else {
				return belaBariera;
			}
		} else if (absolutnoPolje == 25) {
			if (igralecNaVrsti == Figura.CRNA) {
				return crniCilj;
			} else {
				return beliCilj;
			}
		} else {
			return this.plosca[absolutnoPolje - 1];  // pri plosci ne upoštevamo bariere, zato se vse za eno zamakne
		}
	}
	
	
	public boolean igrajPotezo(Poteza poteza, boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj, Figura igralecNaVrsti) {  // vrne true iff je bila poteza veljavna in torej izpeljana
		int absolutnoIzhodisce = Poteza.pridobiPolje(poteza.vrniIzhodisce(), crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
		int absolutniCilj = Poteza.pridobiPolje(poteza.vrniCilj(), crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
		
		Trikotnik izhodiscniTrikotnik = pridobiTrikotnik(absolutnoIzhodisce, igralecNaVrsti);
		Trikotnik ciljniTrikotnik = pridobiTrikotnik(absolutniCilj, igralecNaVrsti);
		
		if (izhodiscniTrikotnik.barvaFigur != igralecNaVrsti) return false;  // èe na izhodišènem trikotniku sploh niso ta prave figure (a bi blo boljš kr throwat kak exception?)
		if (ciljniTrikotnik.barvaFigur == igralecNaVrsti.pridobiNasprotnika() && ciljniTrikotnik.stevilo > 1) return false;  // ne moremo prestaviti tja
		
		izhodiscniTrikotnik.odstraniFiguro();
		ciljniTrikotnik.dodajFiguro(igralecNaVrsti);
		
		return true;  // poteza je bila uspešno odigrana
	}
}
