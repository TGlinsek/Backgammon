package logika;

public class Poteza {  // 0 je izhodisce crnega oz. cilj belega, 25 je izhodisce belega oz. cilj crnega
	public int izhodisce;  // od tu jemljemo figuro
	public int premik;  // to je razlika med ciljem in izhodiscem
	// lahko sta to tudi barieri ali kar cilja
	// 0 je bariera crnega oz. cilj belega (torej, če je izhodišče 0 in premik 4, potem je to poteza, ki figuro vzame iz bariere in jo premakne za 4 polja naprej)
	// 25 je bariera belega oz. cilj crnega
	public Figura igralec;  // igralec, čigar poteza je to (oz. smer, v katero gremo)
	
	
	public Poteza(int izhodisce, int premik, Figura igralec) {
		this.izhodisce = izhodisce;
		this.premik = premik;  // vedno pozitiven
		if (this.premik <= 0) throw new java.lang.RuntimeException("Neustrezen premik: " + this.premik);
		
		this.igralec = igralec;
		if (this.igralec == Figura.PRAZNA) throw new java.lang.RuntimeException("Neustrezen igralec: " + this.igralec);
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
	
	public Integer vrniCilj() {
		return this.izhodisce + this.premik * (this.vrniIgralca() == Figura.CRNA ? 1 : -1);
	}
	
	public Figura vrniIgralca() {
		return this.igralec;
	}
	
	/* ne bomo rabli te metode:
	public static Poteza pristejDvePotezi(Poteza prva, Poteza druga) {  // sešteje dve potezi (sešteje njun premik), katerih izhodišče je enako
		// predpostavljamo, da je prva poteza veljavna (drugače tako te metode ne bomo klicali)
		// if (prva.vrniCilj() != druga.izhodisce || prva.igralec != druga.igralec) throw new java.lang.RuntimeException("To pa ne gre.");
		if (prva.vrniIzhodisce() != druga.vrniIzhodisce()) throw new java.lang.RuntimeException("Ta metoda sešteje le potezi z istima izhodiščema.");
		return new Poteza(prva.izhodisce, prva.premik + druga.premik, prva.igralec);
	}
	*/
	
	
	public void dodaj(Poteza p) {  // za igralca od obeh potez se tukaj predpostavlja, da sta enaka
		if (this.izhodisce != p.izhodisce) throw new java.lang.RuntimeException("Izhodišči potez morata biti enaki! To se ne bi smelo zgoditi ...");
		this.premik += p.premik;
	}
	
	
	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Poteza p = (Poteza) o;
		return this.izhodisce == p.izhodisce && this.premik == p.premik && this.igralec == p.igralec;
	}
}
