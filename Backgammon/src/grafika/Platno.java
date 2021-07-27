package grafika;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import logika.Figura;
import logika.Igra;
import logika.Igralec;
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
	
	protected double debelinaRobaRelativna; // debelina roba plosce
	protected double debelinaObrobeRelativna; // debelina obrobe za zetone
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
		
		this.debelinaObrobeRelativna = 0.003;
		this.debelinaRobaRelativna = 0.05;
		this.odmikRelativen = 0.09;
		this.velikostKockeRelativna = 0.1;
		this.velikostPikRelativna = 0.02;
		
		this.sirina = sirina;
		this.visina = visina;
		
		addMouseListener(this);
		
//		samo za preverjanje kode
		igra = new Igra(Igralec.BELI, true, true);
		
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
			if (i != 6 && i != 19) {
				Polygon p = new Polygon(xTrikotnik, yTrikotnik, 3);
				g2d.fillPolygon(p);
			}
			
//			lepse izgleda brez obrobe na trikotnikih
//			g2d.setColor(barvaObrobe);
//			g2d.setStroke(new BasicStroke((float) obroba));
//			g2d.drawPolygon(p);
			
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
				
				int faktor;
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
		if (igra != null && igra.trenutnoStanje == StanjeIgre.PREMIKANJE_FIGUR) {
			int velikostKocke = (int) (velikostPolja * velikostKockeRelativna);
			int velikostPik = (int) (velikostPolja * velikostPikRelativna);
			int xKoordinata;
			g2d.setColor(barvaKocke);
//			!!popravi, da bo vodja dal vrednost kock
			int[] vrednostKock = igra.vrziKocki(false);
//			int[] vrednostKock = new int[] {5, 1}; // za preverjanje
			int premikKockDve;
			double premikKockStiri;
			if (igra.igralecNaVrsti == Igralec.BELI) {
				premikKockDve = 0;
				premikKockStiri = 0;
			} else {
				premikKockDve = 5;
				premikKockStiri= 13.25;
			}
			
			int xStiriKockePrvi = (int) ((1.5 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeDrugi = (int) ((4.3 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeTretji = (int) ((7.1 + premikKockStiri) * (velikostPolja - rob) / 26);
			int xStiriKockeCetrti = (int) ((9.9 + premikKockStiri) * (velikostPolja - rob) / 26);
			
			int xDveKockiPrvi = (int) ((1 + premikKockDve) * (velikostPolja - rob) / 10);
			int xDveKockiDrugi = (int) ((3 + premikKockDve) * (velikostPolja - rob) / 10);
			
			int yVseKocke = (int) (velikostPolja / 2 - velikostKocke / 2);
			
//			ali potrebujemo stiri kocke ali le dve
			if (vrednostKock[0] == vrednostKock[1]) {
				velikostKocke = (int) (velikostKocke * 0.9);
				g2d.fillRoundRect(xStiriKockePrvi, (int) (rob + velikostPolja / 2 - velikostKocke), velikostKocke, velikostKocke, 20, 20);
				g2d.fillRoundRect((int) ((4.3 + premikKockStiri) * (velikostPolja - rob) / 26), (int) (velikostPolja / 2 - velikostKocke / 2), velikostKocke, velikostKocke, 20, 20);
				g2d.fillRoundRect((int) ((7.1 + premikKockStiri) * (velikostPolja - rob) / 26), (int) (velikostPolja / 2 - velikostKocke / 2), velikostKocke, velikostKocke, 20, 20);
				g2d.fillRoundRect((int) ((9.9 + premikKockStiri) * (velikostPolja - rob) / 26), (int) (velikostPolja / 2 - velikostKocke / 2), velikostKocke, velikostKocke, 20, 20);
			} else {
				g2d.fillRoundRect( xDveKockiPrvi, yVseKocke, velikostKocke, velikostKocke, 20, 20);
				g2d.fillRoundRect( xDveKockiDrugi, yVseKocke, velikostKocke, velikostKocke, 20, 20);					
			}
			
//			pike na kockah
			g2d.setColor(barvaPik);
			if (vrednostKock[0] == vrednostKock[1]) {
				
			} else {
				if (vrednostKock[0] == 1) PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 3, 3);
				if (vrednostKock[1] == 1) PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPik, yVseKocke, 3, 3);
				if (vrednostKock[0] == 2) {
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[1] == 2) {
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[0] == 3) {
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 3, 3);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[1] == 3) {
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPik, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 3, 3);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[0] == 4) {
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[1] == 4) {
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[0] == 5) {
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 3, 3);
				} if (vrednostKock[1] == 5) {
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPik, yVseKocke, 3, 3);
				} if (vrednostKock[0] == 6) {
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 3);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 3);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiPrvi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				} if (vrednostKock[1] == 6) {
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 3);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 3);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 1, 5);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 1);
					PikaNaKocki(g2d, xDveKockiDrugi, velikostKocke, velikostPik, velikostPolja, yVseKocke, 5, 5);
				}
			}
		}
		
	}
	
	private void PikaNaKocki(Graphics2D g2d, int xDveKocki, int velikostKocke, int velikostPik, int velikostPolja, int yVseKocke, int faktorX, int faktorY) {
		g2d.fillOval(xDveKocki + faktorX * velikostKocke / 7,  yVseKocke + faktorY * velikostKocke / 7, velikostPik, velikostPik);
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
