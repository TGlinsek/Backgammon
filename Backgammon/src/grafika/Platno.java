package grafika;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import logika.Figura;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;
import logika.StanjeIgre;
import logika.Trikotnik;
import tekstovni_vmesnik.Vodja;


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
	
	protected double sirinaTrikotnikaRelativen; // sirinaTrikotnika žetonov
	
	protected int sirina; //sirina originalnega polja
	protected int visina; //visina originalnega polja
	
//	teme
	protected boolean Jungle = false;
	protected boolean BubbleGum = false;
	protected boolean Navy = false;
	protected boolean BlackAndWhite = false;

	public Platno(int sirina, int visina) {
		super();
		setPreferredSize(new Dimension(sirina, visina));
		
		if (Jungle) {
			this.barvaOzadja = new Color(134, 161, 125);
			this.barvaRoba = new Color(102, 58, 0);
			this.barvaParnihTrikotnikov = new Color(255, 233, 204);
			this.barvaNeparnihTrikotnokov = new Color(1, 101, 21);
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = new Color(255, 244, 230);
		} else if (BubbleGum) {
			this.barvaOzadja = new Color(225, 184, 184);
			this.barvaRoba = new Color(172, 108, 108);
			this.barvaParnihTrikotnikov = new Color(244, 139, 139);
			this.barvaNeparnihTrikotnokov = new Color(243, 216, 190);
			this.barvaZetonaCrni = new Color(132, 21, 71);
			this.barvaZetonaBeli = new Color(253, 237, 232);
		} else if (Navy) {
			this.barvaOzadja = new Color(179, 241, 255);
			this.barvaRoba = new Color (253, 217, 180);
			this.barvaParnihTrikotnikov = Color.WHITE;
			this.barvaNeparnihTrikotnokov = new Color(0, 0, 120);
			this.barvaZetonaCrni = new Color(0, 195, 230);
			this.barvaZetonaBeli = Color.WHITE;
		} else if (BlackAndWhite) {
			this.barvaOzadja = Color.GRAY;
			this.barvaRoba = Color.DARK_GRAY;
			this.barvaParnihTrikotnikov = Color.WHITE;
			this.barvaNeparnihTrikotnokov = Color.BLACK;
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = Color.WHITE;
		} else {
			this.barvaOzadja = new Color(210, 166, 121);
			this.barvaRoba = new Color(77, 42, 0);
			this.barvaParnihTrikotnikov = new Color(102, 51, 0);
			this.barvaNeparnihTrikotnokov = new Color(247, 231, 212);
			this.barvaZetonaCrni = Color.BLACK;
			this.barvaZetonaBeli = new Color(255, 242, 230);
		}
		
		this.barvaKocke = Color.RED;
		this.barvaPik = Color.BLACK;
		this.barvaObrobeKocke = Color.BLACK;
		this.barvaObrobeOznacen = Color.YELLOW;
		
		this.debelinaObrobeRelativna = 0.003;
		this.debelinaRobaRelativna = 0.05;
		this.debelinaObrobeOznacen = 0.005;
		this.odmikRelativen = 0.09;
		this.velikostKockeRelativna = 0.1;
		this.velikostPikRelativna = 0.02;
		
		this.sirina = sirina;
		this.visina = visina;
		
		addMouseListener(this);
		
//		samo za preverjanje kode
		igra = new Igra(Igralec.BELI, true, true);
		igra.trenutnoStanje = StanjeIgre.METANJE_KOCK;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int velikostPolja = (int) (Math.min(getWidth(), getHeight()));
				
		int rob = (int) (debelinaRobaRelativna * velikostPolja);
		int obroba = (int) (debelinaObrobeRelativna * velikostPolja);
		
		int sirinaTrikotnika = (Math.min(getWidth(), getHeight()) - 2 * rob) / 13;
		int visinaTrikotnika = (int) (2.8 * (Math.min(getWidth(), getHeight()) - 2 * rob) / 7);
				
		int odmikMedTrikotniki = (int) (odmikRelativen * sirinaTrikotnika);
		
//		narise rob
		g2d.setColor(barvaRoba);
		g2d.fillRect(0, 0, velikostPolja, velikostPolja);
		
//		narise odzadje
		g2d.setColor(barvaOzadja);
		g2d.fillRect(rob, rob, velikostPolja - 2 * rob, velikostPolja - 2 * rob);
		
//		narise bariero
		g2d.setStroke(new BasicStroke((float) sirinaTrikotnika));
		g2d.setColor(barvaRoba);
		g2d.drawLine((int) (rob + 6.5 * sirinaTrikotnika), rob, (int) (rob + 6.5 * sirinaTrikotnika), velikostPolja - rob);
		
//		narise tirkotnike tako da zacne v levem zgrnjem robu in gre do desnega zgornjega roba, potem pa nadaljuje v levem spodnjem robu in gre do desnega spodnjega roba
		boolean parnost = true;
		boolean zgornjaStran = true;
		int trikotnikNaPlosci = -1;					// to je indeks za dolocen tirkotnik na plosci
		int[] xTrikotnik;
		int[] yTrikotnik;
		int zacetnaTocka;
		for (int i = 0; i < 26; i++) {
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
				yTrikotnik = new int[] {velikostPolja - rob, velikostPolja - rob - visinaTrikotnika, velikostPolja - rob};
				zacetnaTocka = rob + odmikMedTrikotniki + (i - 13) * sirinaTrikotnika;
			}
			xTrikotnik = new int[] {zacetnaTocka, (int) (zacetnaTocka + (sirinaTrikotnika / 2 - odmikMedTrikotniki)), zacetnaTocka + sirinaTrikotnika - odmikMedTrikotniki};
			
			// narisemo trikotnik
			Polygon p = new Polygon(xTrikotnik, yTrikotnik, 3);
			if (i != 6 && i != 19) {
				g2d.fillPolygon(p);
			}
			
			double obrobaOznacen = debelinaObrobeOznacen * velikostPolja;
//			List<Poteza> veljavnePoteze = igra.vrniVeljavnePotezeTePlosce();
			List<Poteza> veljavnePoteze = Arrays.asList(new Poteza(0, 10, Figura.BELA));

//			relativno polje
			for (Poteza poteza : veljavnePoteze){
				if (poteza.vrniCilj() == i) {
					g2d.setColor(barvaObrobeOznacen);
					g2d.setStroke(new BasicStroke((float) obrobaOznacen));
					g2d.drawPolygon(p);
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
				
				if (i == 6) trenutniTrikotnik = igra.igralnaPlosca.crnaBariera;
				else if (i == 19) trenutniTrikotnik = igra.igralnaPlosca.belaBariera;
				else trenutniTrikotnik = igra.igralnaPlosca.plosca[trikotnikNaPlosci];
				
				// pogledamo kaksno barvo imamo na tem trikotniku
				if (trenutniTrikotnik.barvaFigur == Figura.BELA) {
					barvaFigure = barvaZetonaBeli;
					barvaObrobe = barvaZetonaCrni;
				}
				else {
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
						
						if (i == 6) {			
							yKoordinata = yTrikotnik[1] - sirinaTrikotnika - razdaljaMedSredisciZetonov * (trenutniTrikotnik.stevilo - 1 + j);
						} else {
							yKoordinata = yTrikotnik[0] + j * razdaljaMedSredisciZetonov;
						}
					}
					else {
						if (i == 19) {
							yKoordinata = yTrikotnik[1] + razdaljaMedSredisciZetonov * (trenutniTrikotnik.stevilo - 1 - j);
						} else {
							yKoordinata = yTrikotnik[0] - sirinaTrikotnika + (-1) * j * razdaljaMedSredisciZetonov;
						}
					}
					g2d.setColor(barvaFigure);
					g2d.fillOval(xTrikotnik[0] - odmikMedTrikotniki, yKoordinata, sirinaTrikotnika, sirinaTrikotnika);
					g2d.setColor(barvaObrobe);
					g2d.setStroke(new BasicStroke((float) obroba));
					g2d.drawOval(xTrikotnik[0] - odmikMedTrikotniki, yKoordinata, sirinaTrikotnika, sirinaTrikotnika);		
				}
			}
		}
		// narisemo kocke
		if (igra != null) {
			int velikostKocke = (int) (velikostPolja * velikostKockeRelativna);
			int velikostPik = (int) (velikostPolja * velikostPikRelativna);
			int xKoordinata;
//			!!popravi, da bo vodja dal vrednost kock
			int[] vrednostKock = igra.vrziKocki(false);
//			int[] vrednostKock = new int[] {6, 2}; // za preverjanje
			double premikKockDve = 0;
			double premikKockStiri = 0;
			if (igra.igralecNaVrsti == Igralec.CRNI) {
				premikKockDve = 5;
				premikKockStiri= 13.25;
			}
			
			int xEnaKockaBeli = (int) ((velikostPolja - rob) / 4);
			int xEnaKockaCrni = (int) (3 * (velikostPolja - rob) / 4);
			
			int xDveKockiPrvi = (int) ((1 + premikKockDve) * (velikostPolja - rob) / 10);
			int xDveKockiDrugi = (int) ((3 + premikKockDve) * (velikostPolja - rob) / 10);
			
			int xStiriKockePrvi = (int) ((1.5 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeDrugi = (int) ((4.3 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeTretji = (int) ((7.1 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeCetrti = (int) ((9.9 + premikKockStiri) * (velikostPolja - rob) / 26);
			
			int yVseKocke = (int) (velikostPolja / 2 - velikostKocke / 2); 
			
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
			
			// pove katere faktoje moramo vnesti v PikaNaKocki, za toliko stevilo pik kot je kljuc. Faktorje gledamo po dva skupaj.
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
						PikaNaKocki(g2d, EnaKocka, velikostKocke, velikostPik, velikostPolja, yVseKocke, zaporedje[k], zaporedje[k + 1]);
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
						PikaNaKocki(g2d, StiriKockePrvic, velikostKocke, velikostPik, velikostPolja, yVseKocke, zaporedje[k], zaporedje[k + 1]);
						PikaNaKocki(g2d, StrirKockeDrugic, velikostKocke, velikostPik, velikostPolja, yVseKocke, zaporedje[k], zaporedje[k + 1]);
					}
				} else {
					if (j == 1) DveKocki = xDveKockiPrvi;
					else DveKocki = xDveKockiDrugi;
					for (int k = 0; k < zaporedje.length; k = k + 2) {
						PikaNaKocki(g2d, DveKocki, velikostKocke, velikostPik, velikostPolja, yVseKocke, zaporedje[k], zaporedje[k + 1]);
					}
				}
			}
		}
	}
	
	private void PikaNaKocki(Graphics2D g2d, int xKocki, int velikostKocke, int velikostPik, int velikostPolja, int yVseKocke, int faktorX, int faktorY) {
		g2d.fillOval(xKocki + faktorX * velikostKocke / 7,  yVseKocke + faktorY * velikostKocke / 7, velikostPik, velikostPik);
	}
	
	private	List<Integer> PotezeVInt(List<Poteza> poteze) {
		List<Integer> stevila = Arrays.asList(27);
		for (int i = 0; i < poteze.size(); i++) {
			stevila.add(poteze.get(i).vrniCilj());
		}
		return stevila;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		int velikostPolja = (int) (Math.min(getWidth(), getHeight()));
		int rob = (int) (debelinaRobaRelativna * velikostPolja);

		if (x < rob || y < rob || x > velikostPolja - rob || y > velikostPolja - rob) return;
		
		int sirinaTrikotnika = (Math.min(getWidth(), getHeight()) - 2 * rob) / 13;
		int visinaTrikotnika = 3 * (Math.min(getWidth(), getHeight()) - 2 * rob) / 7;
		
		int stolpec = Math.round((x - rob) / sirinaTrikotnika);
		int vrstica = 2;
		if (y - rob < visinaTrikotnika) vrstica = 0;
		else if (y + rob > velikostPolja - visinaTrikotnika) vrstica = 1;
		
		if (stolpec == 6 || vrstica == 2) return;
		
		int TrikotnikNaPlosci = -1;
		if (0 <= stolpec && stolpec <= 5 && vrstica == 0) TrikotnikNaPlosci = 11 - stolpec;
		if (7 <= stolpec && stolpec <= 12 && vrstica == 0) TrikotnikNaPlosci = 12 - stolpec;
		if (0 <= stolpec && stolpec <= 5 && vrstica == 1) TrikotnikNaPlosci = 12 + stolpec;
		if (7 <= stolpec && stolpec <= 12 && vrstica == 1) TrikotnikNaPlosci = 11 + stolpec;
		
		System.out.println("X: " + x + "; Y: " + y + "; TrikotnikNaPlosci: " + TrikotnikNaPlosci);
		
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
