package tekstovni_vmesnik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import logika.Igra;
import logika.Igralec;
import logika.ImeKocke;
import logika.Poteza;

public class Vodja {
private static BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
	
	private static Map<Igralec, VrstaIgralca> vrstaIgralca;
	
	private static void napolniSlovarZaVrsteIgralcev(VrstaIgralca beliIgralec, VrstaIgralca crniIgralec) {
		vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
		vrstaIgralca.put(Igralec.BELI, beliIgralec);
		vrstaIgralca.put(Igralec.CRNI, crniIgralec);
	}
	
	public static void igramo () throws IOException {
		while (true) {
			{
				System.out.println("Nova igra. Prosim, da izberete:");
				System.out.println(" 1 - èrni je èlovek, beli je raèunalnik");
				System.out.println(" 2 - èrni je raèunalnik, beli je èlovek");
				System.out.println(" 3 - èrni je èlovek, beli je èlovek");
				System.out.println(" 4 - izhod");
				String s = r.readLine();
				if (s.equals("1")) {
					napolniSlovarZaVrsteIgralcev(VrstaIgralca.RACUNALNIK, VrstaIgralca.CLOVEK);
				} else if (s.equals("2")) {
					napolniSlovarZaVrsteIgralcev(VrstaIgralca.CLOVEK, VrstaIgralca.RACUNALNIK);
				} else if (s.equals("3")) {
					napolniSlovarZaVrsteIgralcev(VrstaIgralca.CLOVEK, VrstaIgralca.CLOVEK);
				} else if (s.equals("4")) {
					System.out.println("Nasvidenje!");
					break;
				} else {
					System.out.println("Vnos ni veljaven");
					continue;
				}
				// Èe je s == "1", "2" ali "3"
			}
			
			

			
			
			// boolean zacnemoNaVzhodu;
			boolean crniGreVSmeriUrinegaKazalca;
			boolean crniZacneSpodaj;
			{
				System.out.println("Nova igra. Prosim, da izberete:");
				System.out.println(" 1 - èrni gre SE -> NE");
				System.out.println(" 2 - èrni gre NE -> SE");
				System.out.println(" 3 - èrni gre SW -> NW");
				System.out.println(" 4 - èrni gre NW -> SW");
				System.out.println(" 5 - daj mi default");
				System.out.println(" 6 - zbogom!");
				String ss = r.readLine();
				if (ss.equals("1")) {
					// zacnemoNaVzhodu = true;
					crniGreVSmeriUrinegaKazalca = true;
					crniZacneSpodaj = true;
				} else if (ss.equals("2")) {
					// zacnemoNaVzhodu = true;
					crniGreVSmeriUrinegaKazalca = false;
					crniZacneSpodaj = false;
				} else if (ss.equals("3")) {
					// zacnemoNaVzhodu = false;
					crniGreVSmeriUrinegaKazalca = false;
					crniZacneSpodaj = true;
				} else if (ss.equals("4")) {
					// zacnemoNaVzhodu = false;
					crniGreVSmeriUrinegaKazalca = true;
					crniZacneSpodaj = false;
				} else if (ss.equals("5")) {
					// default nastavitve
					crniGreVSmeriUrinegaKazalca = true;
					crniZacneSpodaj = true;
				} else if (ss.equals("6")) {
					System.out.println("Nasvidenje!");
					break;
				} else {
					System.out.println("Vnos ni veljaven");
					continue;
				}
				
			}
			
			
			Igra igra;
			{
				System.out.println("Nova igra. Prosim, da izberete:");
				System.out.println(" 1 - zaène èrni");
				System.out.println(" 2 - zaène beli");
				System.out.println(" 3 - doloèitev s kockami");
				System.out.println(" 4 - izhod");
				String sss = r.readLine();
				if (sss.equals("1")) {
					igra = new Igra(Igralec.CRNI, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
				} else if (sss.equals("2")) {
					igra = new Igra(Igralec.BELI, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
				} else if (sss.equals("3")) {
					igra = new Igra(null, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);
				} else if (sss.equals("4")) {
					System.out.println("Nasvidenje!");
					break;
				} else {
					System.out.println("Vnos ni veljaven");
					continue;
				}
			}

			igranje : while (true) {
				switch (igra.trenutnoStanje) {
				case ZMAGA_BELI:
					System.out.println("Zmagal je beli igralec!");
					// System.out.println("Zmagovalne vrste: " + igra.zmagovalneVrste.toString());
					break igranje;
				case ZMAGA_CRNI:
					System.out.println("Zmagal je èrni igralec!");
					// System.out.println("Zmagovalne vrste: " + igra.zmagovalneVrste.toString());
					break igranje;
				case IZBIRA_ZACETNEGA_IGRALCA:
					System.out.println("Igra se prièenja!");
					if (igra.igralecNaVrsti == null) {  // èe pustimo, da kocka odloèi zaèetnika
						metanjeKock : while (true) {
							igra.vrziKocki(true);
							
							int vrednostPrveKocke = igra.pridobiVrednostKocke(ImeKocke.PRVA_KOCKA);
							int vrednostDrugeKocke = igra.pridobiVrednostKocke(ImeKocke.DRUGA_KOCKA);
							System.out.println("Vrednost prve kocke: " + vrednostPrveKocke);
							System.out.println("Vrednost druge kocke: " + vrednostDrugeKocke);
							
							// switch (igra.primerjajKocki(ImeKocke.PRVA_KOCKA, ImeKocke.DRUGA_KOCKA)) {
							switch (Integer.compare(vrednostPrveKocke, vrednostDrugeKocke)) {
							case 1:
								igra.igralecNaVrsti = igra.pridobiLastnikaKocke(ImeKocke.PRVA_KOCKA);
								System.out.println("Prvi igralec zaène!");
								break;
							case -1:
								igra.igralecNaVrsti = igra.pridobiLastnikaKocke(ImeKocke.DRUGA_KOCKA);
								System.out.println("Drugi igralec zaène!");
								break;
							case 0:
								System.out.println("Neodloèeno! Ponovimo metanje ...");
								continue metanjeKock;
							}
							break metanjeKock;
						}
					}
					break igranje;
				case METANJE_KOCK:
					Igralec igralec = igra.igralecNaVrsti;
					VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
					
					metanje : switch (vrstaNaPotezi) {
					case CLOVEK:
						System.out.println("Kakšno kocko boš vrgel?");
						System.out.println("1 - dve navadni");
						System.out.println("2 - double");
						String ssss = r.readLine();
						if (ssss.equals("1")) {
							igra.vrziKocki(false);
						} else if (ssss.equals("2")) {
							igra.vrziDvojnoKocko();
						} else {
							System.out.println("Vnos ni veljaven");
							continue;
						}
						break metanje;
					case RACUNALNIK:
						System.out.println("Raèunalnik je izbral navadni kocki!");
						igra.vrziKocki(false);
						break metanje;
					}
					System.out.println("Prva kocka: " + igra.pridobiVrednostKocke(ImeKocke.PRVA_KOCKA) + 
							", druga kocka: " + igra.pridobiVrednostKocke(ImeKocke.DRUGA_KOCKA));
					
					
					break;
				case PREMIKANJE_FIGUR:
					Igralec igralec2 = igra.igralecNaVrsti;
					VrstaIgralca vrstaNaPotezi2 = vrstaIgralca.get(igralec2);
					Poteza poteza = null;
					
					
					
					premikanje : switch (vrstaNaPotezi2) {
					case CLOVEK:
						System.out.println("Možne poteze: " + igra.vrniVeljavnePoteze());
						poteza = clovekovaPoteza(igra);
						break premikanje;
					case RACUNALNIK:
						poteza = racunalnikovaPoteza(igra);
						break premikanje;
					}
					System.out.println(igra);
					System.out.println("Igralec " + igralec2 + " je igral " + poteza);
					break;
				}
				
			}
		}
	}
	
	
	// private static Random random = new Random ();
	
	
	public static Poteza racunalnikovaPoteza(Igra igra) {
		return null;  // vrnemo nekaj samo zato, ker paè moramo
	}
	
	
	public static Poteza clovekovaPoteza(Igra igra) throws IOException {
		while (true) {
			System.out.println("Igralec " + igra.igralecNaVrsti.toString() +
					" vnesite potezo \"x y\"");
			String s = r.readLine();
			int i = s.indexOf(' ');  // pridobimo indeks presledka v nizu s
			if (i == -1 || i  == s.length()) {
				System.out.println("Napaèen format"); continue;
			}
			String xString = s.substring(0, i);
			String yString = s.substring(i + 1);
			int x, y;
			try {
				x = Integer.parseInt(xString);
				y = Integer.parseInt(yString);
			} catch (NumberFormatException e) {
				System.out.println("Napaèen format"); continue;
			}
			// x bo številka trikotnika, y bo za kolk naprej gremo
			Poteza poteza = new Poteza(x, y);  // igra.pridobiTrikotnik(x)
			if (igra.igraj(poteza)) return poteza;
			System.out.println(poteza.toString() + " ni možna");
		}
	}
}
