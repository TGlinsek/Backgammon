package logika;

public class Trikotnik {
	
	Figura barvaFigur;
	int stevilo;
	
	public Trikotnik(Figura barvaFigur, int stevilo) {
		this.barvaFigur = barvaFigur;
		this.stevilo = stevilo;
	}
	
	public Trikotnik(Trikotnik original) {
		this.barvaFigur = original.barvaFigur;
		this.stevilo = original.stevilo;
	}
	
	public void dodajFiguro(Figura barva) {
		if (this.stevilo == 1 && this.barvaFigur != barva) {  // zbijamo
			this.stevilo = 0;
		}
		if (this.stevilo == 0) this.barvaFigur = barva;
		this.stevilo += 1;
	}
	
	public void odstraniFiguro() {
		// if (this.stevilo == 0) throw new java.lang.RuntimeException("Ne moreš odstraniti nièesar, saj je trikotnik prazen!");
		this.stevilo -= 1;
		if (this.stevilo == 0) this.barvaFigur = Figura.PRAZNA;
	}
	
	// tega verjetno ne bomo rabli
	public void spremeniBarvoFigur(Figura novaBarva) {
		this.barvaFigur = novaBarva;
		if (this.stevilo != 1) throw new java.lang.RuntimeException("Število mora biti 1, da se lahko stanje spremeni");
	}
	
	@Override
	public String toString() {
		return Integer.toString((barvaFigur == Figura.CRNA ? 1 : (barvaFigur == Figura.BELA ? -1 : 0)) * stevilo);
	}
}
