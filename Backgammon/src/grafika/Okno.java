package grafika;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import grafika.ColorChooserButton.ColorChangedListener;
import logika.Igralec;
import logika.ImeKocke;
import logika.Kocka;
import logika.StanjeIgre;

public class Okno extends JFrame{
	
	//Barva označenega gumba
	private Color barva = new Color(131, 140, 150);
	
	//Naslov
	private JLabel naslov = new JLabel("Backgammon");
	
	//teme
	private JPanel teme;
		private JLabel temeLabel = new JLabel("Teme : ");
		private JButton jungleButton = new JButton("Jungle");
		//-------
		private JButton bubbleGumButton = new JButton("BubbleGum");
		//-------
		private JButton navyButton = new JButton("Navy");
		//-------
		private JButton blackAndWhiteButton = new JButton("Black and White");
		//-------
		private JButton standardButton = new JButton("Standard");
	
	//Izbira igralcev 
	private JLabel labelIgralec1 = new JLabel("Igralec 1");
	public VrstaIgralca igralec1; //Spremeni, če kliknjen clovek1/racunalnik1
	private JButton clovek1 = new JButton("  Človek  ");
	private JButton racunalnik1 = new JButton("Računalnik");
//	private ColorChooserButton colorChooser1 = new ColorChooserButton(Color.WHITE); //izbira barve igralca
//	public Color barvaIg1 = Color.WHITE;
	
	private JLabel labelIgralec2 = new JLabel("Igralec 2");
	public VrstaIgralca igralec2;
	private JButton clovek2 = new JButton("  Človek  ");
	private JButton racunalnik2 = new JButton("Računalnik");
//	private ColorChooserButton colorChooser2 = new ColorChooserButton(Color.BLACK);
//	public Color barvaIg2 = Color.BLACK;
	
	//Zacni igro
	private JButton igra = new JButton("Nova igra"); 
	
	//Igralno polje 
	public Platno platno;
	
	//vrži kocke
//	private JButton vrziKocke = new JButton("Vrzi kocki");
	
	//Statusna vrstica v spodnjem delu okna
	private JLabel status = new JLabel("Izberite igralce");
	
	private Okno vrniOkno() {
		return this;
	}
	
	public Okno() { //----------------------------------------------------------------------------------------------
		
		super();
		setTitle("Backgammon");
		setResizable(false);
		
		getRootPane().setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, Color.WHITE));
		
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
		
// ------- prva vrstica - naslov
		naslov.setFont(new Font("Serif", Font.BOLD, 28));		
		
		GridBagConstraints n = new GridBagConstraints();
		n.gridx = 0; n.gridy = 0; n.gridwidth = 4;
		n.anchor = GridBagConstraints.CENTER;
		n.insets = new Insets(5,0,5,0);
		this.add(naslov, n);
		
// ------- druga vrstica - teme
		
		jungleButton.setForeground(new Color(255, 244, 230));
		jungleButton.setBackground(new Color(102, 58, 0));
		bubbleGumButton.setForeground(new Color(255, 128, 128));
		bubbleGumButton.setBackground(new Color(172, 108, 108));
		navyButton.setForeground(Color.WHITE);
		navyButton.setBackground(new Color(179, 241, 255));
		blackAndWhiteButton.setBackground(Color.BLACK);
		blackAndWhiteButton.setForeground(Color.WHITE);
		standardButton.setBackground(new Color(210, 166, 121));
		standardButton.setForeground(new Color(255, 242, 230));
		
		teme = new JPanel();
		teme.setBorder(BorderFactory.createLineBorder(Color.black));
		teme.add(temeLabel); teme.add(jungleButton); teme.add(bubbleGumButton); teme.add(navyButton);teme.add(blackAndWhiteButton); teme.add(standardButton);
		
		GridBagConstraints t = new GridBagConstraints();
		t.gridx = 0; t.gridy = 1; t.gridwidth = 4;
		t.anchor = GridBagConstraints.CENTER;
		t.insets = new Insets(5,0,5,0);
		this.add(teme, t);
		
// ------- tretja vrstica - Labeli igralci in izbira barve
		GridBagConstraints a = new GridBagConstraints();
		a.gridx = 0; a.gridy = 2; a.gridwidth = 2;
		a.anchor = GridBagConstraints.CENTER;
		a.insets = new Insets(5,0,5,0); // top padding
		JPanel platIg1 = new JPanel(); //V polju gridBagLayouta lahko le en element
		platIg1.add(labelIgralec1);
//		platIg1.add(colorChooser1);
		this.add(platIg1, a);
		
		
		GridBagConstraints b = new GridBagConstraints();
		b.gridx = 2; b.gridy = 2; b.gridwidth = 2;
		b.anchor = GridBagConstraints.CENTER;
		b.insets = new Insets(5,0,5,0);
		JPanel platIg2 = new JPanel();
		platIg2.add(labelIgralec2);
//		platIg2.add(colorChooser2);
		this.add(platIg2, b);
		

