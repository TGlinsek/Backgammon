package grafika;

import java.util.List;
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
	
	public Map<Igralec, VrstaIgralca> vrstaIgralca;
	// public static Map<Igralec, KdoIgra> kdoIgra;  // tega verjetno ne bomo rabili
	
	public Okno okno;
	
	public Igra igra;
	
	// public boolean clovekNaVrsti = false;  // ni važno, kaj je na začetku
	public boolean clovekNaVrsti;  // ne rabimo v konstruktorju definirat, se bo sproti definiralo
	
	public Inteligenca racunalnikovaInteligenca;
	/*
	private static Igralec igralecKiZacne = Igralec.CRNI;  // default nastavitve
	private static boolean crniGreVSmeriUrinegaKazalca = true;  // default nastavitve
	private static boolean crniZacneSpodaj = true;  // default nastavitve
	*/
	
	public Vodja(Okno okno) {
		// igra = new Igra(igralecKiZacne, crniGreVSmeriUrinegaKazalca, crniZacneSpodaj);  // nastavi velikost (brez parametra za default)
		igra = new Igra();  // verjetno na koncu ne bo nobenih parametrov tule
		this.okno = okno;
		racunalnikovaInteligenca = new Inteligenca(igra);
	}
	
	
	public void igramoNovoIgro() {
		
		// sKockamiDolociZacetnegaIgralca();
		
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
	
	/*
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
	*/
	
	public void igramo() {
		System.out.println(clovekNaVrsti);
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
			
			if (!clovekNaVrsti) {
				igra.vrziKocki();
				igra.trenutnoStanje = StanjeIgre.PREMIKANJE_FIGUR;
				igramo();
			}
			break;
		case PREMIKANJE_FIGUR:
			Igralec igralec2 = igra.igralecNaVrsti;
			VrstaIgralca vrstaNaPotezi2 = vrstaIgralca.get(igralec2);
			
			premikanje : switch (vrstaNaPotezi2) {
			case C:
				clovekNaVrsti = true;
				break premikanje;
			case R:
				clovekNaVrsti = false;
				igrajRacunalnikovoPotezo();
				break premikanje;
			}
			
			
			// if (vrstaNaPotezi2 == VrstaIgralca.R) igrajRacunalnikovoPotezo();
			
			break;
		default: throw new java.lang.RuntimeException("Napaka");
		}
	}
	
	
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
					List<Integer> seznamKock = igra.vrniSeznamKock();
					if (seznamKock.size() == 0) {
						// zdaj bo na vrsti nasprotnik (recimo človek)
						igra.zamenjajIgralca();
						igra.vrziKocki();
						clovekNaVrsti = true;
					}
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