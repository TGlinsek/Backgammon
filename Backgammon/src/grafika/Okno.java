package grafika;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class Okno extends JFrame{
	
	//Izbira igralcev - zgornji del
	private JLabel igralec1 = new JLabel("Igralec 1");
	private JButton clovek1 = new JButton("  Človek  ");
	private JButton racunalnik1 = new JButton("Računalnik");
	
	private JLabel igralec2 = new JLabel("Igralec 2");
	private JButton clovek2 = new JButton("  Človek  ");
	private JButton racunalnik2 = new JButton("Računalnik");
	
	//Zacni igro
	private JButton igra = new JButton("Nova igra"); 
	
	//Igralno polje 
	public Platno platno;
	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status = new JLabel("Izberite igralce");
	
	
	
	public Okno() {
		
		super();
		setTitle("Backgammon");
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

// ------- referenčna vrstica
//če tega ni ne poravna vredu
		JLabel test0= new JLabel();
		GridBagConstraints t0 = new GridBagConstraints();
		t0.gridx = 0; t0.gridy = 0; t0.weightx = 1.0;
		this.add(test0,t0);
		
		JLabel test1= new JLabel();
		GridBagConstraints t1 = new GridBagConstraints();
		t1.gridx = 1; t1.gridy = 0; t1.weightx = 1.0;
		this.add(test1,t1);
		
		JLabel test2= new JLabel();
		GridBagConstraints t2 = new GridBagConstraints();
		t2.gridx = 2; t2.gridy = 0; t2.weightx = 1.0;
		this.add(test2,t2);
		
		JLabel test3= new JLabel();
		GridBagConstraints t3 = new GridBagConstraints();
		t3.gridx = 3; t3.gridy = 0; t3.weightx = 1.0;
		this.add(test3,t3);
		
// ------- prva vrstica
// Labeli kateri igralci
		GridBagConstraints a = new GridBagConstraints();
		a.gridx = 0; a.gridy = 0;
		a.anchor = GridBagConstraints.CENTER;
		a.gridwidth = 2;		
		this.add(igralec1, a);
		
		GridBagConstraints b = new GridBagConstraints();
		b.gridx = 2; b.gridy = 0;
		b.anchor = GridBagConstraints.CENTER;
		b.gridwidth = 2;
		this.add(igralec2, b);

// ------- druga vrstica
// Gumbi za izbiro računalnik ali človek
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		this.add(clovek1, c);
		
		GridBagConstraints d = new GridBagConstraints();
		d.gridx = 1; d.gridy = 1;
		d.anchor = GridBagConstraints.LINE_START;
		this.add(racunalnik1, d);
		
		GridBagConstraints e = new GridBagConstraints();
		e.gridx = 2; e.gridy = 1;
		e.anchor = GridBagConstraints.LINE_END;
		this.add(clovek2, e);
		
		GridBagConstraints f = new GridBagConstraints();
		f.gridx = 3; f.gridy = 1;
		f.anchor = GridBagConstraints.LINE_START;
		this.add(racunalnik2, f);
		
// ------- tretja vrstica
// Gumb za začetek igre
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0; g.gridy = 2; g.gridwidth = 4;
		g.anchor = GridBagConstraints.CENTER;
		this.add(igra, g);
		
// ------- četrta vrstica 
		
		platno = new Platno(500,500);
		GridBagConstraints p = new GridBagConstraints();
	//PROBLEM: če je fill = BOTH in anchor = CENTER, prevlada fill samo razširi kolikor lahko ampak ne dada na sredino
	// platno razširi samo do kvadrata, nočebit pravokotnik ???
//		p.fill = GridBagConstraints.BOTH;
		p.anchor = GridBagConstraints.CENTER;
		p.gridx = 0; p.gridy = 3;
		p.weightx = 1.0; p.weighty = 1.0;
		p.gridwidth = 4;
		this.add(platno, p);
	}

}
