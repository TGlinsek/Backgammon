package logika;

public class Trikotnik {
	
	public Figura barvaFigur;
	public int stevilo;
	
	public Trikotnik(Figura barvaFigur, int stevilo) {
		if (barvaFigur != Figura.PRAZNA && stevilo == 0) throw new java.lang.RuntimeException("To se ne sme zgoditi.");
		this.barvaFigur = barvaFigur;
		this.stevilo = stevilo;
	}
	
	public Trikotnik(Trikotnik original) {
		this.barvaFigur = original.barvaFigur;
		this.stevilo = original.stevilo;
	}
	
	public boolean dodajFiguro(Figura barva) {
		boolean zbijanje = false;
		if (this.stevilo == 1 && this.barvaFigur != barva) {  // zbijamo
			zbijanje = true;
			this.stevilo = 0;
		}
		if (this.stevilo == 0) this.barvaFigur = barva;  // zasedemo nov trikotnik, torej spremenimo barvo iz PRAZNO v barvaFigur
		this.stevilo += 1; // dodaj figuro
		return zbijanje;
	}
	
	public void odstraniFiguro() {
		if (this.stevilo == 0) throw new java.lang.RuntimeException("Ne moreš odstraniti ničesar, saj je trikotnik prazen!");
		this.stevilo -= 1;
		if (this.stevilo == 0) this.barvaFigur = Figura.PRAZNA;
	}
	
	/*
	// tega verjetno ne bomo rabli
	public void spremeniBarvoFigur(Figura novaBarva) {
		this.barvaFigur = novaBarva;
		if (this.stevilo != 1) throw new java.lang.RuntimeException("število mora biti 1, da se lahko stanje spremeni");  // pač, zbijanje figur
	}
	*/
	
	@Override
	public String toString() {
		return Integer.toString((barvaFigur == Figura.CRNA ? 1 : (barvaFigur == Figura.BELA ? -1 : 0)) * stevilo);
	}
}
