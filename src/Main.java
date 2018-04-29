import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import Baum.TElement;
import Baum.treeListener;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;

import java.awt.Color;

public class Main {
	private static final int FEHLER = 0;														
	private static final int OK = 1;															// Konstanten für den Status der Funktion insertElement() zum Einfügen von Elementen
	private static final int VORHANDEN = 2;
	private static final int GELOESCHT = 1;														// Konstanten für den Status der Funktion deleteElement() zum Einfügen von Elementen
	private static final int NICHT_GEFUNDEN = 2;

	private JFrame frmBinrerBaum;
	private JTextField addTxtF;
	private JTextField orderTxtF;
	private TreePanel treePanel;
	private JLabel statusMessage;
	
	public static TElement root;
	private JTextField randomTxtF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmBinrerBaum.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBinrerBaum = new JFrame();
		frmBinrerBaum.setResizable(false);
		frmBinrerBaum.setTitle("Bin\u00E4rer Baum");
		frmBinrerBaum.setBounds(100, 100, 1200, 720);
		frmBinrerBaum.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBinrerBaum.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel.setBounds(10, 11, 1174, 61);
		panel.setLayout(null);
		frmBinrerBaum.getContentPane().add(panel);
		
		JLabel label = new JLabel("Wert");
		label.setBounds(10, 11, 94, 14);
		panel.add(label);
		
