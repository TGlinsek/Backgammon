package grafika;

import java.util.Map;

import javax.swing.SwingWorker;

import inteligenca.Inteligenca;

import java.util.concurrent.TimeUnit;

import logika.Igra;
import logika.Igralec;
import logika.ImeKocke;
import logika.Poteza;
import logika.StanjeIgre;
import splosno.KdoIgra;
import splosno.Koordinati;

public class Vodja {	
	
	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	// public static Map<Igralec, KdoIgra> kdoIgra;  // tega verjetno ne bomo rabili
	
	public static Okno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	private static Igralec igralecKiZacne = Igralec.CRNI;  // default nastavitve
	private static boolean crniGreVSmeriUrinegaKazalca = true;  // default nastavitve
	private static boolean crniZacneSpodaj = true;  // default nastavitve
	
	public static void igramoNovoIgro() {
		igra = new Igra(igralecKiZacne, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);  // nastavi velikost (brez parametra za default)
		if (igralecKiZacne == null) {
			sKockamiDolociZacetnegaIgralca();
		}
		okno.nastaviVelikostPoljVPlatnu();
		igramo();
	}
	
	public static void nastaviZacetnoBarvo(Igralec igralec) {
		igralecKiZacne = igralec;
	}
	
	public static void nastaviStran(boolean leva) {
		crniGreVSmeriUrinegaKazalca = leva != crniZacneSpodaj;
	}
	
	public static void nastaviPolovico(boolean crniZacneSpodaj1) {
		crniZacneSpodaj = crniZacneSpodaj1;
	}
	
	public static void sKockamiDolociZacetnegaIgralca() {
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
		igra.trenutnoStanje = StanjeIgre.METANJE_KOCK;  // priènemo z igro
	}
	
	public static void igramo() {
		okno.osveziGUI();
		// igra.spremeniStanjeIgre();
		
		switch (igra.trenutnoStanje) {
		case ZMAGA_CRNI: return;
		case ZMAGA_BELI: return;
		case METANJE_KOCK: 
			Igralec igralec = igra.igralecNaVrsti;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			clovekNaVrsti = false;  // èe tega ne bi bilo, bi lahko uporabnik "prenesel" potezo iz enega naèina igre v drugega, kar pa se ne sme zgoditi
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		case PREMIKANJE_FIGUR: throw new java.lang.RuntimeException("To bi se moralo že izvesti in spremeniti nazaj na metanje kock.");
		case IZBIRA_ZACETNEGA_IGRALCA: throw new java.lang.RuntimeException("Na tem mestu smo verjetno že èez to fazo.");
		default: throw new java.lang.RuntimeException("Napaka");
		}
	}
	
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca();
	
	
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetkaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {

			@Override
			protected Poteza doInBackground() {
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				
				// try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				try {TimeUnit.MILLISECONDS.sleep(1000);} catch (Exception e) {};
				
				return poteza;
			}
			@Override
			protected void done () {
				Poteza poteza = null;
				try {poteza = get();} catch (Exception e) {return;};
				
				if (igra == zacetkaIgra) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
	}
	
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo();
	}
}