package logika;

public enum Igralec {
	BELI, CRNI;
	
	public Igralec pridobiNasprotnika() {  // a je ta metoda v redu / koristna?
		return this == CRNI ? BELI : CRNI;
	}
	
	public Figura pridobiFiguro() {
		return this == CRNI ? Figura.CRNA : Figura.BELA;
	}
	
	
	public String toString() {
		return this == CRNI ? "Igralec 2" : "Igralec 1";
	}
}
