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

import logika.Igra;
import logika.Igralec;
import tekstovni_vmesnik.Vodja;


@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener {
	
	public Vodja vodja;
	
	protected Color barvaOzadja;
	protected Color barvaRoba;
	protected Color barvaObrobe;
	protected Color barvaZetonaBeli;
	protected Color barvaZetonaCrni;
	protected Color barvaParnihTrikotnikov;
	protected Color barvaNeparnihTrikotnokov;
	
	protected double debelinaRoba; // debelina roba plosce
	protected double debelinaObrobe; // debelina obrobe za trikotnike, zetone
	
	protected int sirina; //sirina originalnega polja
	protected int visina; //visina originalnega polja

	public Platno(int sirina, int visina) {
		super();
		setPreferredSize(new Dimension(sirina, visina));

		vodja = null;
		
		this.barvaOzadja = Color.WHITE;
		this.barvaRoba = Color.GRAY;
		this.barvaObrobe = Color.BLACK;
		this.barvaZetonaCrni = Color.BLACK;
		this.barvaZetonaBeli = Color.WHITE;
		this.barvaParnihTrikotnikov = Color.BLUE;
		this.barvaNeparnihTrikotnokov = Color.CYAN;
		
		this.debelinaObrobe = 0.003;
		this.debelinaRoba = 0.05;
		
		this.sirina = sirina;
		this.visina = visina;
		
		addMouseListener(this);
	}
	
	private double triangleWidth() {
		return (Math.min(getWidth(), getHeight()) - 2 * debelinaRoba) / 13;
	}
	
	private double triangleHeight() {
		return 3 * (Math.min(getWidth(), getHeight()) - 2 * debelinaRoba) / 7;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		int velikostPolja = (int) (Math.min(getWidth(), getHeight()));
		
		int rob = (int) (debelinaRoba * velikostPolja);
		double obroba = debelinaObrobe * velikostPolja;
				
		double w = (Math.min(getWidth(), getHeight()) - 2 * rob) / 13;
		double h = 3 * (Math.min(getWidth(), getHeight()) - 2 * rob) / 7;
		
		double odmikMedTrikotniki = 0.09 * w;
		
		double polmer = w * 0.9;
		
//		narise rob
		g2d.setColor(barvaRoba);
		g2d.fillRect(0, 0, velikostPolja, velikostPolja);
		
//		narise odzadje
		g2d.setColor(barvaOzadja);
		g2d.fillRect(rob, rob, velikostPolja - 2 * rob, velikostPolja - 2 * rob);
		
//		narise bariero
		g2d.setStroke(new BasicStroke((float) w));
		g2d.setColor(barvaRoba);
		g2d.drawLine((int) (rob + 6.5 * w), rob, (int) (rob + 6.5 * w), velikostPolja - rob);
		
//		narise tirkotnike
		boolean parnost = true;
		boolean spodnjaStran = false;
		int[] x;
		int[] y;
		int zacetnaTocka;
		for (int i = 0; i < 26; i++) {
			if (i == 6 || i == 19) continue;
			if (i == 13) {
				spodnjaStran = !spodnjaStran;
			} if (parnost) {
				g2d.setColor(barvaParnihTrikotnikov); parnost = !parnost;
			} else {
				g2d.setColor(barvaNeparnihTrikotnokov); parnost = !parnost;
			} if (spodnjaStran) {
				y = new int[] {velikostPolja - rob, (int) (velikostPolja - rob - h), velikostPolja - rob};
				zacetnaTocka = (int) (rob + odmikMedTrikotniki + (i - 13) * w);
			} else {
				y = new int[] {rob, (int) (rob + h), rob};
				zacetnaTocka = (int) (rob + odmikMedTrikotniki + i * w);
			}
			x = new int[] {zacetnaTocka, (int) (zacetnaTocka + (w / 2 - odmikMedTrikotniki)), (int) (zacetnaTocka + w - odmikMedTrikotniki)};
			Polygon p = new Polygon(x, y, 3);
			g2d.fillPolygon(p);
			g2d.setColor(barvaObrobe);
			g2d.setStroke(new BasicStroke((float) obroba));
			g2d.drawPolygon(p);
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
