import java.io.*;
import java.util.*;
/**
 * Password Manager by Danylo Mirin
 * @Author Danylo Mirin
 * @Copyright 2018
 * @Date 4/3/18 (d/m/y)
 * @Version 1
 */
public class PasswordManager {
    //There are 3 bits to hold: website/company, username, password.
    //Cannot do that conveniently with just a hashmap
    private HashMap<String, Account> map;
    private File list = new File("list.pw");
    private ErrorLogger el = new ErrorLogger();
    public PasswordManager() {
        map = new HashMap<>();
        try {
            if(!list.exists())
                list.createNewFile();//may have issues b/c of writing access
            else {
                readFromFile();
            }
        } catch(FileNotFoundException ex) {
            el.add(ex, 3);
        } catch(IOException ex) {
            el.add(ex, 1);
        }
    }
    
    public static void main() {
        PasswordManager pw = new PasswordManager();
        pw.run();
    }
    
    public void run() {
        System.out.println(generatePassword(true));
    }
    
    public boolean addAccount(String site, String name, String password) {
        return map.putIfAbsent(site, new Account(name,password)) == null;
    }
    
    public Account getCredentials(String website) {
        return map.get(website);
    }
    
    public String generatePassword(boolean special) {
        String[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_+-=[]{}|;:,.<>?/'".split("");
        int upperBound; String pw = "";
        if(special)
            upperBound = chars.length;
        else
            upperBound = 26+26+10;
        for(int i = 0; i < 12; i++)
            pw += chars[(int)(Math.random()*upperBound)];
        return pw;
    }
    
    public void readFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(list));
            String line;
            while( (line = reader.readLine()) != null) {
                //line = Encryptor.ln(line,false);
                int arrow = line.indexOf("-->");
                int semcol = line.indexOf("; ");
                map.put(line.substring(0, arrow-1), new Account(line.substring(arrow+4, semcol), line.substring(semcol+2)));
            }
        } catch(FileNotFoundException ex) {
            el.add(ex, 3);
        } catch(IOException x) {
            el.add(x, 2);
        }
    }
    
    public boolean writeToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(list));
            for(String s : map.keySet()) {
                String line = s+" --> "+map.get(s);
                System.out.println(line);
                writer.write(line+"\n");
            }
            writer.flush();
            writer.close();
            System.out.println("Success, terminated");
            return true;
        } catch(FileNotFoundException ex) {
            el.add(ex, 3);
        } catch(IOException ex) {
            el.add(ex, 1);
        }
        return false;
    }
    public class Account {
        public String name, password;
        public Account(String name, String password) {
            this.name = name;
            this.password = password;
        }
        
        public String toString() {
            return name+"; "+password;
        }
    }
}