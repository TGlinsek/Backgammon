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
	
//	teme
	protected boolean Jungle = false;
	protected boolean BubbleGum = false;
	protected boolean Navy = false;
	protected boolean BlackAndWhite = false;
	
	protected double debelinaRobaRelativna; // debelina roba plosce
	protected double debelinaObrobeRelativna; // debelina obrobe za trikotnike, zetone
	protected double odmikRelativen; // odmik med trikotniki v polju
	
	protected double sirinaTrikotnikaRelativen; // sirinaTrikotnika žetonov
	
	protected int sirina; //sirina originalnega polja
	protected int visina; //visina originalnega polja

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
		
		this.debelinaObrobeRelativna = 0.003;
		this.debelinaRobaRelativna = 0.05;
		this.odmikRelativen = 0.09;
		
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
		int visinaTrikotnika = 3 * (Math.min(getWidth(), getHeight()) - 2 * rob) / 7;
				
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
		int spodnjaStran = 1;
		int trikotnikNaPlosci = -1;					// to je indeks za dolocen tirkotnik na plosci
		int[] xTrikotnik;
		int[] yTrikotnik;
		int zacetnaTocka;
		for (int i = 0; i < 26; i++) {
			if (i == 6 || i == 19) continue; 	// ne nariše trikotnika na barieri
			
			if (i == 13) { 						// narisal je vse trikotnike na zgornji strani in gre na spodnjo stran
				spodnjaStran = -1;
			} 
			
			if (parnost) { 						// nastavi pravo barvo trikotnikov
				g2d.setColor(barvaParnihTrikotnikov); parnost = !parnost;
			} else {
				g2d.setColor(barvaNeparnihTrikotnokov); parnost = !parnost;
			} 
			
			if (spodnjaStran == -1) { 				// doloci koordinate za trikotnik
				yTrikotnik = new int[] {velikostPolja - rob, velikostPolja - rob - visinaTrikotnika, velikostPolja - rob};
				zacetnaTocka = rob + odmikMedTrikotniki + (i - 13) * sirinaTrikotnika;
			} else {
				yTrikotnik = new int[] {rob, rob + visinaTrikotnika, rob};
				zacetnaTocka = rob + odmikMedTrikotniki + i * sirinaTrikotnika;
			}
			xTrikotnik = new int[] {zacetnaTocka, (int) (zacetnaTocka + (sirinaTrikotnika / 2 - odmikMedTrikotniki)), zacetnaTocka + sirinaTrikotnika - odmikMedTrikotniki};
			
			// narisemo trikotnik
			Polygon p = new Polygon(xTrikotnik, yTrikotnik, 3);
			g2d.fillPolygon(p);
			
//			lepse izgleda brez obrobe na trikotnikih
//			g2d.setColor(barvaObrobe);
//			g2d.setStroke(new BasicStroke((float) obroba));
//			g2d.drawPolygon(p);
			
//			narisemo zetone na triktoniku, če imamo igro
			
			if (igra != null) {
				// dolocimo kateri trikotnik iz plosce risemo
				if (0 <= i && i <= 5) trikotnikNaPlosci = -i + 11; 
				if (7 <= i && i <= 12) trikotnikNaPlosci = -i + 12;
				if (13 <= i && i <= 18) trikotnikNaPlosci = i - 1;
				if (20 <= i && i <= 25) trikotnikNaPlosci = i - 2;
				
				Trikotnik trenutniTrikotnik = igra.igralnaPlosca.plosca[trikotnikNaPlosci];
				Color barvaFigure;
				Color barvaObrobe;
				int razdaljaMedSredisciZetonov;
				
				// pogledamo kaksno barvo imamo na tem trikotniku
				if (trenutniTrikotnik.barvaFigur == Figura.BELA) {
					barvaFigure = barvaZetonaBeli;
					barvaObrobe = barvaZetonaCrni;
				}
				else {
					barvaFigure = barvaZetonaCrni;
					barvaObrobe = barvaZetonaBeli;
				}
				
				// kako skupaj moramo narisati zetone, da ne pogledajo čez trikotnik
				if (trenutniTrikotnik.stevilo * sirinaTrikotnika <= visinaTrikotnika) razdaljaMedSredisciZetonov = sirinaTrikotnika;
				else razdaljaMedSredisciZetonov = (int) ((visinaTrikotnika - sirinaTrikotnika) / (trenutniTrikotnik.stevilo - 1));
				
				int faktor;
				for (int j = 0; j < trenutniTrikotnik.stevilo; j++) {
					if (spodnjaStran == 1) faktor = spodnjaStran * j;
					else faktor = spodnjaStran * (j + 1);
					
					// narisemo zeotne
					g2d.setColor(barvaFigure);
					g2d.fillOval(xTrikotnik[0] - odmikMedTrikotniki, yTrikotnik[0] + faktor * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
					
					g2d.setColor(barvaObrobe);
					g2d.setStroke(new BasicStroke((float) obroba));
					g2d.drawOval(xTrikotnik[0] - odmikMedTrikotniki, yTrikotnik[0] + faktor * razdaljaMedSredisciZetonov, sirinaTrikotnika, sirinaTrikotnika);
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
