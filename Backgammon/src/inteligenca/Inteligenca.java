package inteligenca;

import java.util.List;

import logika.Figura;
import logika.Igra;
import logika.Igralec;
import logika.IgralnaPlosca;
import logika.Poteza;
import logika.Trikotnik;

public class Inteligenca {
	
	// navdihnjeno po https://www.thesprucecrafts.com/how-to-win-at-backgammon-411041
	
	public Igralec cigavaPoteza;
	public IgralnaPlosca plosca;
	public List<Poteza> moznePoteze;
	
	
	public Inteligenca(Igra igra) {
		this.cigavaPoteza = igra.igralecNaVrsti;
		this.plosca = igra.igralnaPlosca;
		this.moznePoteze = igra.vrniVeljavnePotezeTePlosce();
	}
	
	
	private int steviloFigurNaIzhodiscnemTrikotniku(Poteza poteza) {
		Trikotnik trikotnik = plosca.vrniTrikotnik(poteza.vrniIzhodisce(), poteza.vrniIgralca());
		if (trikotnik.barvaFigur != poteza.igralec) throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
		return trikotnik.stevilo;
	}
	
	
	private int steviloFigurNaCiljnemTrikotniku(Poteza poteza) {
		// vrne negativno, če tuja figura, drugače pozitivno
		Trikotnik trikotnik = plosca.vrniTrikotnik(poteza.vrniIzhodisce(), poteza.vrniIgralca());
		return (trikotnik.barvaFigur == poteza.igralec ? 1 : (trikotnik.barvaFigur != Figura.PRAZNA ? -1 : 0)) * trikotnik.stevilo;
	}
	
	
	private int oddaljenostCiljnegaTrikotnikaOdNasprotnikovegaDoma(Poteza poteza) {
		// vrednosti od 1 do 24, vključno
		int relativno = IgralnaPlosca.poljePotezeVRelativno(poteza.vrniCilj(), poteza.igralec);  // manjša kot je ta vrednost, večja bo oddaljenost od nasprotnikovega doma
		if (relativno >= 25) throw new java.lang.RuntimeException("Poteza, ki gre na cilj, ne more zbiti nasprotnika.");
		// 3, 4, 5, 6, 7, 8 so možne vrednosti
		return 8 - ((relativno - 1) / 4);
		// mogoče bi lahko dodali še 0.5, če je v njegovem home boardu, tj. takrat, ko je 1 <= relativno <= 6
	}
	
	
	private int kolikoNasprotnikovImaDostopDoTrikotnika(int relativno, Figura naseFigure) {
		int vsota = 0;
		for (int i = relativno + 1; i <= Math.min(relativno + 6, 25); i++) {  // 25 je dom nasprotnika, zato dlje od tega ne gremo
			Trikotnik trikotnik = plosca.relativnoVTrikotnik(i, naseFigure);
			if (trikotnik.barvaFigur.pridobiNasprotnika() == naseFigure) {
				vsota += trikotnik.stevilo;
			}
		}
		return vsota;
	}
	
	
	private int kolikoNasprotnikovVecPoIgraniPotezi(Poteza poteza) {  // če je na našem trikotniku več kot ena figura, potem nas nasprotnik ne more zbiti, zato tega ne štejemo
		return kolikoNasprotnikovImaDostopDoTrikotnika(IgralnaPlosca.poljePotezeVRelativno(poteza.vrniCilj(), poteza.igralec), poteza.igralec) -
		kolikoNasprotnikovImaDostopDoTrikotnika(IgralnaPlosca.poljePotezeVRelativno(poteza.vrniIzhodisce(), poteza.igralec), poteza.igralec);
	}
	
	
	private boolean ciljniTrikotnikJeVNasprotnikovemHomeBoardu(Poteza poteza) {
		int relativno = IgralnaPlosca.poljePotezeVRelativno(poteza.vrniIzhodisce(), poteza.igralec);
		return 1 <= relativno && relativno <= 6;  // 0 ne more biti ciljni trikotnik
	}
	
	
	private int steviloNasprotnikovNaNasiPolovici(Figura naseFigure) {
		int vsota = 0;
		for (int i = 1; i <= 12; i++) {
			Trikotnik trikotnik = plosca.relativnoVTrikotnik(i, naseFigure);
			if (trikotnik.barvaFigur.pridobiNasprotnika() == naseFigure) {
				vsota += trikotnik.stevilo;
			}
		}
		return vsota;
	}
	
	
	private int stNasihNaNasprotnikovemHomeBoardu(Figura naseFigure) {
		int vsota = 0;
		for (int i = 1; i <= 6; i++) {
			Trikotnik trikotnik = plosca.relativnoVTrikotnik(i, naseFigure);
			if (trikotnik.barvaFigur == naseFigure) {
				vsota += trikotnik.stevilo;
			}
		}
		return vsota;
	}
	
	
	private boolean vsajEnSosednjiTrikotnikOdCiljnegaTrikotnikaImaVsajDveNasiFiguri(Poteza poteza) {
		int poljePoteze = poteza.vrniCilj();
		if (poljePoteze <= 0 || poljePoteze >= 25) {
			return false;  // cilji nas ne zanimajo
		} else if (poljePoteze == 1) {  // to je prvo polje v belem home boardu, zato imamo le enega soseda
			Trikotnik trikotnik = plosca.vrniTrikotnik(2, poteza.igralec);
			if (trikotnik.barvaFigur == poteza.igralec && trikotnik.stevilo >= 2) {
				return true;
			}
		} else if (poljePoteze == 24) {  // tudi v črnem home boardu imamo le enega soseda
			Trikotnik trikotnik = plosca.vrniTrikotnik(24, poteza.igralec);
			if (trikotnik.barvaFigur == poteza.igralec && trikotnik.stevilo >= 2) {
				return true;
			}
		}
		else {
			Trikotnik trikotnik1 = plosca.vrniTrikotnik(poljePoteze - 1, poteza.igralec);
			Trikotnik trikotnik2 = plosca.vrniTrikotnik(poljePoteze + 1, poteza.igralec);
			if (trikotnik1.barvaFigur == poteza.igralec && trikotnik1.stevilo >= 2) {
				return true;
			}
			if (trikotnik2.barvaFigur == poteza.igralec && trikotnik2.stevilo >= 2) {
				return true;
			}
		}
		return false;
	}
	
	
	public Poteza izberiPotezo() {
		double najvisjaVrednost = 0;
		Poteza najboljsaPoteza = null;
		for (Poteza poteza : moznePoteze) {
			int vrednost1; 
			int stevilo1 = steviloFigurNaIzhodiscnemTrikotniku(poteza);
			
			if (stevilo1 == 0) {
				throw new java.lang.RuntimeException("Ta poteza ni ustrezna");
			} else if (stevilo1 == 1) {
				vrednost1 = 2;
			} else if (stevilo1 > 2) {
				vrednost1 = 1;
			} else if (stevilo1 == 2) {
				vrednost1 = 0;
			} else {
				throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
			}
			
			
			int vrednost2;
			int stevilo2 = steviloFigurNaCiljnemTrikotniku(poteza);
			
			if (stevilo2 == 1) {
				vrednost2 = 2;
			} else if (stevilo2 > 1) {
				vrednost2 = 1;
			} else if (stevilo2 == 0) {
				vrednost2 = 0;
			} else if (stevilo2 == -1) {  // zbijamo (the blitz)
				// vrednost2 = 5;
				vrednost2 = oddaljenostCiljnegaTrikotnikaOdNasprotnikovegaDoma(poteza);
			} else {
				throw new java.lang.RuntimeException("To se ne bi smelo zgoditi.");
			}
			
			
			double vrednost3 = 0.5 * kolikoNasprotnikovVecPoIgraniPotezi(poteza);
			
			
			double vrednost4 = vsajEnSosednjiTrikotnikOdCiljnegaTrikotnikaImaVsajDveNasiFiguri(poteza) ? 0.5 : 0;  // priming
			
			
			double pomoznaVrednost = 0.5 * steviloNasprotnikovNaNasiPolovici(poteza.igralec) + stNasihNaNasprotnikovemHomeBoardu(poteza.igralec);  // the back game
			
			int vrednost5 = 0;
			
			if (pomoznaVrednost >= 4) {
				if (ciljniTrikotnikJeVNasprotnikovemHomeBoardu(poteza) && steviloFigurNaCiljnemTrikotniku(poteza) == 1) {  // Če poteza daje na trikotnik, ki je v nasprotnikovem home boardu in kjer je že natanko ena naša figura, dodamo bonus
					vrednost5 = 3;  // dodamo majhen bonus
				}
			}
			
			
			double novaVrednost = vrednost1 + vrednost2 + vrednost3 + vrednost4 + vrednost5;
			
			if (novaVrednost > najvisjaVrednost) {
				najvisjaVrednost = novaVrednost;
				najboljsaPoteza = poteza;
			}
		}
		return najboljsaPoteza;
	}
	
}
