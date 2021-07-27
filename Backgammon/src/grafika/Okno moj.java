package grafika;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igralec;
import grafika.VrstaIgralca;


/**
 * Glavno okno aplikacije hrani trenutno stanje igre in nadzoruje potek
 * igre.
 *
 */
@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	/**
	 * JPanel, v katerega igramo
	 */
	private Platno platno;
	
	
	// Statusna vrstica v spodnjem delu okna
	private JLabel status;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	
	private JMenuItem crniZacne;
	private JMenuItem beliZacne;
	private JMenuItem kockaZacne;
	
	private JMenuItem levaStran;
	private JMenuItem desnaStran;
	
	private JMenuItem spodnjaPolovica;
	private JMenuItem zgornjaPolovica;
	
	private JMenuItem pritrdilno;
	private JMenuItem odklonilno;
	
	private Map<JMenuItem, String> imenik;
	
	/**
	 * KONSTRUKTOR - ustvari novo glavno okno in priƒçni igrati igro.
	 */
	public Okno() {
		
		this.setTitle("Backgammon");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		// menu
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		
		
		imenik = new HashMap<JMenuItem, String>();
		
		
		JMenu igra_menu = new JMenu("DoloËi igralce");
		menu_bar.add(igra_menu);
		
		igraClovekRacunalnik = new JMenuItem("»rni = Ëlovek, beli = raËunalnik");
		igra_menu.add(igraClovekRacunalnik);
		igraClovekRacunalnik.addActionListener(this);
		imenik.put(igraClovekRacunalnik, igraClovekRacunalnik.getName());
		
		igraRacunalnikClovek = new JMenuItem("»rni = raËunalnik, beli = Ëlovek");
		igra_menu.add(igraRacunalnikClovek);
		igraRacunalnikClovek.addActionListener(this);
		imenik.put(igraRacunalnikClovek, igraRacunalnikClovek.getName());
		
		igraClovekClovek = new JMenuItem("»rni = Ëlovek, beli = Ëlovek");
		igra_menu.add(igraClovekClovek);
		igraClovekClovek.addActionListener(this);
		imenik.put(igraClovekClovek, igraClovekClovek.getName());
		
		igraRacunalnikRacunalnik = new JMenuItem("»rni = raËunalnik, beli = raËunalnik");
		igra_menu.add(igraRacunalnikRacunalnik);
		igraRacunalnikRacunalnik.addActionListener(this);
		imenik.put(igraRacunalnikRacunalnik, igraRacunalnikRacunalnik.getName());
		

		// menu2
		JMenu igra_menu2 = new JMenu("Kdo zaËne?");
		menu_bar.add(igra_menu2);
		
		crniZacne = new JMenuItem("»rni");
		igra_menu2.add(crniZacne);
		crniZacne.addActionListener(this);
		imenik.put(crniZacne, crniZacne.getName());
		
		beliZacne = new JMenuItem("Beli");
		igra_menu2.add(beliZacne);
		beliZacne.addActionListener(this);
		imenik.put(beliZacne, beliZacne.getName());
		
		kockaZacne = new JMenuItem("Naj kocke doloËijo");
		igra_menu2.add(kockaZacne);
		kockaZacne.addActionListener(this);
		imenik.put(kockaZacne, kockaZacne.getName());
		
		
		
		// menu3
		JMenu igra_menu3 = new JMenu("Kje zaËneta in konËata igralca?");
		menu_bar.add(igra_menu3);
		
		levaStran = new JMenuItem("Na levi strani");
		igra_menu3.add(levaStran);
		levaStran.addActionListener(this);
		imenik.put(levaStran, levaStran.getName());
		
		desnaStran = new JMenuItem("Na desni strani");
		igra_menu3.add(desnaStran);
		desnaStran.addActionListener(this);
		imenik.put(desnaStran, desnaStran.getName());
		
		
		
		// menu4
		JMenu igra_menu4 = new JMenu("Kje zaËne Ërni?");
		menu_bar.add(igra_menu4);
		
		spodnjaPolovica = new JMenuItem("Na spodnji polovici");
		igra_menu4.add(spodnjaPolovica);
		spodnjaPolovica.addActionListener(this);
		imenik.put(spodnjaPolovica, spodnjaPolovica.getName());
		
		zgornjaPolovica = new JMenuItem("Na zgornji polovici");
		igra_menu4.add(zgornjaPolovica);
		zgornjaPolovica.addActionListener(this);
		imenik.put(zgornjaPolovica, zgornjaPolovica.getName());
		
		
		// menu5
		JMenu igra_menu5 = new JMenu("ZaËni igro!");
		menu_bar.add(igra_menu5);
		
		pritrdilno = new JMenuItem("Ja, prosim");
		igra_menu5.add(pritrdilno);
		pritrdilno.addActionListener(this);
		imenik.put(pritrdilno, pritrdilno.getName());
		
		odklonilno = new JMenuItem("Ne öe");
		igra_menu5.add(odklonilno);
		odklonilno.addActionListener(this);
		imenik.put(odklonilno, odklonilno.getName());
		
		
		// igralno polje oz. platno
		platno = new Platno();
		
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		getContentPane().add(platno, polje_layout);
		
		// statusna vrstica za sporoƒçila
		status = new JLabel();
		status.setFont(
				new Font(
						status.getFont().getName(),
						status.getFont().getStyle(),
						20
				)
		);
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("IZBERI IGRO!");
	}
	
	
	public void nastaviVelikostPoljVPlatnu() {
		this.platno.nastaviVelikostPolj();
	}
	
	
	private static void napolniSlovarZaVrsteIgralcev(VrstaIgralca crniIgralec, VrstaIgralca beliIgralec) {
		Vodja.vrstaIgralca = new EnumMap<Igralec, VrstaIgralca>(Igralec.class);
		Vodja.vrstaIgralca.put(Igralec.CRNI, crniIgralec);
		Vodja.vrstaIgralca.put(Igralec.BELI, beliIgralec);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(imenik.get(e.getSource())) {
		case "igraClovekRacunalnik": napolniSlovarZaVrsteIgralcev(VrstaIgralca.C, VrstaIgralca.R); break;
		case "igraRacunalnikClovek": napolniSlovarZaVrsteIgralcev(VrstaIgralca.R, VrstaIgralca.C); break;
		case "igraClovekClovek": napolniSlovarZaVrsteIgralcev(VrstaIgralca.C, VrstaIgralca.C); break;
		case "igraRacunalnikRacunalnik": napolniSlovarZaVrsteIgralcev(VrstaIgralca.R, VrstaIgralca.R); break;
			
		case "crniZacne": Vodja.nastaviZacetnoBarvo(Igralec.CRNI); break;
		case "beliZacne": Vodja.nastaviZacetnoBarvo(Igralec.BELI); break;
		case "kockaZacne": Vodja.nastaviZacetnoBarvo(null); break;
			
		case "levaStran": Vodja.nastaviStran(true); break;
		case "desnaStran": Vodja.nastaviStran(false); break;
			
		case "spodnjaPolovica": Vodja.nastaviPolovico(true); break;
		case "zgornjaPolovica": Vodja.nastaviPolovico(false); break;
			
		case "pritrdilno": Vodja.igramoNovoIgro(); break;
		case "odklonilno": break;
			
		default: throw new java.lang.RuntimeException("NeobstojeË niz! To se naj ne bi zgodilo ...");
		}
	}
	
	
	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ne poteka");
		}
		else {
			// Vodja.igra.spremeniStanjeIgre(Vodja.igra.zadnjaIgranaPoteza);
			switch(Vodja.igra.trenutnoStanje) {
			case NEODLOCENO: status.setText("Neodloƒçeno!"); break;
			case V_TEKU:
				status.setText("Na potezi je " + Vodja.igra.igralecNaPotezi + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.igralecNaPotezi)); 
				break;
			case ZMAGA_CRNI:
				status.setText("ZMAGAL ƒåRNI - " + 
						Vodja.vrstaIgralca.get(Igralec.CRNI));
				break;
			case ZMAGA_BELI:
				status.setText("ZMAGAL BELI - " + 
						Vodja.vrstaIgralca.get(Igralec.BELI));
				break;
			}
		}
		platno.repaint();
	}
}