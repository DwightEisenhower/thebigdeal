import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
/**
 * Time to screw around.
 */
public class Menu {
    private static JFrame program = new JFrame("Encryptor");
    public static void main() {
        JPanel one = new JPanel();
        JPanel two = new JPanel();
        Button a = new Button("Option A");
        Button b = new Button("Option B");
        a.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new JFrame(), "BOO!");
                JOptionPane.showConfirmDialog(new JFrame(), "Join the Student Party?");
            }
        });
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String in = JOptionPane.showInputDialog(new JFrame(), "Gimme something");
                System.out.println(in);
            }
        });
        CardLayout layout = new CardLayout();
        program.setLayout(layout);
        program.setLocation(600,600);
        program.setSize(200,50);
        program.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        program.add(a);
        program.add(b);
        program.show();
    }
}