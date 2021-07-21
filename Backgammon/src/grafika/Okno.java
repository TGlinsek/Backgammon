package grafika;

import javax.swing.JFrame;

public class Okno extends JFrame{
	
	public Platno platno;
	
	public Okno() {
		super();
		setTitle("Backgammon");
		platno = new Platno(500, 500);
		add(platno);
	}

}
