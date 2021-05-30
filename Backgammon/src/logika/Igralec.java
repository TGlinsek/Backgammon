package logika;

public enum Igralec {
	BELI, CRNI;
	
	public Igralec pridobiNasprotnika() {  // a je ta metoda v redu / koristna?
		return this == CRNI ? BELI : CRNI;
	}
}
