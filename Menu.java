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
    
    private JFrame frame = new JFrame("Encryptor");
    private JPanel cards;
    private final String BUTTONPANEL = "Encryption";
    private final String TEXTPANEL = "PasswordManager";
    
    private JButton encrypt = new JButton("Encrypt");
    private JTextField encinput = new JTextField("message...", 25);
    private JButton decrypt = new JButton("Decrypt");
    private JButton b = new JButton("Generate");
    
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
            }
        });
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                enc.generateNewValues();
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
        card2.add(new JTextField("TextField", 20));
        card2.add(new JButton("Go"));
        
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
        JFrame frame = new JFrame("CardLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Menu demo = new Menu();
        demo.addComponentToPane(frame.getContentPane());
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
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
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        run();
    }
}