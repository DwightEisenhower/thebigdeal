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
    private PasswordManager manager = new PasswordManager();
    public static final ErrorLogger el = new ErrorLogger();
    
    private JTextField website = new JTextField("Website", 10);
    private JTextField username = new JTextField("Username", 10);
    private JTextField password = new JTextField("Password",10);
    
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
    
    public static void main() {
        JFrame program = new JFrame("CardLayoutDemo");
        Menu menu = new Menu();
        program.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                menu.el.log();
                menu.manager.writeToFile();
            }
        });
        program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.addComponents(program.getContentPane());
        program.pack();
        program.setVisible(true);
    }
    
    private void addComponents(Container pane) {
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
    
    /**Buttons in both cards*/
    private void addActionListeners() {
        encrypt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String in = encinput.getText();
                if(in.equals("message...") || in.equals(""))
                    JOptionPane.showMessageDialog(new JFrame(),"Enter something first!");
                else {
                    if(!enc.empty()) {
                        JTextArea wat = new JTextArea(enc.encrypt(in));
                        JOptionPane.showMessageDialog(new JFrame(), wat);
                    } else
                        JOptionPane.showMessageDialog(new JFrame(),
                        "No conversion file found. If you are supposed to receive a file from a friend,\nclose the program, add the file to the folder with the program and\nrestart.\n\nIf you are not receiving a file for this from a friend, click generate and proceed.");
                }
            }
        });
        decrypt.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String in = encinput.getText();
                if(in.equals("message...") || in.equals(""))
                    JOptionPane.showMessageDialog(new JFrame(),"Enter something first!");
                else {
                    if(!enc.empty()) {
                        JTextArea wat = new JTextArea(enc.decrypt(in));
                        JOptionPane.showMessageDialog(new JFrame(), wat);
                    } else
                        JOptionPane.showMessageDialog(new JFrame(),
                        "No conversion file found. If you are supposed to receive a file from a friend,\nclose the program, add the file to the folder with the program and\nrestart.\n\nIf you are not receiving a file for this from a friend, click generate and proceed.");
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
                createAndShow2ndGUI();//inside is all the code to add a new account
            }
        });
        search.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String input = pwinput.getText();
                JTextArea wat = new JTextArea(manager.getCredentials(input).toString());
                JOptionPane.showMessageDialog(new JFrame(), wat);
            }
        });
    }
    
    private void createAndShow2ndGUI() {
        JFrame frame = new JFrame("Account Creation");
        JPanel panel = new JPanel();
        JButton generate = new JButton("Generate password");
        JButton save = new JButton("Save");
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                manager.addAccount(website.getText(), username.getText(), password.getText());
                JOptionPane.showMessageDialog(new JFrame(), "Saved!");
            }
        });
        generate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int s = JOptionPane.showConfirmDialog(new JFrame(),"Special characters?","",JOptionPane.YES_NO_OPTION);
                boolean special = false;
                if(s == 0)
                    special = true;
                password.setText(manager.generatePassword(special));
            }
        });
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        website.setAlignmentX(Component.CENTER_ALIGNMENT);
        username.setAlignmentX(Component.CENTER_ALIGNMENT);
        password.setAlignmentX(Component.CENTER_ALIGNMENT);
        generate.setAlignmentX(Component.CENTER_ALIGNMENT);
        save.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel comboBoxPane = new JPanel();
        String comboBoxItems[] = { TEXTPANEL };
        JComboBox box = new JComboBox(comboBoxItems);
        box.setEditable(false);
        box.addItemListener(this);
        comboBoxPane.add(box);
        
        panel.add(website);
        panel.add(username);
        panel.add(password);
        panel.add(generate);
        panel.add(save);
        
        frame.setSize(200,200);
        frame.getContentPane().add(comboBoxPane, BorderLayout.PAGE_START);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
    }
}