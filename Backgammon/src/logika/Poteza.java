package logika;

/*
public class Poteza {
	public Trikotnik izhodisce;  // od tu jemljemo figuro
	public Trikotnik ciljniTrikotnik;  // sem ga dajemo
	// lahko sta to tudi barieri ali kar cilja
	
	// ali bi bilo bolje to nardit z enim trikotnikom in enim intom?
	
	public Poteza(Trikotnik izhodisce, Trikotnik ciljniTrikotnik) {
		this.izhodisce = izhodisce;
		this.ciljniTrikotnik = ciljniTrikotnik;
	}
	
	@Override
	public String toString() {
		return "" + izhodisce + " " + ciljniTrikotnik;
	}
}
*/

public class Poteza {  // 0 je izhodisce crnega oz. cilj belega, 25 je izhodisce belega oz. cilj crnega
	public int izhodisce;  // od tu jemljemo figuro
	public int premik;  // to je razlika med ciljem in izhodiscem
	// lahko sta to tudi barieri ali kar cilja
	// 0 je bariera crnega oz. cilj belega
	// 25 je bariera belega oz. cilj crnega
	
	public Poteza(int izhodisce, int premik) {
		this.izhodisce = izhodisce;
		this.premik = premik;
	}
	
	@Override
	public String toString() {
		return "" + izhodisce + " " + premik;
	}
	
	private static int mod(int x, int y)  // https://stackoverflow.com/questions/90238/whats-the-syntax-for-mod-in-java
	{
	    int result = x % y;
	    return result < 0 ? result + y : result;
	}
	
	private static int transformirajPozicijo(int pozicija, boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj) {
		// pozicija = absolutno polje
		if (crniGreVSmeriUrinegaKazalca) {
			if (crniZacneSpodaj) {
				return pozicija;
			} else {
				return mod(pozicija + 13, 26);
			}
		} else {
			if (crniZacneSpodaj) {
				return mod(13 - pozicija, 26);  // èe je prvi argument pod 0, prištejemo 26
			} else {
				return 26 - pozicija;
			}
		}
	}
	
	public static Poteza vrniPotezoGledeNaKoordinate(int izhodisce, int cilj, boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj) {  // parametri grejo od 1 do 24 po vrsti od SE, SW, NW, NE - ne glede na to, kje crni zacne
		int novoIzhodisce = transformirajPozicijo(izhodisce, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
		int novCilj = transformirajPozicijo(cilj, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
		return new Poteza(
				novoIzhodisce,
				novCilj - novoIzhodisce  // premik (negativen natanko takrat, ko je poteza od belega)
		);
	}
	
	// to je samo inverz metode transformirajPozicijo - izkaže se, da je praktièno enaka metoda
	public static int pridobiPolje(int relativnoPolje, boolean crniGreVSmeriUrinegaKazalca, boolean crniZacneSpodaj) {
		int absolutnoPolje;
		if (crniGreVSmeriUrinegaKazalca) {
			if (crniZacneSpodaj) {
				absolutnoPolje = relativnoPolje;
			} else {
				absolutnoPolje = mod(relativnoPolje + 13, 26);  // kar je isto kot mod(relativnoPolje - 13, 26)
			}
		} else {
			if (crniZacneSpodaj) {
				absolutnoPolje = mod(13 - relativnoPolje, 26);  // èe je prvi argument pod 0, prištejemo 26
			} else {
				absolutnoPolje = 26 - relativnoPolje;
			}
		}
		return absolutnoPolje;
	}
	
	// absolutno polje 0 je vedno tisto polje, kjer bi bila bariera crnega, ce bi crni zacel desno spodaj
	// relativno polje 0 je vedno bariera crnega
	
	public int vrniIzhodisce() {
		return this.izhodisce;
	}
	
	public int vrniCilj() {
		return this.izhodisce + this.premik;
	}
}
