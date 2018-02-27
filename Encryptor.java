import java.util.*;
import java.io.*;
/**
 * Encryption
 * @Author: Danylo Mirin
 * @Date: 27 February 2018
 * @Version: 5
 * @Error Codes:
 * 1 = no writing or reading access
 * 
 * @Notes:
 * If you and your partner have different keys, one of you needs to send his to the other.
 * The conversion file wouldn't normally be safe - intercept it and you're done. Therefore,
 * a second, common algorithm encrypts the conversion file so that it is unreadable to humans
 * and by the time a machine finds a decryption for it, your message will already go through
 * and encryption can be changed again
 */
public class Encryptor {
    private HashMap<String, String> alphabet;
    private BufferedReader reader;
    private BufferedWriter writer;
    private File convFile, log;
    /*
     * If the conversion file is empty/nonexistent or after initialisation it is found that not all
     * characters are present, it will be written. This WILL screw with decryption on the other end!
     * You, as a responsible user, will have to pass on this table to someone
     */
    public Encryptor() {
        convFile = new File("l2n.enc");
        log = new File("log.txt");
        if(!convFile.canRead() && !convFile.canWrite())
            System.exit(1);
        else
            if(!convFile.exists()) {
                convFile.mkdirs();
                try {
                    if(!log.exists())
                        log.createNewFile();
                    convFile.createNewFile();
                    generateNewValues();
                    writeToFile();
                } catch(IOException ex) {
                    reportException(ex);
                    System.exit(2);
                }
            } else {
                try {
                    if(!log.exists())
                        log.createNewFile();
                    readFromFile();
                } catch(FileNotFoundException ex) {
                    
                } catch(IOException ex) {
                    
                }
            }
    }
    
    //This simply saves the computer the trouble of making up new values
    public Encryptor(HashMap<String, String> alphabet) {
        this.alphabet.putAll(alphabet);
        if(!convFile.exists()) {
            convFile.mkdirs();
            try {
                if(!log.exists())
                    log.createNewFile();
                convFile.createNewFile();
                writeToFile();
            } catch(IOException ex) {
                reportException(ex);
                System.exit(2);
            }
        } else {
            try {
                if(!log.exists())
                    log.createNewFile();
                readFromFile();
            } catch(FileNotFoundException ex) {
                
            } catch(IOException ex) {
                
            }
        }
    }
    
    private void generateNewValues() {
        String[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()-_=+{}[]|;:'\",.<>/?".split("");
        for(String s : chars) {
            String value = keyGen();
            alphabet.put(s, value);
        }
    }
    public String keyGen() {//In the EXTREMELY improbable event that keys are the same, just rerun the program. If that doesn't fix it...
        String[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".split("");
        String key = "";
        for(int i = 0; i < 10; i++) {
            int rand = (int)(Math.random()*chars.length);
            key += chars[rand];
        }
        return key;
    }
    
    private void readFromFile() throws FileNotFoundException, IOException {
        reader = new BufferedReader(new FileReader(convFile));
        String line;
        while( (line = reader.readLine()) != null)
            alphabet.put(line.substring(0,1), line.substring(2));
        reader.close();
    }
    
    private void writeToFile() throws IOException {
        writer = new BufferedWriter(new FileWriter(convFile));
        for(String k : alphabet.keySet())
            writer.write(k+"|"+alphabet.get(k));
        writer.flush();
        writer.close();
    }
    
    private void reportException(IOException ex) {
        
    }
}