		JButton addBtn = new JButton("hinzuf\u00FCgen");
		@SuppressWarnings("serial")
		Action addBtnAction = new AbstractAction() {											// Aktion als Variable erstellen, um sie sowohl für den addBtn als auch für addTxtF zu nutzen
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int insertWert = Integer.parseInt(addTxtF.getText());						// Überprüfen, ob eingegebener Text eine ganze Zahl ist
					if(root == null) {															// Inserwert behandelt root == null nicht, also Root setzen, falls nicht vorhanden
						root = new TElement(treePanel);
						root.setWert(insertWert);
						statusMessage.setText("Root mit Wert " + insertWert + " gesetzt");
					} else {																	
							TElement addElement = new TElement(treePanel);						// Element mit dem eingegebenem Wert in den Baum einfügen
							addElement.setWert(insertWert);				
							switch (insertElement(addElement)) {
							case FEHLER:
								statusMessage.setText("Beim Einfügen ist ein Fehler aufgetreten");
								break;
							case OK:
								statusMessage.setText("Element mit Wert " + insertWert + " eingefügt");
								break;
							case VORHANDEN:
								statusMessage.setText("Element mit Wert " + insertWert + " ist bereits vorhanden");
								break;
							}
					}		
				} catch (NumberFormatException ex) {											// Fange, wenn eingefügter Text kein Integer ist
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Ungültige Eingabe");
				} finally {
					addTxtF.requestFocus();														// Für neue Eingabe vorbereiten
					addTxtF.selectAll();		
				}
			}
		};
		addBtn.addActionListener(addBtnAction);
		addBtn.setBounds(120, 26, 100, 23);
		panel.add(addBtn);
		
		addTxtF = new JTextField();
		addTxtF.setToolTipText("Nur ganze Zahlen");
		addTxtF.setColumns(10);
		addTxtF.addActionListener(addBtnAction);
		addTxtF.setBounds(10, 27, 100, 20);
		panel.add(addTxtF);
		
		JButton searchBtn = new JButton("suchen");
		searchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int searchWert = Integer.parseInt(addTxtF.getText());						// Überprüfen, ob eingegebener Wert eine ganze Zahl ist
					if(FindElement(searchWert)==null) {											// Element mit dem Wert im Baum suchen
						statusMessage.setText("Element mit dem Wert " + searchWert + " ist nicht im Baum vorhanden");
					} else {
						statusMessage.setText("Element mit dem Wert " + searchWert + " ist im Baum vorhanden");
					}
				} catch (NumberFormatException ex){												// Fange, wenn gesuchter Text kein Integer ist
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Ungültige Eingabe");
				} finally {
					addTxtF.requestFocus();													// Für neue Eingabe vorbereiten
					addTxtF.selectAll();		
				}
			}
		});
		searchBtn.setBounds(230, 26, 82, 23);
		panel.add(searchBtn);
		
		JButton deleteBtn = new JButton("l\u00F6schen");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int deleteWert = Integer.parseInt(addTxtF.getText());						// Überprüfen, ob einegegebener Wert eine ganze Zahl ist
					if (FindElement(deleteWert) == null) {										// Überprüfen, ob Wert im Baum vorhanden ist
						statusMessage.setText("Element mit dem Wert " + deleteWert + " nicht im Baum vorhanden");
						return;
					}
					switch(deleteElement(deleteWert)) {											// Element mit dem Wert im Baum löschen
					case FEHLER:
						statusMessage.setText("Beim Löschen ist ein Fehler aufgetreten");
						return;
					case OK:
						statusMessage.setText("Element mit Wert " + deleteWert + " gelöscht");
						break;
					case NICHT_GEFUNDEN:
						statusMessage.setText("Element mit Wert " + deleteWert + " nicht gefunden");
						break;
					}
					if ( root ==  null ) {
						treePanel.paintComponent(treePanel.getGraphics());						// Panel manuell erneuern, wenn Baum nicht mnehr gesetzt ist
					}
				} catch (NumberFormatException ex){												// Fange, wenn zu löschender Text kein Integer ist
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Ungültige Eingabe");
				} finally {
					addTxtF.requestFocus();														// Für neue Eingabe vorbereiten
					addTxtF.selectAll();		
				}
			}
		});
		deleteBtn.setBounds(323, 26, 94, 23);
		panel.add(deleteBtn);
		
		JButton clearTreeBtn = new JButton("Alles l\u00F6schen");
		clearTreeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				root = null;
				treePanel.paintComponent(treePanel.getGraphics());
				orderTxtF.setText("");
				addTxtF.setText("");
				addTxtF.requestFocus();	
				statusMessage.setText("Baum gelöscht");
			}
		});
		clearTreeBtn.setBounds(1050, 26, 114, 23);
		panel.add(clearTreeBtn);
		
		JButton randomBtn = new JButton("Zuf\u00E4llig hinzuf\u00FCgen");
		@SuppressWarnings("serial")
		Action addRandomBtnAction = new AbstractAction() {										// Aktion als Variable erstellen, um sie sowohl für den radnomBtn als auch für randomTxtF zu nutzen
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int addCount = Integer.parseInt(randomTxtF.getText());						// Überprüfen, ob eingegebener
					Random randomWert = new Random();
					if(root == null) {															// Root setzen, falls nicht vorhanden
						root = new TElement(treePanel);
						root.setWert(randomWert.nextInt(1000));
					}
					for(int count = 0; count < addCount; count++) {								// Element in der Schleife setzen und einfügen
						TElement addElement = new TElement(treePanel);							
						addElement.setWert(randomWert.nextInt(1000));				
						switch (insertElement(addElement)) {
						case FEHLER:
							statusMessage.setText("Beim Einfügen ist ein Fehler aufgetreten");
							return;
						}
					}
					statusMessage.setText("Erfolgreich eingefügt");
						
				} catch (NumberFormatException ex) {											// Fange, wenn eingefügter Text kein Integer ist
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Ungültige Eingabe");
				} finally {
					randomTxtF.requestFocus();													// Für neue Eingabe vorbereiten
					randomTxtF.selectAll();		
				}
			}
		};
		randomBtn.addActionListener(addRandomBtnAction);
		randomBtn.setBounds(688, 26, 146, 23);
		panel.add(randomBtn);
		
		randomTxtF = new JTextField();
		randomTxtF.setToolTipText("Nur ganze Zahlen");
		randomTxtF.setColumns(10);
		randomTxtF.setBounds(578, 27, 100, 20);
		randomTxtF.addActionListener(addRandomBtnAction);
		panel.add(randomTxtF);
		
		JLabel lblAnzahl = new JLabel("Anzahl");
		lblAnzahl.setBounds(578, 11, 94, 14);
		panel.add(lblAnzahl);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_1.setBounds(10, 615, 1174, 43);
		frmBinrerBaum.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JButton postOrderBtn = new JButton("PostOrder");
		postOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(PostOrder(root));										// Baum mit PostOrder ausgeben
				} catch (NullPointerException ex) {											// Fangen, wenn noch kein Element im Baum ist
					System.err.println(ex);													// Fehler in der Konsole und auf dem Bildschirm ausgeben
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");							
					orderTxtF.setText("");													// Ausgabe zurücksetzen
					addTxtF.requestFocus();													// Für neue Eingabe vorbereiten
					addTxtF.selectAll();
				}
			}
		});
		postOrderBtn.setBounds(210, 11, 95, 23);
		panel_1.add(postOrderBtn);
		
		orderTxtF = new JTextField();
		orderTxtF.setEditable(false);
		orderTxtF.setBounds(315, 12, 849, 20);
		panel_1.add(orderTxtF);
		orderTxtF.setColumns(10);
		
		JButton inOrderBtn = new JButton("InOrder");
		inOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(InOrder(root));										// Baum mit InOrder ausgeben
				} catch (NullPointerException ex) {											// Fangen, wenn noch kein Element im Baum ist
					System.err.println(ex);													// Fehler in der Konsole und auf dem Bildschirm ausgeben
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");							
					orderTxtF.setText("");													// Ausgabe zurücksetzen
					addTxtF.requestFocus();													// Für neue Eingabe vorbereiten
					addTxtF.selectAll();
				}
			}
		});
		inOrderBtn.setBounds(115, 11, 85, 23);
		panel_1.add(inOrderBtn);
		
		JButton preOrderBtn = new JButton("PreOrder");
		preOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(PreOrder(root));										// Baum in PreOrder ausgeben
				} catch (NullPointerException ex) {											// Fangen, wenn noch kein Element im Baum ist
					System.err.println(ex);													// Fehler in der Konsole und auf dem Bildschirm ausgeben
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");							
					orderTxtF.setText("");													// Ausgabe zurücksetzen
					addTxtF.requestFocus();													// Für neue Eingabe vorbereiten
					addTxtF.selectAll();
				}
			}
		});
		preOrderBtn.setBounds(10, 11, 95, 23);
		panel_1.add(preOrderBtn);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_2.setBounds(0, 669, 1194, 23);
		frmBinrerBaum.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		statusMessage = new JLabel("Lege den Wert des Root Elements fest!");
		statusMessage.setBounds(5, 4, 950, 14);
		panel_2.add(statusMessage);
		
		JScrollPane scrollPane = new JScrollPane(treePanel);
		scrollPane.setWheelScrollingEnabled(false);
		scrollPane.setBounds(10, 83, 1174, 521);
		frmBinrerBaum.getContentPane().add(scrollPane);
		
		treePanel = new TreePanel();
		scrollPane.setViewportView(treePanel);
		treePanel.setLayout(null);
	}
	
	/**
	 * Fügt ein Objekt der Klasse TElement in einem Baum ein
	 * param:	r: TElement	: aktuelles Element
	 * 			e: TElement	: einzufügendes Element
	 * return:	 : int		: 0: Fehler, 1: OK, 2: Element schon im Baum
	 * Version 1
	 * Höhe setzen entfernt
	 */
	private static int insertElement(TElement e) {
		return insertElement(root, e);															// Root als standard Element, wo eingefügt wird
	}
	private static int insertElement(TElement r, TElement e) {
		try {
			int status = FEHLER;																// Status der Funktion für Wiedergabe
			if (r.getWert() == e.getWert())	{													// Überprüfen, ob die Werte des aktuellen Elements und des Einzufügenden Elements gleich sind
				return VORHANDEN;																		
			} else if(e.getWert() < r.getWert()) {												// Links rekursiv einfügen, wenn der Wert des einzufügenden Elements kleiner als des aktuellen Elements ist und
				if(r.getLeft() != null) {														// ein linkes Element gesetzt ist
					status = insertElement(r.getLeft(), e);
				} else {
					r.setLeft(e);																// Einzufügendes Element im aktuellen Element links setzen
					status = OK;
				}
			} else if(e.getWert() > r.getWert()) {												// Rechts rekursiv einfügen, wenn der Wert des einzufügenden Elements größer als des aktuellen Elements ist und
				if(r.getRight() != null) {														// ein rechtes Element gesetzt ist
					status = insertElement(r.getRight(), e);
				} else {
					r.setRight(e);																// Einzufügendes Element im aktuellen Element rechts setzen
					status = OK;
				}
			}
			r.setHoehe(calCurHoehe(r));															// Höhe aktualisieren
			return status;																		// Status wiedergeben
		} catch (Exception ex) {
			return FEHLER;
		}
	}
	
	/**
	 * Sucht ein Element nach eingegebenem Wert
	 * param:	r	: TElement	: Baum
	 * 			wert: int		: Wert
	 * return:	 	: int		: 0: Fehler, 1: Gefunden, 2: Nicht gefunden
	 * Version 1
	 * Klappt
	 */
	private static TElement FindElement(int wert) {
		return FindElement(root, wert);
	}
	private static TElement FindElement(TElement r, int wert) throws NullPointerException{
		try {
			if(r.getWert() == wert) {															// Wenn der Wert des aktuellen Elements dem gesuchten Element gleicht, aktuelles Element zurückgeben
				return r;
			} else {
				if (r.getLeft() != null) {
					TElement leftElement = FindElement(r.getLeft(), wert);						// Im linken Zweig rekursiv suchen
					if(leftElement!= null){														
						return leftElement;														// Gefundenes Element zurückgeben, falls gefunden
					}
				}
				if (r.getRight() != null) {
					TElement rightElement = FindElement(r.getRight(), wert);
					if(rightElement != null){
						return rightElement;													// Gefundenes Element zurückgeben, falls gefunden
					}
				}
			} 
			return null;																		
		} catch (Exception ex) {																// Fange, falls Baum nicht gesetzt ist
			System.err.println(ex);																// Fehler in der Konsole ausgeben
			return null;
		}
	}
	
	/**
	 * Löscht ein Element nach eingegebenem Wert 
	 * param:	r	: TElement	: Baum
	 * 			wert: int		: Wert
	 * Version 3
	 * Komplett überarbeitet 
	 */
	private static int deleteElement(int wert) {
		try {
			deleteElement(root, wert);
			return GELOESCHT;
		} catch(Exception ex) {
			System.err.println(ex);
			return FEHLER;
		}
	}
	private static TElement deleteElement(TElement r, int wert) throws Exception{
		try {
			if (r == null) {
				return null;
			}
			if(wert < r.getWert()) {
				r.setLeft(deleteElement(r.getLeft(), wert));									// Links rekursiv, wenn Wert kleiner
			} else if(wert > r.getWert()){
				r.setRight(deleteElement(r.getRight(), wert));									// Recht rekursiv, wenn Wert größer
			} else {																			// Passendes Element gefunden
				if(r.getLeft() != null) {
					if(r.getLeft().getRight() != null) {										// LR ersetzen
						r.setWert(biggestWert(r.getLeft()));									// Höchsten darunter liegenden Wert setzen
						r.setLeft(removeBiggestWertElement(r.getLeft()));						// Element ohne Element mit höchstem Wert setzen
					} else {																	// LL  ersetzen
						if(r.getRight() != null) {
							r.getLeft().setRight(r.getRight());									// Rechtes Element an vorgesehenem linkem Element verknüpfen
						}
						r.replace(r.getLeft());													// Element ohne aktuelles Element setzen
					}
				} else if(r.getRight() != null) {
					if(r.getRight().getLeft() != null) {										// RL  ersetzen
						r.setWert(smallestWert(r.getRight()));									// Niedrigsten darunter liegenden Wert setzen
						r.setRight(removeSmallestWertElement(r.getRight()));					// Element ohne Element mit niedrigstem Wert setzen
					} else {																	// RR  ersetzen
						if(r.getLeft() != null) {
							r.getRight().setLeft(r.getRight());									// Linkes Element an vorgesehenem rechtem Element verknüpfen
						}
						r.replace(r.getRight());												// Element ohne aktuelles Element setzen
					}
				}  else {																		// Ohne Childs
					if(r == root){																
						root = null; 															
					}
					return null;									
				}
			}
			r.setHoehe(calCurHoehe(r));
			return r;
		}catch(Exception ex) {
			throw new Exception(ex);
		}
	}

	/**
	 * Gibt einen Baum mit PreOrder als String wieder
	 * param:	r: TElement	: Baum
	 * return:	 : String	: PreOrder Text
	 * Version 1
	 * Erstellt
	 */
	private static String PreOrder(TElement r) {
		ArrayList<String> preOrderList = new ArrayList<>();										// ArrayList um die Werte zum Schluss zusammenzufügen
		preOrderList.add(String.valueOf(r.getWert()));											// Wert des aktuellen Elements der ArrayList hinzufügen
		if (r.getLeft() != null) {																// PreOrder von linkem Element der ArrayList hinzufügen, falls vorhanden
			preOrderList.add(PreOrder(r.getLeft()));
		}
		if (r.getRight() != null) {																// PreOrder von rechtem Element der ArrayList hinzufügen, falls vorhanden
			preOrderList.add(PreOrder(r.getRight()));
		}
		return String.join(", ", preOrderList);													// ArrayList mit Komma zusammengefügt zurückgeben
	}
	
	/**
	 * Gibt einen Baum mit PostOrder als String wieder
	 * param:	r: TElement	: Baum
	 * return:	 : String	: PostOrder Text
	 * Version 1
	 * Erstellt
	 */
	private static String PostOrder(TElement r) {
		ArrayList<String> postOrderList = new ArrayList<>();									// ArrayList um die Werte zum Schluss zusammenzufügen
		if (r.getLeft() != null) {																// PostOrder von linkem Element der ArrayList hinzufügen, falls vorhanden
			postOrderList.add(PostOrder(r.getLeft()));
		}
		if (r.getRight() != null) {																// PostOrder von rechtem Element der ArrayList hinzufügen, falls vorhanden
			postOrderList.add(PostOrder(r.getRight()));
		}
		postOrderList.add(String.valueOf(r.getWert()));											// Wert des aktuellen Elements der ArrayList hinzufügen
		return String.join(", ", postOrderList);												// ArrayList mit Komma zusammengefügt zurückgeben
	}
	
	/**
	 * Gibt einen Baum mit InOrder als String wieder
	 * param:	r: TElement	: Baum
	 * return:	 : String	: InOrder Text
	 * Version 1
	 * Erstellt
	 */
	private static String InOrder(TElement r) {
		ArrayList<String> inOrderList = new ArrayList<>();										// ArrayList um die Werte zum Schluss zusammenzufügen
		if (r.getLeft() != null) {																// InOrder von linkem Element der ArrayList hinzufügen, falls vorhanden
			inOrderList.add(InOrder(r.getLeft()));
		}
		inOrderList.add(String.valueOf(r.getWert()));											// Wert des aktuellen Elements der ArrayList hinzufügen
		if (r.getRight() != null) {																// InOrder von rechtem Element der ArrayList hinzufügen, falls vorhanden
			inOrderList.add(InOrder(r.getRight()));
		}
		return String.join(", ", inOrderList);													// ArrayList mit Komma zusammengefügt zurückgeben
	}
	
	/**
	 * Zählt die maximale Tiefe  
	 * param:	r: TElement	: Baum
	 * return:	 : int		: Maximale Höhe
	 * Version 1
	 * Erstellt
	 */
	private static int calMaxHoehe(TElement r, int curHoehe) {
		int leftHoehe = 0;																		// 0 als standard Höhe Wert																		
		int rightHoehe = 0;
		if(r == null) {
			return 0;
		}
		if(r.getLeft() != null) {
			leftHoehe = calMaxHoehe(r.getLeft(), curHoehe + 1);									// Links mit einer Ebene erhöht fortfahren Ergebnis zwischenspeichern
		}
		if(r.getRight() != null) {
			rightHoehe = calMaxHoehe(r.getRight(), curHoehe + 1);								// Rechts mit einer Ebene erhöht fortfahren und Ergebnis zwischenspeichern
		}
		if(leftHoehe > curHoehe) {
			curHoehe = leftHoehe;																// Links als aktuelle Höhe setzen, wenn aktuelle Höhe kleiner ist
		}
		if(rightHoehe > curHoehe) {
			curHoehe = rightHoehe;																// Rechts als aktuelle Höhe setzen, wenn aktuelle Höhe kleiner ist
		} 
		return curHoehe;																		// Maximale Höhe zurückgeben
	}
	
	/**
	 * Errechnet die Differenz der Höhe links und rechts
	 * param:	r: 			TElement: Baum
	 * 			curHoehe:	int		: Aktuelle Höhe	
	 * return:	 : int				: Differenz der linken und rechten Höhe
	 * Version 1
	 * Erstellt
	 */
	private static int calCurHoehe(TElement r) {													
		return calCurHoehe(r, 0);																// 0 als standard Höhe Wert
	}
	private static int calCurHoehe(TElement r, int curHoehe) {
		int leftHoehe=0;																		
		int rightHoehe=0;
		if(r.getLeft()!= null) {
			leftHoehe = calMaxHoehe(r.getLeft(), curHoehe + 1);									// Links mit einer Ebene erhöht fortfahren Ergebnis zwischenspeichern
		}
		if(r.getRight()!= null) {
			rightHoehe = calMaxHoehe(r.getRight(), curHoehe + 1);								// Rechts mit einer Ebene erhöht fortfahren und Ergebnis zwischenspeichern
		}
		return leftHoehe-rightHoehe;															// Aktuelle Höhe zurückgeben
	}	
	
	/**
	 * Gibt kleinsten Wert im Baum zurück
	 * param:	r: TElement	: Baum
	 * return:	 : int		: Kleinster Wert von r aus
	 * Version 1
	 * Erstellt
	 */
	private static int smallestWert(TElement r){
        int smallW = r.getWert();																// Wert des aktuellen Elements als kleinsten Wert zwischenspeichern   
        while (r.getLeft() != null) {                                                           // Solange es ein Element mit kleinerem Wert gibt...                  
        	smallW = r.getLeft().getWert();                                                     // Kleineren Wert zwischenspeichern zur Rückgabe                      
            r = r.getLeft();                                                                    // Element mit kleinerem Wert als aktuelels Element zwischenspeichern 
        }                                                                                                                                                            
        return smallW;                                                                          // Kleinsten Wert ausgeben                                             
    }
	
	/**
	 * Gibt höchsten Wert im Baum zurück
	 * param:	r: TElement	: Baum
	 * return:	 : int		: Größter Wert von r aus
	 * Version 1
	 * Erstellt
	 */
	private static int biggestWert(TElement r){
        int biggestW = r.getWert();																// Wert des aktuellen Elements als höchsten Wert zwischenspeichern
        while (r.getRight() != null) {															// Solange es ein Element mit größerem Wert gibt...
        	biggestW = r.getRight().getWert();													// Größeren Wert zwischenspeichern zur Rückgabe
            r = r.getRight();																	// Element mit größerem Wert als aktuellen Element zwischenspeichern
        }
        return biggestW;																		// Größten Wert ausgeben
    }
	
	/**
	 * Gibt Baum ohne Element mit kleinstem Wert zurück
	 * param:	r: TElement	: Baum
	 * return:	 : TElement	: Baum ohne kleinstes Element von r aus
	 * Version 1
	 * Erstellt
	 */
	private static TElement removeSmallestWertElement(TElement r){
		TElement smallestWertElement = r;														// Aktuelles Element zwischenspeichern                                                
		TElement smallestWertParentElement = null;                                              // Vorgängerelements ist noch nicht vorhanden                                          
        while (smallestWertElement.getLeft() != null) {                                         // Solange es ein Element mit kleinerem Wert gibt...                                             
        	smallestWertParentElement = smallestWertElement;                                    // Aktuelles Element als Vorgängerelement zum Löschen des nächst kleinesten Werts zwischenspeichern
        	smallestWertElement = smallestWertElement.getLeft();                                // Neues Element mit kleinstem Wert zum Überprüfen zwischenspeichern                              
        }                                                                                                                                                                                       
        smallestWertParentElement.setLeft(null);                                                // Kleinsten Wert löschen                                                                         
        return r;                                                                               // Bearbeiteten Baum zurückgeben                                                                
    }
	
	/**
	 * Gibt Baum ohne Element mit höchstem Wert zurück
	 * param:	r: TElement	: Baum
	 * return:	 : TElement	: Baum ohne größtes Element von r aus
	 * Version 1
	 * Erstellt
	 */
	private static TElement removeBiggestWertElement(TElement r){
		TElement biggestWertElement = r;														// Aktuelles Element zwischenspeichern
		TElement biggestWertParentElement = null;												// Vorgängerelement ist noch nicht vorhanden
        while (biggestWertElement.getRight() != null) {											// Solange es ein Element mit größerem Wert gibt...
        	biggestWertParentElement = biggestWertElement;										// Aktuelles Element als Vorgängerelement zum Löschen des nächst größten Werts zwischenspeichern
        	biggestWertElement = biggestWertElement.getRight();									// Neues Element mit größtem Wert zum Überprüfen zwischenspeichern
        }
        biggestWertParentElement.setRight(null);												// Größten Wert löschen
        return r;																				// Bearbeiteten Baum zurückgeben
    }
	
	/**
	 * Unterklasse zur Darstellung des Baumes
	 * param:	r: TElement	: Baum
	 * Version 1
	 * Erstellt
	 */
	@SuppressWarnings("serial")
	public class TreePanel extends JPanel implements treeListener{
		private final Color STD_BACKGROUND = Color.WHITE;										// Konstanten für Farben
		private final Color STD_PAINT = Color.BLACK;
		private final int ELEMENT_WIDTH = 35;													// Konstanten für Größen
		private final int ELEMENT_HEIGHT = 30;
		private final int ELEMENT_GAP = 7;

		public TreePanel() {
			super();
		}
		
		public void paintComponent(Graphics g) {												// paintComponent überschreiben
			super.paintComponent(g);
			
			this.setBackground(STD_BACKGROUND);
			
			if(root != null) {
				drawTree(g);																	// Baum zeichnen
			}
		}
		
		
		/**
		 * Zeichnet Baum
		 * param:	g: 			Graphics: Baum
		 * 			curElement: TElement: aktuelles Element
		 * 			drawX: 		int		: X Kooirdinate
		 * 			drawY: 		int		: Y Koordinate
		 * 			layer: 		layer	: Aktuelle Ebene von oben aus gesehen ( 1 = Rootebene )
		 * Version 4
		 * Layer Parameter hinzugefügt. Annähernde Lösung zur rekursiven Bestimmung der nächsten x und y Werte gefunden
		 */
		private void drawTree(Graphics g) {
			g.setColor(STD_PAINT);																// Standard Farbe zum zeichnen setzen
			drawTree(g, root, this.getWidth() / 2, 0, 1);										// Mit Rootwert mittig anfangen zu zeichnen
		}
		private void drawTree(Graphics g, TElement curElement, int drawX, int drawY, int layer) {
				g.drawOval(drawX - ELEMENT_WIDTH / 2, drawY, ELEMENT_WIDTH, ELEMENT_HEIGHT);	// Kreis mittig unter drawX zeichnen
				String drawStr= String.valueOf(curElement.getWert());							// Zu zeichnender Wert 
				g.drawString(drawStr, drawX - g.getFontMetrics().stringWidth(drawStr) / 2, drawY + g.getFontMetrics().getHeight() + 4);// Wert zentriert im Kreis zeichnen
				if(curElement.getLeft() != null) {												// Links und rechts mit kleiner(-) als - und größer(+) als Wert des aktuellen Elements unterscheiden
					g.drawLine(drawX, drawY + ELEMENT_HEIGHT, drawX - this.getWidth() / (int)(Math.pow(2, layer + 1)), drawY + ELEMENT_HEIGHT + ELEMENT_GAP);// Linie von aktuellem zum nächsten Element Zeichnen
					drawTree(g, curElement.getLeft(), drawX - this.getWidth() / (int)(Math.pow(2, layer + 1)), drawY + ELEMENT_HEIGHT + ELEMENT_GAP, layer + 1);// Nächstes Element mit X auf die hälfte der Differenz zum vorherigen Element zeichnen
																								// Hälfte der Differenz berechnet aus panelwidth * 1/(2^layer)
				}
				if(curElement.getRight() != null) {
						g.drawLine(drawX, drawY + ELEMENT_HEIGHT, drawX + this.getWidth() / (int)(Math.pow(2, layer + 1)), drawY + ELEMENT_HEIGHT + ELEMENT_GAP);// Linie von aktuellem zum nächsten Element Zeichnen
						drawTree(g, curElement.getRight(), drawX + this.getWidth() / (int)(Math.pow(2, layer + 1)), drawY + ELEMENT_HEIGHT + ELEMENT_GAP, layer + 1);// Nächstes Element mit X auf das Produkt von 1,5 und der Differenz zum vorherigen Element zeichnen
																								// Hälfte der Differenz berechnet aus panelwidth * 1/(2^layer)
				}
		}
		
		@Override
		public void onTreeChanged() {
			this.paintComponent(this.getGraphics());									// Komponente neu zeichnen, wenn sich etwas im Baum ändert
		}
	}
}
