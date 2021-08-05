package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Trikotnik;

public class OceniPolje {

	/**
	 * Board evaluation function, kaj upoštevati:
	 * 		- Lokacija žetonov
	 * 		- Koliko žetonov
	 * 		- Žetoni v barieri
	 * 
	 */
	
	
	public static int ocenaTrikotnik(Trikotnik trikotnik, Igralec igralec, int mesto) {
		
		/**
		 * Potrebna orientacija
		 * 
		 * if(trikotnik.stevilo == 0){
		 * 		return 0;
		 * }
		 * if(trikotnik.stevilo == 1){
		 * 		//Samotna figura se oceni precej slabše
		 * 		return utežSamoEna * (oddaljenost od starta):
		 *}
		 *else{
		 *		return utež * (koliko mest od starta) * (trikotnik.stevilo);
		 *}
		 * 
		 */
		
		return 0;
	}
	
	
	
	public static int ocenaIgre(Igra igra, Igralec igralec) {
		
		/**
		 * Naredi zanko skozi seznam igralnaPlosca.plosca[mesto]
		 * 
		 * int ocena = 0;
		 * 
		 * for (int mesto = 0; mesto < 24 ; mesto ++){
		 * 		ocena += ocenaTrikotnik(igra.igralnaPlosca.plosca[mesto], mesto);
		 * }
		 * 
		 * return ocena;
		 * 
		 */
		
		return 0;
	}
}
