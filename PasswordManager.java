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
        
    }
    
    public boolean addAccount(String site, String name, String password) {
        Account temp = map.putIfAbsent(site, new Account(name,password));
        return temp == null;
    }
    
    public Account getCredentials(String website) {
        
    }
    
    public void readFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(list));
            String line;
            while( (line = reader.readLine()) != null) {
                line = Encryptor.ln(line,false);
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
                String line = s+" - "+map.get(s);
                writer.write(line);
            }
            writer.flush();
            writer.close();
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
    }
}