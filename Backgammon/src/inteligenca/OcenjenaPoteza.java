package inteligenca;

import logika.Poteza;

//Kot je imel profesor na predavanjih

public class OcenjenaPoteza {
	
	Poteza poteza;
	int ocena;
	
	public OcenjenaPoteza (Poteza poteza, int ocena) {
		this.poteza = poteza;
		this.ocena = ocena;
	}
	
	public int compareTo (OcenjenaPoteza op) {
		if (this.ocena < op.ocena) return -1;
		else if (this.ocena > op.ocena) return 1;
		else return 0;
	}

	//izpisovanje
	@Override
	public String toString() {
		
		return String.valueOf(this.ocena) + " : " + this.poteza;
	}
}
