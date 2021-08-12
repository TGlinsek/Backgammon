package grafika;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import logika.Figura;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;
import logika.StanjeIgre;
import logika.Trikotnik;


@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener {
	
	public Igra igra;
	
	public Vodja vodja;
	
	protected Color barvaOzadja;
	protected Color barvaRoba;
	protected Color barvaZetonaBeli;
	protected Color barvaZetonaCrni;
	protected Color barvaParnihTrikotnikov;
	protected Color barvaNeparnihTrikotnokov;
	protected Color barvaKocke;
	protected Color barvaPik;
	protected Color barvaObrobeKocke;
	protected Color barvaObrobeOznacen;
	
	protected double debelinaRobaRelativna; // debelina roba plosce
	protected double debelinaObrobeRelativna; // debelina obrobe za zetone
	protected double debelinaObrobeOznacen; // debelina obrobe za oznacene trikotnike
	protected double odmikRelativen; // odmik med trikotniki v polju
	protected double velikostKockeRelativna;
	protected double velikostPikRelativna;
	protected double razmerjeStranic;
	protected double sirinaTrikotnikaRelativen; // sirinaTrikotnika žetonov
	
	protected int izhodisce;
	protected int cilj;
	protected HashSet<Integer> izbraniTrikotniki;
	
//	teme
	protected boolean Jungle = false;
	protected boolean BubbleGum = false;
	protected boolean Navy = false;
	protected boolean BlackAndWhite = false;

	public Platno(int sirina, int visina) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		
		this.barvaPik = Color.BLACK;
		this.barvaObrobeKocke = Color.BLACK;
		this.barvaObrobeOznacen = Color.YELLOW;
		
		this.debelinaObrobeRelativna = 0.004;
		this.debelinaRobaRelativna = 0.05;
		this.debelinaObrobeOznacen = 0.005;
		this.odmikRelativen = 0.09;
		this.velikostKockeRelativna = 0.081;
		this.velikostPikRelativna = 0.015;
		this.razmerjeStranic = 0.97;
		
		this.izhodisce = 30;
		this.cilj = 30;
		
		addMouseListener(this);
		
//		igra = vodja.igra;
//		samo za preverjanje kode
		igra = new Igra(Igralec.CRNI, true, true);
//		igra.igralnaPlosca.belaBariera.stevilo = 5;
//		igra.igralnaPlosca.crnaBariera.stevilo = 4;
//		igra.trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (Jungle) {
			this.barvaOzadja = new Color(134, 161, 125);
			this.barvaRoba = new Color(102, 58, 0);
			this.barvaParnihTrikotnikov = new Color(255, 233, 204);
			this.barvaNeparnihTrikotnokov = new Color(1, 101, 21);
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = new Color(255, 244, 230);
			this.barvaKocke = new Color(217, 179, 140);
		} else if (BubbleGum) {
			this.barvaOzadja = new Color(225, 184, 184);
			this.barvaRoba = new Color(172, 108, 108);
			this.barvaParnihTrikotnikov = new Color(255, 128, 128);
			this.barvaNeparnihTrikotnokov = new Color(243, 216, 190);
			this.barvaZetonaCrni = new Color(132, 21, 71);
			this.barvaZetonaBeli = new Color(253, 237, 232);
			this.barvaKocke = new Color(244, 139, 139);
		} else if (Navy) {
			this.barvaOzadja = new Color(179, 241, 255);
			this.barvaRoba = new Color (247, 208, 161);
			this.barvaParnihTrikotnikov = Color.WHITE;
			this.barvaNeparnihTrikotnokov = new Color(0, 0, 120);
			this.barvaZetonaCrni = new Color(0, 195, 230);
			this.barvaZetonaBeli = Color.WHITE;
			this.barvaKocke = new Color(179, 204, 255);
		} else if (BlackAndWhite) {
			this.barvaOzadja = Color.GRAY;
			this.barvaRoba = Color.DARK_GRAY;
			this.barvaParnihTrikotnikov = Color.WHITE;
			this.barvaNeparnihTrikotnokov = Color.BLACK;
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = Color.WHITE;
			this.barvaKocke = Color.LIGHT_GRAY;
		} else {
			this.barvaOzadja = new Color(210, 166, 121);
			this.barvaRoba = new Color(77, 42, 0);
			this.barvaParnihTrikotnikov = new Color(102, 51, 0);
			this.barvaNeparnihTrikotnokov = new Color(247, 231, 212);
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = new Color(255, 242, 230);
			this.barvaKocke = new Color(246, 205, 162);
		}

		int vodoravnaStranica = (int) (Math.min(getWidth(), getHeight()));
		int navpicnaStranica = (int) (razmerjeStranic * vodoravnaStranica);
				
		int rob = (int) (debelinaRobaRelativna * vodoravnaStranica);
		int obroba = (int) (debelinaObrobeRelativna * vodoravnaStranica);
		
		int sirinaTrikotnika = (vodoravnaStranica - 2 * rob) / 15;
		int visinaTrikotnika = (int) (2.8 * (navpicnaStranica - 2 * rob) / 7);
				
		int odmikMedTrikotniki = (int) (odmikRelativen * sirinaTrikotnika);
		
