package logika;

public class Poteza {  // 0 je izhodisce crnega oz. cilj belega, 25 je izhodisce belega oz. cilj crnega
	public int izhodisce;  // od tu jemljemo figuro
	public int premik;  // to je razlika med ciljem in izhodiscem
	// lahko sta to tudi barieri ali kar cilja
	// 0 je bariera crnega oz. cilj belega (torej, �e je izhodi��e 0 in premik 4, potem je to poteza, ki figuro vzame iz bariere in jo premakne za 4 polja naprej)
	// 25 je bariera belega oz. cilj crnega
	public Figura igralec;  // igralec, �igar poteza je to (oz. smer, v katero gremo)
	
	
	public Poteza(int izhodisce, int premik, Figura igralec) {
		this.izhodisce = izhodisce;
		this.premik = premik;  // vedno pozitiven
		this.igralec = igralec;
	}
	
	@Override
	public String toString() {
		return "Izh: " + izhodisce + " Premik: " + premik + " Igralec: " + this.igralec;
	}
	
	// absolutno polje 0 je vedno tisto polje, kjer bi bila bariera crnega, ce bi crni zacel desno spodaj
	// relativno polje 0 je vedno bariera crnega
	// poteze bodo vse relativne
	
	public int vrniIzhodisce() {
		return this.izhodisce;
	}
	
	public int vrniCilj() {
		return this.izhodisce + this.premik;
	}
	
	public Figura vrniIgralca() {
		return this.igralec;
	}
	
	/* ne bomo rabli te metode:
	public static Poteza pristejDvePotezi(Poteza prva, Poteza druga) {  // se�teje dve potezi (se�teje njun premik), katerih izhodi��e je enako
		// predpostavljamo, da je prva poteza veljavna (druga�e tako te metode ne bomo klicali)
		// if (prva.vrniCilj() != druga.izhodisce || prva.igralec != druga.igralec) throw new java.lang.RuntimeException("To pa ne gre.");
		if (prva.vrniIzhodisce() != druga.vrniIzhodisce()) throw new java.lang.RuntimeException("Ta metoda se�teje le potezi z istima izhodi��ema.");
		return new Poteza(prva.izhodisce, prva.premik + druga.premik, prva.igralec);
	}
	*/
	
	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Poteza p = (Poteza) o;
		return this.izhodisce == p.izhodisce && this.premik == p.premik && this.igralec == p.igralec;
	}
}
