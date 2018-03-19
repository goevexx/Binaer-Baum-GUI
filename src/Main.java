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
import javax.swing.JRootPane;
import javax.swing.UIManager;

import org.apache.commons.lang3.math.NumberUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.BevelBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class Main {

	private JFrame frmBinrerBaum;
	private JTextField addTxtF;
	private JTextField orderTxtF;
	private JLabel statusMessage;
	
	private TElement root;

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
		Action addBtnAction = new AbstractAction() {											// Erstellen einer Action, um sie sowohl für den addBtn und addTxtF zu setzen
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int insertWert = NumberUtils.createInteger(addTxtF.getText());				// Eingefügten Text zu Integer konvertieren						
					if(root == null) {															// Root setzen, falls nicht vorhanden
						root = new TElement();
						root.setWert(insertWert);
						statusMessage.setText("Root mit Wert " + insertWert + " gesetzt");
					} else {																	
							TElement insertElement = new TElement();							// Element setzen und einfügen
							insertElement.setWert(insertWert);				
							insertElement(root, insertElement);
							statusMessage.setText("Element mit Wert " + insertWert + " eingefügt");
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
		addTxtF.setToolTipText("Eingabe muss eine ganze Zahl sein");
		addTxtF.setColumns(10);
		addTxtF.addActionListener(addBtnAction);
		addTxtF.setBounds(10, 27, 100, 20);
		panel.add(addTxtF);
		
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
	 * param:	r: TElement	: Baum
	 * 			e: TElement	: NeuesElement
	 * return:	 : int		: 0: Fehler, 1: OK, 2: Element schon im Baum
	 * Version 1
	 * Höhe noch nicht richtig gesetzt
	 */
	private static int insertElement(TElement r, TElement e) {
		try {
			if (r.getWert() == e.getWert())	{													// Überprüfen, ob die Werte des Root und des Einzufügenden Elements gleich sind
				return 2;																		
			} else if(e.getWert() < r.getWert()) {												// Links rekursiv einfügen, wenn der Wert des einzufügenden Elements kleiner als des Root Elements ist und
				if(r.getLeft() != null) {														// ein linkes Element gesetzt ist
					insertElement(r.getLeft(), e);
				} else {
					r.setLeft(e);																// Andernfalls das einzufügende Element links im Root Element einsetzen
					if(r.getRight() == null) {													// Erhöhe Hoehe von Root um 1, wenn rechts nicht gesetzt ist
						r.setHoehe(r.getHoehe()+1);
					}
				}
			} else if(e.getWert() > r.getWert()) {												// Rechts rekursiv einfügen, wenn der Wert des einzufügenden Elements größer als des Root Elements ist und
				if(r.getRight() != null) {														// ein rechtes Element gesetzt ist
					insertElement(r.getRight(), e);
				} else {
					r.setRight(e);																// Andernfalls das einzufügende Element links im Root Element einsetzen
					if(r.getLeft() == null) {													// Erhöhe Hoehe von Root um 1, wenn links nicht gesetzt ist
						r.setHoehe(r.getHoehe() + 1);
					}
				}
			}
			return 1;
		} catch (Exception ex) {
			System.err.println(ex);
			return 0;
		}
	}
	
	/**
	 * Findet ein Element mit bestimmten Wert
	 * param:	r	: TElement	: Baum
	 * 			wert: int		: Wert
	 * return:	 	: int		: 0: Fehler, 1: Gefunden, 2: Nicht gefunden
	 * Version 1
	 * Erstellt
	 */
	private static int FindElement(TElement r, int wert) {
		try {
			return 0;
		} catch (Exception ex) {
			System.err.println(ex);
			return 0;
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
}