//		narise rob
		g2d.setColor(barvaRoba);
		g2d.fillRect(0, 0, vodoravnaStranica, navpicnaStranica);
		
//		narise odzadje
		g2d.setColor(barvaOzadja);
		g2d.fillRect(rob, rob, vodoravnaStranica - 4 * rob, navpicnaStranica - 2 * rob);
		
//		narise bariero
		g2d.setStroke(new BasicStroke((float) sirinaTrikotnika));
		g2d.setColor(barvaRoba);
		g2d.drawLine((int) (rob + 6.5 * sirinaTrikotnika), rob, (int) (rob + 6.5 * sirinaTrikotnika), navpicnaStranica - rob);
		
//		narise cilj
		int[] koordinateZaCiljBelega = new int[] {rob + sirinaTrikotnika * 14, rob, sirinaTrikotnika, visinaTrikotnika, 5, 5};
		int[] koordinateZaCiljCrnega = new int[] {rob + sirinaTrikotnika * 14, navpicnaStranica - rob - visinaTrikotnika, sirinaTrikotnika, visinaTrikotnika, 5, 5};
		g2d.setColor(barvaOzadja);
		g2d.fillRoundRect(rob + sirinaTrikotnika * 14, rob, sirinaTrikotnika, visinaTrikotnika, 5, 5);
		g2d.fillRoundRect(rob + sirinaTrikotnika * 14, navpicnaStranica - rob - visinaTrikotnika, sirinaTrikotnika, visinaTrikotnika, 5, 5);
		
