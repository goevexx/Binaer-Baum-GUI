import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;

import Baum.TElement;
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

public class Main {
	private static final int FEHLER = 0;														
	private static final int OK = 1;															// Konstanten für den Status der Funktion insertElement() zum Einfügen von Elementen
	private static final int VORHANDEN = 2;
	private static final int GELOESCHT = 1;														// Konstanten für den Status der Funktion deleteElement() zum Einfügen von Elementen
	private static final int NICHT_GEFUNDEN = 2;
	private static final int ALL = 0;															// Konstanten für den Status der Funktion updateHoehe() zum Einfügen von Elementen
	private static final int LEFT = 1;
	private static final int RIGHT = 2;

	private JFrame frmBinrerBaum;
	private JTextField addTxtF;
	private JTextField orderTxtF;
	private JLabel statusMessage;
	
	private static TElement root;

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
		frmBinrerBaum.setBounds(100, 100, 683, 482);
		frmBinrerBaum.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBinrerBaum.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel.setBounds(10, 11, 657, 61);
		frmBinrerBaum.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("Wert");
		label.setBounds(10, 11, 94, 14);
		panel.add(label);
		
		JButton addBtn = new JButton("hinzuf\u00FCgen");
		@SuppressWarnings("serial")
		Action addBtnAction = new AbstractAction() {											// Erstellen einer Action, um sie sowohl für den addBtn als auch für addTxtF zu setzen
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int insertWert = Integer.parseInt(addTxtF.getText());						// Eingefügten Text zu Integer konvertieren	
					if(root == null) {															// Root setzen, falls nicht vorhanden
						root = new TElement();
						root.setWert(insertWert);
						statusMessage.setText("Root mit Wert " + insertWert + " gesetzt");
					} else {																	
							TElement insertElement = new TElement();							// Element setzen und einfügen
							insertElement.setWert(insertWert);				
							switch (insertElement(insertElement)) {
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
					int searchWert = Integer.parseInt(addTxtF.getText());						// Gesuchten Text zu Integer konvertieren	
					if(FindElement(searchWert)==null)
						statusMessage.setText("Element mit dem Wert " + searchWert + " ist nicht im Baum vorhanden");
					else
						statusMessage.setText("Element mit dem Wert " + searchWert + " ist im Baum vorhanden");
				}catch (NumberFormatException ex){												// Fange, wenn gesuchter Text kein Integer ist
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Ungültige Eingabe");
				}
			}
		});
		searchBtn.setBounds(230, 26, 82, 23);
		panel.add(searchBtn);
		
		JButton deleteBtn = new JButton("l\u00F6schen");
		deleteBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int deleteWert = Integer.parseInt(addTxtF.getText());						// Zu löschenden Text zu Integer konvertieren	
					switch(deleteElement(deleteWert)) {
					case FEHLER:
						statusMessage.setText("Beim Löschen ist ein Fehler aufgetreten");
						break;
					case OK:
						statusMessage.setText("Element mit Wert " + deleteWert + " gelöscht");
						break;
					case VORHANDEN:
						statusMessage.setText("Element mit Wert " + deleteWert + " nicht gefunden");
						break;
					}
				} catch (NullPointerException ex) {												// Fange wenn FindElement NullPointerException wirft
					System.err.println(ex);														// Gebe Fehler in Konsole und auf dem Bildschirm aus
					JOptionPane.showMessageDialog(frmBinrerBaum, "Das Root Element ist nicht gesetzt");
				}
			}
		});
		deleteBtn.setBounds(323, 26, 94, 23);
		panel.add(deleteBtn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(UIManager.getBorder("TitledBorder.border"));
		panel_1.setBounds(10, 376, 657, 43);
		frmBinrerBaum.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JButton postOrderBtn = new JButton("PostOrder");
		postOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(PostOrder(root));
				} catch (NullPointerException ex) {
					System.err.println(ex);
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");
				}
			}
		});
		postOrderBtn.setBounds(210, 11, 95, 23);
		panel_1.add(postOrderBtn);
		
		JButton inOrderBtn = new JButton("InOrder");
		inOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(InOrder(root));
				} catch (NullPointerException ex) {
					System.err.println(ex);
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");
				}
			}
		});
		inOrderBtn.setBounds(115, 11, 85, 23);
		panel_1.add(inOrderBtn);
		
		JButton preOrderBtn = new JButton("PreOrder");
		preOrderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					orderTxtF.setText(PreOrder(root));
				} catch (NullPointerException ex) {
					System.err.println(ex);
					JOptionPane.showMessageDialog(frmBinrerBaum, "Root is nicht gesetzt");
				}
			}
		});
		preOrderBtn.setBounds(10, 11, 95, 23);
		panel_1.add(preOrderBtn);
		
		orderTxtF = new JTextField();
		orderTxtF.setBounds(315, 12, 332, 20);
		panel_1.add(orderTxtF);
		orderTxtF.setEditable(false);
		orderTxtF.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel_2.setBounds(0, 430, 677, 23);
		frmBinrerBaum.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		statusMessage = new JLabel("Lege den Wert des Root Elements fest!");
		statusMessage.setBounds(5, 4, 652, 14);
		panel_2.add(statusMessage);
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
	 * Noch nicht durchdacht
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
			return null;																		// Werfe falls r nicht gesetzt ist
		} catch (Exception ex) {
			System.err.println(ex);																// Fehler in der Konsole ausgeben
			return null;
		}
	}
	
	/**
	 * Löscht ein Element nach eingegebenem Wert 
	 * param:	r	: TElement	: Baum
	 * 			wert: int		: Wert
	 * return:	 	: int		: 0: Fehler, 1: Gelöscht, 2: Nicht gefunden
	 * Version 1
	 * Noch nicht durchdacht
	 */
	private static int deleteElement(int wert) {
		try {
			TElement deleteElement = FindElement(wert);											// Zu löschendes Element suchen
			TElement replaceElement = null;														// Mit zu löschendem Element zu ersetzendes Element
			TElement parentReplaceElement = null;												// Vorgänger des ersetzenden Elements
			if(deleteElement != null) {
				if(deleteElement.getLeft() != null) {
					parentReplaceElement = deleteElement;
					replaceElement = deleteElement.getLeft();
					while (replaceElement.getRight() != null) {
						parentReplaceElement = replaceElement;
						replaceElement = replaceElement.getRight();
					}
					deleteElement.setWert(replaceElement.getWert());
					if(parentReplaceElement != deleteElement) {
						parentReplaceElement.setRight(replaceElement.getLeft());
					} else {
						parentReplaceElement.setLeft(replaceElement.getLeft());
					}
				} else if(deleteElement.getRight() != null) {
					parentReplaceElement = deleteElement;
					replaceElement = deleteElement.getRight();
					while (replaceElement.getLeft() != null) {
						parentReplaceElement = replaceElement;
						replaceElement = replaceElement.getLeft();
					}
					deleteElement.setWert(replaceElement.getWert());
					if(parentReplaceElement != deleteElement) {
						parentReplaceElement.setLeft(replaceElement.getRight());
					} else {
						parentReplaceElement.setRight(replaceElement.getRight());
					}
				} else if(root ==deleteElement) {
					root = null;
				}
				if(root != null && deleteElement != null) {
					if(deleteElement.getWert() < root.getWert()) {
						updateHoehe(LEFT);
					} else if(deleteElement.getWert() > root.getWert()){
						updateHoehe(RIGHT);
					}
				}
				return GELOESCHT;
			} else {
				return NICHT_GEFUNDEN;
			}
		} catch (Exception ex) {
			return FEHLER;
		}
	}

	/**
	 * Gibt einen Baum mit PreOrder als String wieder
	 * param:	r: TElement	: Baum
	 * return:	 : String	: PreOrder
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
	 * return:	 : String	: PostOrder
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
	 * return:	 : String	: InOrder
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
	 * Zählt das Maximum der am Baum hängenden Ebenen
	 * param:	r: TElement	: Baum
	 * Version 1
	 * Erstellt
	 */
	private static int calMaxHoehe(TElement r, int curHoehe) {
		int leftHoehe=0;																		// 0 als standard Höhe Wert
		int rightHoehe=0;
		if(r.getLeft()!= null) {
			leftHoehe = calMaxHoehe(r.getLeft(), curHoehe + 1);									// Links mit einer Ebene erhöht fortfahren Ergebnis zwischenspeichern
		}
		if(r.getRight()!= null) {
			rightHoehe = calMaxHoehe(r.getRight(), curHoehe + 1);								// Rechts mit einer Ebene erhöht fortfahren und Ergebnis zwischenspeichern
		}
		if(leftHoehe>curHoehe) {
			curHoehe=leftHoehe;																	// Links als aktuelle Höhe setzen, wenn aktuelle Höhe kleiner ist
		}
		if(rightHoehe>curHoehe) {
			curHoehe=rightHoehe;																// Rechts als aktuelle Höhe setzen, wenn aktuelle Höhe kleiner ist
		} 
		return curHoehe;																		// Maximale Höhe zurückgeben
	}
	
	/**
	 * Zählt die aktuelle Höhe der am Baum hängenden Elemente und des baum Elements
	 * param:	r: TElement	: Baum
	 * Version 1
	 * Erstellt
	 */
	private static int calCurHoehe(TElement r) {													
		return calCurHoehe(r, 0);																
	}
	private static int calCurHoehe(TElement r, int curHoehe) {
		int leftHoehe=0;																		// 0 als standard Höhe Wert
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
	 * Aktualisiert die Höhen der Elemente
	 * param:	r: TElement	: Baum
	 * Version 1
	 * Funktioniert noch nicht
	 */
	private static void updateHoehe(int mode) {
		updateHoehe(root, mode);
	}
	private static void updateHoehe(TElement r, int mode) {							
		switch(mode) {
		case ALL:
			if(r.getLeft() != null) {
				updateHoehe(r, LEFT);
			}
			if(r.getRight() != null) {
				updateHoehe(r, RIGHT);
			}
			break;
		case LEFT:
			r.setHoehe(calCurHoehe(r));
			if(r.getLeft() != null) {
				r=r.getLeft();
			} else {
				return;
			}
			break;
		case RIGHT:
			r.setHoehe(calCurHoehe(r));
			if(r.getRight() != null) {
				r=r.getRight();
			} else {
				return;
			}
			break;
		}
		System.out.println(r.getHoehe());
		updateHoehe(r, ALL);
	}
}
