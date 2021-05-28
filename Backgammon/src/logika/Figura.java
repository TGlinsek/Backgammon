package logika;

public enum Figura {
	BELA, CRNA, PRAZNA;
	
	public Figura pridobiNasprotnika() {  // a je ta metoda v redu / koristna?
		return this == PRAZNA ? PRAZNA : (this == BELA ? CRNA : BELA);
	}
}
