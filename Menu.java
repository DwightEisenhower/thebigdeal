import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Time to screw around.
 * 
 * Certain segments borrowed from CardLayoutDemo by Oracle
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/layout/CardLayoutDemoProject/src/layout/CardLayoutDemo.java
 */
public class Menu implements ItemListener {
    private Encryptor enc = new Encryptor();
    private PasswordManager pw = new PasswordManager();
    public final ErrorLogger el = new ErrorLogger();
    
    private JPanel cards;
    private final String BUTTONPANEL = "Encryption";
    private final String TEXTPANEL = "PasswordManager";
    
    private JButton encrypt = new JButton("Encrypt");
    private JTextField encinput = new JTextField("message...", 25);
    private JButton decrypt = new JButton("Decrypt");
    private JButton b = new JButton("Generate");
    
    private JButton add = new JButton("Add account");
    private JTextField pwinput = new JTextField("",20);
    private JButton search = new JButton("Search");
    
    private void addActionListeners() {
        encrypt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String in = encinput.getText();
                if(in.equals("message...") || in.equals(""))
                    JOptionPane.showMessageDialog(new JFrame(),"Enter something first!");
                else {
                    if(!enc.empty())
                        JOptionPane.showMessageDialog(new JFrame(), enc.encrypt(in));
                    else
                        JOptionPane.showMessageDialog(new JFrame(), "No conversion file found. If you are supposed to receive a file from a friend,\nclose the program, add the file to the folder with the program and\nrestart.\n\nIf you are not receiving a file for this from a friend, click generate and proceed.");
                }
            }
        });
        decrypt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String in = encinput.getText();
                if(in.equals("message...") || in.equals(""))
                    JOptionPane.showMessageDialog(new JFrame(),"Enter something first!");
                else {
                    if(!enc.empty())
                        JOptionPane.showMessageDialog(new JFrame(), enc.decrypt(in));
                    else
                        JOptionPane.showMessageDialog(new JFrame(), "No conversion file found. If you are supposed to receive a file from a friend,\nclose the program, add the file to the folder with the program and\nrestart.\n\nIf you are not receiving a file for this from a friend, click generate and proceed.");
                }
            }
        });
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                enc.generateNewValues();
                JOptionPane.showMessageDialog(new JFrame(), "Values generated.");
            }
        });
        add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                //make a new frame with 3 fields
            }
        });
        search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String input = pwinput.getText();
                JOptionPane.showMessageDialog(new JFrame(), pw.getCredentials(input));
            }
        });
    }
    
    public void addComponentToPane(Container pane) {
        JPanel comboBoxPane = new JPanel();
        String comboBoxItems[] = { BUTTONPANEL, TEXTPANEL };
        JComboBox box = new JComboBox(comboBoxItems);
        box.setEditable(false);
        box.addItemListener(this);
        comboBoxPane.add(box);
        
        JPanel card1 = new JPanel();
        card1.add(encrypt);
        card1.add(encinput);
        card1.add(decrypt);
        card1.add(b);
        
        JPanel card2 = new JPanel();
        card2.add(pwinput);
        card2.add(add);
        card2.add(search);
        
        addActionListeners();
        
        cards = new JPanel(new CardLayout());
        cards.add(card1, BUTTONPANEL);
        cards.add(card2, TEXTPANEL);
        
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
    }
    
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
    
    private static void run() {
        JFrame program = new JFrame("CardLayoutDemo");
        Menu menu = new Menu();
        program.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                menu.el.log();
            }
        });
        program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.addComponentToPane(program.getContentPane());
        program.pack();
        program.setVisible(true);
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        run();
    }
}