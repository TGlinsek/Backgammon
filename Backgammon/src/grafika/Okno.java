package grafika;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import grafika.ColorChooserButton.ColorChangedListener;

public class Okno extends JFrame{
	
	//Barva označen gumb
	private Color barva = new Color(131, 140, 150);
	
	//Izbira igralcev - zgornji del
	private JLabel labelIgralec1 = new JLabel("Igralec 1");
	public VrstaIgralca igralec1; //Spremeni, če kliknjen clovek1/racunalnik1
	private JButton clovek1 = new JButton("  Človek  ");
	private JButton racunalnik1 = new JButton("Računalnik");
	private ColorChooserButton colorChooser1 = new ColorChooserButton(Color.WHITE); //izbira barve igralca
	public Color barvaIg1 = Color.WHITE;
	
	private JLabel labelIgralec2 = new JLabel("Igralec 2");
	public VrstaIgralca igralec2;
	private JButton clovek2 = new JButton("  Človek  ");
	private JButton racunalnik2 = new JButton("Računalnik");
	private ColorChooserButton colorChooser2 = new ColorChooserButton(Color.BLACK);
	public Color barvaIg2 = Color.BLACK;
	
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
//če tega ni ne poravna vredu (vrsta s praznimi Labels, se je ne vidi)
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
		a.gridx = 0; a.gridy = 0; a.gridwidth = 2;
		a.anchor = GridBagConstraints.CENTER;
		a.insets = new Insets(5,0,5,0); // top padding
		JPanel platIg1 = new JPanel(); //V polju gridBagLayouta lahko le ne element
		platIg1.add(labelIgralec1);
		platIg1.add(colorChooser1);
		this.add(platIg1, a);
		
		
		GridBagConstraints b = new GridBagConstraints();
		b.gridx = 2; b.gridy = 0; b.gridwidth = 2;
		b.anchor = GridBagConstraints.CENTER;
		b.insets = new Insets(5,0,5,0);
		JPanel platIg2 = new JPanel();
		platIg2.add(labelIgralec2);
		platIg2.add(colorChooser2);
		this.add(platIg2, b);
		

// ------- druga vrstica
// Gumbi za izbiro računalnik ali človek
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(5,10,5,5);
		this.add(clovek1, c);
		
		GridBagConstraints d = new GridBagConstraints();
		d.gridx = 1; d.gridy = 1;
		d.anchor = GridBagConstraints.LINE_START;
		d.insets = new Insets(5,5,5,10);
		this.add(racunalnik1, d);
		
		GridBagConstraints e = new GridBagConstraints();
		e.gridx = 2; e.gridy = 1;
		e.anchor = GridBagConstraints.LINE_END;
		e.insets = new Insets(5,10,5,5);
		this.add(clovek2, e);
		
		GridBagConstraints f = new GridBagConstraints();
		f.gridx = 3; f.gridy = 1;
		f.anchor = GridBagConstraints.LINE_START;
		f.insets = new Insets(5,5,5,10);
		this.add(racunalnik2, f);
		
// ------- tretja vrstica
// Gumb za začetek igre
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0; g.gridy = 2; g.gridwidth = 4;
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(5,0,5,0);
		this.add(igra, g);
		
// ------- četrta vrstica 
		
		platno = new Platno(500,500);
		GridBagConstraints p = new GridBagConstraints();
	//PROBLEM: če je fill = BOTH in anchor = CENTER, prevlada fill samo razširi kolikor lahko ampak ne dada na sredino
	// platno razširi samo do kvadrata, noče bit pravokotnik ???
//		p.fill = GridBagConstraints.BOTH;
		p.anchor = GridBagConstraints.CENTER;
		p.gridx = 0; p.gridy = 3;
		p.weightx = 1.0; p.weighty = 1.0;
		p.gridwidth = 4;
		this.add(platno, p);

// ------- peta vrstica
		GridBagConstraints l = new GridBagConstraints();
		l.gridx = 0; l.gridy = 4; l.gridwidth = 4;
		l.anchor = GridBagConstraints.CENTER;
		l.insets = new Insets(5,0,5,0);
		this.add(status, l);
	
		
	
// -------	actionListeners ---------------------------------------------------------------
		//izbira barve igralec1
		colorChooser1.addColorChangedListener(new ColorChangedListener() {
		    public void colorChanged(Color newColor) {
		            barvaIg1 = newColor;
		            // naj se tekst labelIgralec1 obrava z novo barvo
		    }
		});
		
		//izbira barve igralec2
		colorChooser2.addColorChangedListener(new ColorChangedListener() {
		    public void colorChanged(Color newColor) {
		            barvaIg2 = newColor;
		            //Kot igralec1
		    }
		});
		
		//igralec1 izbira
		clovek1.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(clovek1.getBackground() != barva) {
				  clovek1.setBackground(barva);
				  racunalnik1.setBackground(null);
				  igralec1 = VrstaIgralca.C;
			  } 
		  }
		});
	
		racunalnik1.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(racunalnik1.getBackground() != barva) {
				  racunalnik1.setBackground(barva);
				  clovek1.setBackground(null);
				  igralec1 = VrstaIgralca.R;
			  }			  
		  }
		});
		
		//igalec2 izbira
		clovek2.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(clovek2.getBackground() != barva) {
				  clovek2.setBackground(barva);
				  racunalnik2.setBackground(null);
				  igralec2 = VrstaIgralca.C;
			  }
		  }
		});
	
		racunalnik2.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if(racunalnik2.getBackground() != barva) {
				  racunalnik2.setBackground(barva);
				  clovek2.setBackground(null);
				  igralec2 = VrstaIgralca.R;
			  } 	
		  }
		});
		
		//Zacni igro
		igra.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
		    //TODO
			// Začni z igro (če kliknjen med igro resetiraj igro?)
			  
			//Če se barva Ig1 == Ig2 -> izpiši v statusno vrstico.
		  }
		});
	
	
	}

}