// ------- četrta vrstica - Gumbi za izbiro računalnik ali človek
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 3;
		c.anchor = GridBagConstraints.LINE_END;
		c.insets = new Insets(5,10,5,5);
		this.add(clovek1, c);
		
		GridBagConstraints d = new GridBagConstraints();
		d.gridx = 1; d.gridy = 3;
		d.anchor = GridBagConstraints.LINE_START;
		d.insets = new Insets(5,5,5,10);
		this.add(racunalnik1, d);
		
		GridBagConstraints e = new GridBagConstraints();
		e.gridx = 2; e.gridy = 3;
		e.anchor = GridBagConstraints.LINE_END;
		e.insets = new Insets(5,10,5,5);
		this.add(clovek2, e);
		
		GridBagConstraints f = new GridBagConstraints();
		f.gridx = 3; f.gridy = 3;
		f.anchor = GridBagConstraints.LINE_START;
		f.insets = new Insets(5,5,5,10);
		this.add(racunalnik2, f);
		
// ------- peta vrstica - Gumb za začetek igre
		GridBagConstraints g = new GridBagConstraints();
		g.gridx = 0; g.gridy = 4; g.gridwidth = 4;
		g.anchor = GridBagConstraints.CENTER;
		g.insets = new Insets(5,0,20,0);
		this.add(igra, g);
		
// ------- šesta vrstica - platno - Igralno polje		
		platno = new Platno(500,500);
		GridBagConstraints p = new GridBagConstraints();
		p.anchor = GridBagConstraints.PAGE_END;
		p.gridx = 0; p.gridy = 5;
		p.gridwidth = 4;
		this.add(platno, p);

// ------- sedma vrstica - met kocke
//		GridBagConstraints k = new GridBagConstraints();
//		k.gridx = 0; k.gridy = 6; k.gridwidth = 4;
//		k.anchor = GridBagConstraints.PAGE_START;
//		k.insets = new Insets(5,0,5,0);
//		this.add(vrziKocke, k);
		
// ------- osma vrstica - Statusna vrstica
		GridBagConstraints l = new GridBagConstraints();
		l.gridx = 0; l.gridy = 6; l.gridwidth = 4;
		l.anchor = GridBagConstraints.PAGE_START;
		l.insets = new Insets(5,0,5,0);
		this.add(status, l);
	
// ------- actionListeners ---------------------------------------------------------------
		//izbira barve igralec1
//		colorChooser1.addColorChangedListener(new ColorChangedListener() {
//		    public void colorChanged(Color newColor) {
//		            barvaIg1 = newColor;
//		    }
//		});
//		
//		//izbira barve igralec2
//		colorChooser2.addColorChangedListener(new ColorChangedListener() {
//		    public void colorChanged(Color newColor) {
//		            barvaIg2 = newColor;
//		    }
//		});
		
	//Teme
		jungleButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  platno.nastaviTemo("Jungle");
			  platno.repaint();
		  }
		});
		bubbleGumButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  platno.nastaviTemo("BubbleGum");
			  platno.repaint();
		  }
		});
		navyButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  platno.nastaviTemo("Navy");
			  platno.repaint();
		  }
		});
		blackAndWhiteButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  platno.nastaviTemo("BlackAndWhite");
			  platno.repaint();
		  }
		});
		standardButton.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  platno.nastaviTemo("Standard");
			  platno.repaint();
		  }
		});
		
	//igralec1 izbira (Človek/Računalnik)
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
		
	//igralec2 izbira
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

			  if (igralec1 == null || igralec2 == null) {
				  status.setText("Najprej moraš nastaviti igralce!");
			  } else {
			  
				  Vodja vodja = new Vodja(vrniOkno());
	
				  platno.nastaviIgro(vodja);
				  
				  
				  vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
				  vodja.vrstaIgralca.put(Igralec.BELI, igralec1);
				  vodja.vrstaIgralca.put(Igralec.CRNI, igralec2);
				  
				  platno.vodja.igramoNovoIgro();
			  }
		    //TODO
			// Začni z igro (če kliknjen med igro resetiraj igro?)
			
			//Če se barva Ig1 == Ig2 -> izpiši v statusno vrstico.
			//Kaj pa če barve niso iste amapak samo podobne? 
		  }
		});
		/*
		vrziKocke.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  if (platno.igra.trenutnoStanje == StanjeIgre.METANJE_KOCK) {
			//Ali bomo tu preverjali, če lahko vrže kocki?
				  	platno.igra.vrziKocki();
			  }
				else {
				  status.setText("Ne moreš še metati kock!");
			  }
		    platno.repaint();
		  }
		});
		*/
	
	
	}//------------------------------------------------------------------------------------------------------------
	
	
	public void osveziGUI() {
		if (platno.vodja.igra == null) {
			status.setText("Igra ne poteka");
		}
		else {
			switch(platno.vodja.igra.trenutnoStanje) {
			case METANJE_KOCK:  // samo če je človek na vrsti
				status.setText(platno.vodja.igra.igralecNaVrsti + ", lahko vržeš kocke!");
				break;
			case PREMIKANJE_FIGUR:
				status.setText("Na potezi je " + platno.vodja.igra.igralecNaVrsti + 
						" (" + platno.vodja.vrstaIgralca.get(platno.vodja.igra.igralecNaVrsti) + 
						")"); 
				break;
			
			case ZMAGA_CRNI:
				status.setText("ZMAGAL ČRNI - " + 
						platno.vodja.vrstaIgralca.get(Igralec.CRNI));
				break;
			case ZMAGA_BELI:
				status.setText("ZMAGAL BELI - " + 
						platno.vodja.vrstaIgralca.get(Igralec.BELI));
				break;
			}
		}
		platno.repaint();
	}
}