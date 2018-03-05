import java.io.*;
import java.util.*;
/**
 * Password Manager by Danylo Mirin
 * @Author Danylo Mirin
 * @Copyright 2018
 * @Date 4/3/18 (d/m/y)
 * @Version 1
 * @Error codes:
 * 1 = 
 */
public class PasswordManager {
    //There are 3 bits to hold: website/company, username, password.
    //Cannot do that conveniently with just a hashmap
    private HashMap<String, Account> map;
    private File list = new File("list.pw");
    public PasswordManager() {
        map = new HashMap<>();
        try {
            if(!list.exists())
                list.createNewFile();
            else {
                readFromFile();
            }
        } catch(FileNotFoundException ex) {
            ex.printStackTrace();
        } catch(IOException ex) {
            
        }
    }
    
    public boolean addAccount(String site, String name, String password) {
        Account temp = map.putIfAbsent(site, new Account(name,password));
        if(temp == null)
            return true;
        else
            return false;
    }
    
    public void readFromFile() throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(list));
    }
    
    public boolean writeToFile() {
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