//		narise tirkotnike tako da zacne v levem zgrnjem robu in gre do desnega zgornjega roba, potem pa nadaljuje v levem spodnjem robu in gre do desnega spodnjega roba
		boolean parnost = true;
		boolean zgornjaStran = true;
		int trikotnikNaPlosci = -1;					// to je indeks za dolocen tirkotnik na plosci
		int[] xTrikotnik;
		int[] yTrikotnik;
		int zacetnaTocka;
		izbraniTrikotniki = new HashSet<>();
		for (int i = 0; i < 28; i++) {
			if (i == 13) { 						// narisal je vse trikotnike na zgornji strani in gre na spodnjo stran
				zgornjaStran = false;
			} 
			
			if (parnost) { 						// nastavi pravo barvo trikotnikov
				g2d.setColor(barvaParnihTrikotnikov); parnost = !parnost;
			} else {
				g2d.setColor(barvaNeparnihTrikotnokov); parnost = !parnost;
			} 
			
			if (zgornjaStran) { 				// doloci koordinate za trikotnik
				yTrikotnik = new int[] {rob, rob + visinaTrikotnika, rob};
				zacetnaTocka = rob + odmikMedTrikotniki + i * sirinaTrikotnika;
			} else {
				yTrikotnik = new int[] {navpicnaStranica - rob, navpicnaStranica - rob - visinaTrikotnika, navpicnaStranica - rob};
				zacetnaTocka = rob + odmikMedTrikotniki + (i - 13) * sirinaTrikotnika;
			}
			xTrikotnik = new int[] {zacetnaTocka, (int) (zacetnaTocka + (sirinaTrikotnika / 2 - odmikMedTrikotniki)), zacetnaTocka + sirinaTrikotnika - odmikMedTrikotniki};
			
			// narisemo trikotnik
			Polygon p = new Polygon(xTrikotnik, yTrikotnik, 3);
			if (i != 6 && i != 19 && i < 26) {
				g2d.fillPolygon(p);
			}
			
//			oznacen trikotnik
			double obrobaOznacen = debelinaObrobeOznacen * vodoravnaStranica;

			g2d.setColor(barvaObrobeOznacen);
			g2d.setStroke(new BasicStroke((float) obrobaOznacen));

			int trikotnik = 30;
			if (i >= 0 && i <= 5) trikotnik = 12 - i;
			if (i >= 7 && i <= 12) trikotnik = 13 - i;
			if (i >= 13 && i <= 18) trikotnik = i;
			if (i >= 20 && i <= 25) trikotnik = i - 1;
			
//			izhodiscni trikotnik je bil izbran
			if (izhodisce != 30) {
				List<Poteza> veljavnePoteze = igra.vrniVeljavnePotezeTePlosce();
				System.out.println(veljavnePoteze);
				for (Poteza poteza : veljavnePoteze){
					if (poteza.vrniIzhodisce() == izhodisce && poteza.vrniCilj() <= 0) {
						izbraniTrikotniki.add(0);
						g2d.drawRoundRect(koordinateZaCiljBelega[0], koordinateZaCiljBelega[1], koordinateZaCiljBelega[2], koordinateZaCiljBelega[3], koordinateZaCiljBelega[4], koordinateZaCiljBelega[5]);
					} else if (poteza.vrniIzhodisce() == izhodisce && poteza.vrniCilj() >= 25) {
						izbraniTrikotniki.add(25);
						g2d.drawRoundRect(koordinateZaCiljCrnega[0], koordinateZaCiljCrnega[1], koordinateZaCiljCrnega[2], koordinateZaCiljCrnega[3], koordinateZaCiljCrnega[4], koordinateZaCiljCrnega[5]);
					} else if (poteza.vrniIzhodisce() == izhodisce && trikotnik == poteza.vrniCilj()){
						izbraniTrikotniki.add(trikotnik);
						g2d.drawPolygon(p);
						System.out.println("Izbranitrikotniki:  " + izbraniTrikotniki);
					}
				}
			}
			
//			narisemo zetone na triktoniku, ce imamo igro
		
			if (igra != null) {
				// dolocimo kateri trikotnik iz plosce risemo
				if (0 <= i && i <= 5) trikotnikNaPlosci = -i + 11; 
				if (7 <= i && i <= 12) trikotnikNaPlosci = -i + 12;
				if (13 <= i && i <= 18) trikotnikNaPlosci = i - 1;
				if (20 <= i && i <= 25) trikotnikNaPlosci = i - 2;
				
				Trikotnik trenutniTrikotnik;
				Color barvaFigure;
				Color barvaObrobe;
				int razdaljaMedSredisciZetonov;
				boolean zetonNaBarieriBeli = false;
				boolean zetonNaBarieriCrni = false;
				
				if (i == 6) {
					trenutniTrikotnik = igra.igralnaPlosca.crnaBariera;
					trenutniTrikotnik.barvaFigur = Figura.CRNA;
					if (igra.igralecNaVrsti == Igralec.CRNI && trenutniTrikotnik.stevilo > 0) zetonNaBarieriCrni = true;
				}
				else if (i == 19) {
					trenutniTrikotnik = igra.igralnaPlosca.belaBariera;
					trenutniTrikotnik.barvaFigur = Figura.BELA;
					if (igra.igralecNaVrsti == Igralec.BELI && trenutniTrikotnik.stevilo > 0) zetonNaBarieriBeli = true;
				}
				else if (i == 26) trenutniTrikotnik = igra.igralnaPlosca.beliCilj;
				else if (i == 27) trenutniTrikotnik = igra.igralnaPlosca.crniCilj;
				else trenutniTrikotnik = igra.igralnaPlosca.plosca[trikotnikNaPlosci];
				
				// pogledamo kaksno barvo imamo na tem trikotniku
				if (trenutniTrikotnik.barvaFigur == Figura.BELA) {
					barvaFigure = barvaZetonaBeli;
					barvaObrobe = barvaZetonaCrni;
				} else {
					barvaFigure = barvaZetonaCrni;
					barvaObrobe = barvaZetonaBeli;
				}
				
				// kako skupaj moramo narisati zetone, da ne pogledajo cez trikotnik
				if (trenutniTrikotnik.stevilo * sirinaTrikotnika <= visinaTrikotnika) razdaljaMedSredisciZetonov = sirinaTrikotnika;
				else razdaljaMedSredisciZetonov = (int) ((visinaTrikotnika - sirinaTrikotnika) / (trenutniTrikotnik.stevilo - 1));
				for (int j = 0; j < trenutniTrikotnik.stevilo; j++) {
					
					// narisemo zetone
					int yKoordinata;
					if (zgornjaStran) {
//						crna bariera
						if (i == 6) {			
							yKoordinata = yTrikotnik[1] - sirinaTrikotnika - razdaljaMedSredisciZetonov * (trenutniTrikotnik.stevilo - 1 - j);
						} else {
							yKoordinata = yTrikotnik[0] + j * razdaljaMedSredisciZetonov;
						}
					} else {
//						bela bariera
						if (i == 19) {
							yKoordinata = yTrikotnik[1] + razdaljaMedSredisciZetonov * (trenutniTrikotnik.stevilo - 1 - j);
						} else {
							yKoordinata = yTrikotnik[0] - sirinaTrikotnika + (-1) * j * razdaljaMedSredisciZetonov;
						}
					}
	//				beli cilj
					if (i == 26) {
						g2d.setColor(barvaFigure);
						g2d.fillOval(rob + sirinaTrikotnika * 14, rob + j * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
						g2d.setColor(barvaObrobe);
						g2d.setStroke(new BasicStroke((float) obroba));
						g2d.drawOval(rob + sirinaTrikotnika * 14, rob + j * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
	//				crni cilj
					} else if (i == 27) {
						g2d.setColor(barvaFigure);
						g2d.fillOval(rob + sirinaTrikotnika * 14, navpicnaStranica - rob - sirinaTrikotnika + (-1) * j * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
						g2d.setColor(barvaObrobe);
						g2d.setStroke(new BasicStroke((float) obroba));
						g2d.drawOval(rob + sirinaTrikotnika * 14, navpicnaStranica - rob - sirinaTrikotnika + (-1) * j * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
					} else {
						g2d.setColor(barvaFigure);
						g2d.fillOval(xTrikotnik[0] - odmikMedTrikotniki, yKoordinata, sirinaTrikotnika, sirinaTrikotnika);
						if (j == trenutniTrikotnik.stevilo - 1 && i == 6 && zetonNaBarieriCrni) g2d.setColor(barvaObrobeOznacen);
						else if (j == trenutniTrikotnik.stevilo - 1 && i == 19 && zetonNaBarieriBeli) g2d.setColor(barvaObrobeOznacen);
						else if (j == trenutniTrikotnik.stevilo - 1 && trenutniTrikotnik.barvaFigur == Figura.CRNA && izhodisce == 30 && igra.igralecNaVrsti.pridobiFiguro() == Figura.CRNA && !zetonNaBarieriCrni) g2d.setColor(barvaObrobeOznacen);
						else if (j == trenutniTrikotnik.stevilo - 1 && trenutniTrikotnik.barvaFigur == Figura.BELA && izhodisce == 30 && igra.igralecNaVrsti.pridobiFiguro() == Figura.BELA && !zetonNaBarieriBeli) g2d.setColor(barvaObrobeOznacen);
						else g2d.setColor(barvaObrobe);
						g2d.setStroke(new BasicStroke((float) obroba));
						g2d.drawOval(xTrikotnik[0] - odmikMedTrikotniki, yKoordinata, sirinaTrikotnika, sirinaTrikotnika);
					}
				}
			}
		}
		// narisemo kocke
		if (igra != null) {
//			int[] vrednostKock = new int[] {3, 3};
			int[] vrednostKock = new int[] {igra.kocka1.vrniVrednost(), igra.kocka2.vrniVrednost()};
			int velikostKocke = (int) (vodoravnaStranica * velikostKockeRelativna);
			int velikostPik = (int) (vodoravnaStranica * velikostPikRelativna);
			double premikKock = 0;
			if (igra.igralecNaVrsti == Igralec.CRNI) {
				premikKock = 7;
			}
			
			int xEnaKockaBeli = (int) (rob + 3 * sirinaTrikotnika - 0.5 * velikostKocke);
			int xEnaKockaCrni = (int) (rob + 10 * sirinaTrikotnika - 0.5 * velikostKocke);
			
			int xDveKockiPrvi = (int) (rob + (3 + premikKock) * sirinaTrikotnika - 1.25 * velikostKocke);
			int xDveKockiDrugi = (int) (rob + (3 + premikKock) * sirinaTrikotnika + 0.25 * velikostKocke);
			
			int xStiriKockePrvi = (int) (rob + (3 + premikKock) * sirinaTrikotnika - 2.1 * velikostKocke + 0.5 * odmikMedTrikotniki);
			int xStiriKockeDrugi = (int) (rob + (3 + premikKock) * sirinaTrikotnika - 1 * velikostKocke + 0.5 * odmikMedTrikotniki);
			int xStiriKockeTretji = (int) (rob + (3 + premikKock) * sirinaTrikotnika + 0.1 * velikostKocke + 0.5 * odmikMedTrikotniki);
			int xStiriKockeCetrti = (int) (rob + (3 + premikKock) * sirinaTrikotnika + 1.2 * velikostKocke + 0.5 * odmikMedTrikotniki);
			
			int yVseKocke = (int) (navpicnaStranica / 2 - velikostKocke / 2); 
			
			if (igra.trenutnoStanje == StanjeIgre.METANJE_KOCK) {
				g2d.setColor(barvaKocke);
				g2d.fillRoundRect(xEnaKockaBeli, yVseKocke, velikostKocke, velikostKocke, 20, 20);
				g2d.fillRoundRect(xEnaKockaCrni, yVseKocke, velikostKocke, velikostKocke, 20, 20);
				g2d.setStroke(new BasicStroke(obroba));
				g2d.setColor(barvaObrobeKocke);
				g2d.drawRoundRect(xEnaKockaBeli, yVseKocke, velikostKocke, velikostKocke, 20, 20);
				g2d.drawRoundRect(xEnaKockaCrni, yVseKocke, velikostKocke, velikostKocke, 20, 20);
			}
			else {
//				ali potrebujemo stiri kocke ali le dve
				if (vrednostKock[0] == vrednostKock[1]) {
					velikostKocke = (int) (velikostKocke * 0.9);
					g2d.setColor(barvaKocke);
					g2d.fillRoundRect(xStiriKockePrvi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.fillRoundRect(xStiriKockeDrugi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.fillRoundRect(xStiriKockeTretji, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.fillRoundRect(xStiriKockeCetrti, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.setStroke(new BasicStroke(obroba));
					g2d.setColor(barvaObrobeKocke);
					g2d.drawRoundRect(xStiriKockePrvi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.drawRoundRect(xStiriKockeDrugi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.drawRoundRect(xStiriKockeTretji, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.drawRoundRect(xStiriKockeCetrti, yVseKocke, velikostKocke, velikostKocke, 20, 20);
				} else {
					g2d.setColor(barvaKocke);
					g2d.fillRoundRect(xDveKockiPrvi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.fillRoundRect(xDveKockiDrugi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.setStroke(new BasicStroke(obroba));
					g2d.setColor(barvaObrobeKocke);
					g2d.drawRoundRect(xDveKockiPrvi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
					g2d.drawRoundRect(xDveKockiDrugi, yVseKocke, velikostKocke, velikostKocke, 20, 20);	
				}				
			}
			
//			pike na kockah
			
//			pove katere faktoje moramo vnesti v PikaNaKocki, za toliko stevilo pik kot je kljuc. Faktorje gledamo po dva skupaj.
			Map<Integer, int[]> pikeNaKocki = Map.of(1, new int[] {3, 3}, 2, new int[] {1, 1, 5, 5}, 3, new int[] {1, 1, 3, 3, 5, 5}, 
					4, new int[] {1, 1, 1, 5, 5, 1, 5, 5}, 5, new int[] {1, 1, 1, 5, 5, 1, 5, 5, 3, 3}, 6, new int[] {1, 3, 5, 3, 1, 1, 1, 5, 5, 1, 5, 5});
			int StiriKockePrvic;
			int StrirKockeDrugic;
			int DveKocki;
			int EnaKocka;
			g2d.setColor(barvaPik);
			
			for (int j = 0; j < vrednostKock.length; j++) {
				int [] zaporedje = pikeNaKocki.get(vrednostKock[j]);
				if (igra.trenutnoStanje == StanjeIgre.METANJE_KOCK) {
					if (j == 1) EnaKocka = xEnaKockaBeli;
					else EnaKocka = xEnaKockaCrni;
					for (int k = 0; k < zaporedje.length; k = k + 2) {
						PikaNaKocki(g2d, EnaKocka, velikostKocke, velikostPik, yVseKocke, zaporedje[k], zaporedje[k + 1]);
					}
				} else if (vrednostKock[0] == vrednostKock[1]) {
					if (j == 1) {
						StiriKockePrvic = xStiriKockePrvi;
						StrirKockeDrugic = xStiriKockeDrugi;
					}
					else {
						StiriKockePrvic = xStiriKockeTretji;
						StrirKockeDrugic = xStiriKockeCetrti;
					}
					for (int k = 0; k < zaporedje.length; k = k + 2) {
						PikaNaKocki(g2d, StiriKockePrvic, velikostKocke, velikostPik, yVseKocke, zaporedje[k], zaporedje[k + 1]);
						PikaNaKocki(g2d, StrirKockeDrugic, velikostKocke, velikostPik, yVseKocke, zaporedje[k], zaporedje[k + 1]);
					}
				} else {
					if (j == 1) DveKocki = xDveKockiPrvi;
					else DveKocki = xDveKockiDrugi;
					for (int k = 0; k < zaporedje.length; k = k + 2) {
						PikaNaKocki(g2d, DveKocki, velikostKocke, velikostPik, yVseKocke, zaporedje[k], zaporedje[k + 1]);
					}
				}
			}
		}
	}
	
	private void PikaNaKocki(Graphics2D g2d, int xKocki, int velikostKocke, int velikostPik, int yVseKocke, int faktorX, int faktorY) {
		g2d.fillOval(xKocki + faktorX * velikostKocke / 7,  yVseKocke + faktorY * velikostKocke / 7, velikostPik, velikostPik);
	}
	
	
	private Poteza izberiPotezoIzmedMoznihPotez(int izhodisce, int premik, Figura igralec) {  // vrne potezo s temi parametri, razen premik je lahka tut večji
		// taka poteza bo vedno obstajala
		List<Poteza> veljavnePoteze = igra.vrniVeljavnePotezeTePlosce();
		for (Poteza poteza : veljavnePoteze) {
			if (poteza.vrniIzhodisce() == izhodisce && poteza.premik >= premik && poteza.igralec == igralec) {
				return poteza;
			}
		}
		throw new java.lang.RuntimeException("Ni bilo najdene ustrezne poteze!");
	}
	
	
	boolean izbiramoCilj = false;
	int prejsnjiTrikotnik;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		int vodoravnaStranica = (int) (Math.min(getWidth(), getHeight()));
		int navpicnaStranica = (int) (razmerjeStranic * vodoravnaStranica);
				
		int rob = (int) (debelinaRobaRelativna * vodoravnaStranica);
		
		int sirinaTrikotnika = (vodoravnaStranica - 2 * rob) / 15;
		int visinaTrikotnika = (int) (2.8 * (navpicnaStranica - 2 * rob) / 7);

		if (x < rob || y < rob || x > vodoravnaStranica - rob || y > navpicnaStranica - rob) return;
		
		int stolpec = Math.round((x - rob) / sirinaTrikotnika);
		int vrstica = 2;
		if (y - rob < visinaTrikotnika) vrstica = 0;
		else if (y + rob > navpicnaStranica - visinaTrikotnika) vrstica = 1;
		
		if (vrstica == 2 || stolpec == 13) return;
		
		int trikotnik = 30; // 0 je bariera crnega, 25 je cilj crnega

		if (0 <= stolpec && stolpec <= 5 && vrstica == 0) trikotnik = 12 - stolpec;
		if (7 <= stolpec && stolpec <= 12 && vrstica == 0) trikotnik = 13 - stolpec;
		if (0 <= stolpec && stolpec <= 5 && vrstica == 1) trikotnik = 13 + stolpec;
		if (7 <= stolpec && stolpec <= 12 && vrstica == 1) trikotnik = 12 + stolpec;
		if (vrstica == 0 && (stolpec == 6 || stolpec == 14)) trikotnik = 0;
		if (vrstica == 1 && (stolpec == 6 || stolpec == 14)) trikotnik = 25;
		
		prejsnjiTrikotnik = izhodisce;
		if (1 <= trikotnik && trikotnik <= 24) {
			if (igra.igralnaPlosca.plosca[trikotnik - 1].barvaFigur == igra.igralecNaVrsti.pridobiFiguro()) izhodisce = trikotnik;		
		}
		else if (trikotnik == 0 && igra.igralnaPlosca.crnaBariera.stevilo > 0 && igra.igralecNaVrsti.pridobiFiguro() == Figura.CRNA) izhodisce = trikotnik;
		else if (trikotnik == 25 && igra.igralnaPlosca.belaBariera.stevilo > 0 && igra.igralecNaVrsti.pridobiFiguro() == Figura.BELA) izhodisce = trikotnik;
		else izhodisce = 30;
		System.out.println("Trikotnik:    " + trikotnik);
		System.out.println("Izbrani trikotniki klikanje:   " + izbraniTrikotniki);
		if (izbraniTrikotniki != null) {		
			if (izbraniTrikotniki.size() > 0 && izbraniTrikotniki.contains(trikotnik)) {
				cilj = trikotnik;
			}
			else cilj = 30;
		} 
		System.out.println("Prejsnji trikotnik:    " + prejsnjiTrikotnik);
		System.out.println("Izhodisce:   " + izhodisce);
		System.out.println("Cilj:  " + cilj);
		
		if (cilj != 30) {
			Poteza poteza = izberiPotezoIzmedMoznihPotez(prejsnjiTrikotnik, cilj - prejsnjiTrikotnik, igra.igralecNaVrsti.pridobiFiguro());
			
			// System.out.println("Poteza:   " + (new Poteza(prejsnjiTrikotnik, cilj - prejsnjiTrikotnik, igra.igralecNaVrsti.pridobiFiguro())));
			System.out.println("Poteza: " + poteza);
			igra.odigraj(poteza);
			izhodisce = 30;
			cilj = 30;
		}
		
		
//		System.out.println("X: " + x + "; Y: " + y + "; izhodisce: " + izhodisce);
		repaint();
	}
	
	public void nastaviTemo(String tema) {
		//uporabljeno v oknu za nastavljanje tem
		
		Jungle = false;
		BubbleGum = false;
		Navy = false;
		BlackAndWhite = false;
		
		if(tema == "Jungle") {
			Jungle = true;
		}else if(tema == "BubbleGum") {
			BubbleGum = true;
		}else if(tema == "Navy"){
			Navy = true;
		}else if(tema == "BlackAndWhite") {
			BlackAndWhite = true;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}