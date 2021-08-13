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

public class Vodja {	
	
	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	// public static Map<Igralec, KdoIgra> kdoIgra;  // tega verjetno ne bomo rabili
	
	public Okno okno;
	
	public Igra igra;
	
	// public boolean clovekNaVrsti = false;  // ni važno, kaj je na začetku
	public boolean clovekNaVrsti;  // ne rabimo v konstruktorju definirat, se bo sproti definiralo
	
	/*
	private static Igralec igralecKiZacne = Igralec.CRNI;  // default nastavitve
	private static boolean crniGreVSmeriUrinegaKazalca = true;  // default nastavitve
	private static boolean crniZacneSpodaj = true;  // default nastavitve
	*/
	
	public Vodja() {
		// igra = new Igra(igralecKiZacne, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);  // nastavi velikost (brez parametra za default)
		igra = new Igra();  // verjetno na koncu ne bo nobenih parametrov tule
		okno = new Okno();
	}
	
	
	public void igramoNovoIgro() {
		
		sKockamiDolociZacetnegaIgralca();
		
		igramo();
	}
	
	/*
	public void nastaviZacetnoBarvo(Igralec igralec) {
		igralecKiZacne = igralec;
	}
	
	public void nastaviStran(boolean leva) {
		crniGreVSmeriUrinegaKazalca = leva != crniZacneSpodaj;
	}
	
	public void nastaviPolovico(boolean crniZacneSpodaj1) {
		crniZacneSpodaj = crniZacneSpodaj1;
	}
	*/
	
	public void sKockamiDolociZacetnegaIgralca() {
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
				System.out.println("Prvi igralec začne!");
				break;
			case -1:
				igra.igralecNaVrsti = igra.pridobiLastnikaKocke(ImeKocke.DRUGA_KOCKA);
				System.out.println("Drugi igralec začne!");
				break;
			case 0:
				System.out.println("Neodločeno! Ponovimo metanje ...");
				continue metanjeKock;
			}
			break metanjeKock;
		}
		igra.trenutnoStanje = StanjeIgre.METANJE_KOCK;  // pričnemo z igro
	}
	
	public void igramo() {
		okno.osveziGUI();  // to je npr. za posodabljanje napisa na oknu oz. napake
		// igra.spremeniStanjeIgre();
		
		switch (igra.trenutnoStanje) {
		case ZMAGA_CRNI: return;
		case ZMAGA_BELI: return;
		case METANJE_KOCK: 
			Igralec igralec = igra.igralecNaVrsti;
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				clovekNaVrsti = false;
				break;
			}
			
			igra.vrziKocki(false);
			igra.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
			
			if (vrstaNaPotezi == VrstaIgralca.R) {
				igramo();
			}
			break;
		case PREMIKANJE_FIGUR:
			Igralec igralec2 = igra.igralecNaVrsti;
			VrstaIgralca vrstaNaPotezi2 = vrstaIgralca.get(igralec2);
			
			premikanje : switch (vrstaNaPotezi2) {
			case C:
				// TODO
				throw new java.lang.RuntimeException("Ni še implementirano.");
			case R:
				igrajRacunalnikovoPotezo();
				break premikanje;
			}
			
			break;
		case IZBIRA_ZACETNEGA_IGRALCA: throw new java.lang.RuntimeException("Na tem mestu bi morali že biti čez to fazo.");
		default: throw new java.lang.RuntimeException("Napaka");
		}
	}
	
	
	public Inteligenca racunalnikovaInteligenca = new Inteligenca(igra);
	
	
	public void igrajRacunalnikovoPotezo() {
		Igra zacetkaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void> () {

			@Override
			protected Poteza doInBackground() {
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo();
				
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
					// zdaj bo na vrsti nasprotnik (recimo človek)
					igramo();
				}
			}
		};
		worker.execute();
	}
	
	
	public void igrajClovekovoPotezo(Poteza poteza) {
		boolean potezaJeBilaUspesnoOdigrana = igra.odigraj(poteza);
		if (potezaJeBilaUspesnoOdigrana) { 
			clovekNaVrsti = false;
		}
		
		igramo();
	}
}