import java.util.*;
import java.io.*;
/**
 * Encryption
 * @Author: Danylo Mirin
 * @Copyright 2018
 * @Date: 27 February 2018
 * @Version: 5
 * @Error Codes:
 * 1 = no writing or reading access
 * 2 = IOException - writing fail
 * 3 = File Not Found - file(s) were removed midway through operation
 * 4 = IOException - reading fail
 * 5 = when message was copied, it was not copied in full. Decryption
 * impossible.
 * 
 * @Notes:
 * If you and your partner have different keys, one of you needs to send his to the other.
 * The conversion file wouldn't normally be safe - intercept it and you're done. Therefore,
 * a second, common algorithm encrypts the conversion file so that it is unreadable to humans
 * and by the time a machine finds a decryption for it, your message will already go through
 * and encryption can be changed again
 * If the conversion file is empty/nonexistent or after initialisation it is found that not all
 * characters are present, it will be rewritten. This WILL screw with decryption on the other
 * end! You will have to pass on the key to your partner or decryption will fail and give you 
 * gibberish answers. (encryption stored in l2n.enc)
 */
public class Encryptor {
    private HashMap<String, String> alphabet = new HashMap<>();
    private BufferedReader reader;
    private BufferedWriter writer;
    private File convFile = new File("l2n.enc");
    private File log = new File("log.txt");
    public final ErrorLogger el = new ErrorLogger();
    public Encryptor() {
        if(!convFile.exists()) {
            try {
                if(!log.exists())
                    log.createNewFile();
                convFile.createNewFile();
            } catch(IOException ex) {
                el.add(ex,1);
            }
        } else {
            try {
                if(!log.exists())
                    log.createNewFile();
                readFromFile();
            } catch(FileNotFoundException ex) {
                //this should already be taken care of by the "if" part of this statement but just in case...
                el.add(ex, 3);
            } catch(IOException ex) {
                el.add(ex, 2);
            }
        }
    }
    
    //This constructor simply saves the computer the trouble of making up new values, everything else remains the same
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
                el.add(ex, 1);
                System.exit(2);
            }
        } else {
            try {
                if(!log.exists())
                    log.createNewFile();
                readFromFile();
            } catch(FileNotFoundException ex) {
                el.add(ex, 3);
                System.exit(3);
            } catch(IOException ex) {
                el.add(ex, 2);
                System.exit(4);
            }
        }
    }
    
    public static void main() {
        System.out.println(Encryptor.ln("740403184938595",false));
    }
    
    /*#Encryption*/
    public String encrypt(String message) {
        String scrambled = "";
        for(int i = 0; i < message.length(); i++)
            scrambled += alphabet.get(message.substring(i,i+1));
        return scrambled;
    }
    public void generateNewValues() {
        String[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()-_=+{}[]|;:'\",.<>/?".split("");
        for(String s : chars) {
            String value = keyGen();//generates a random 10-character key for encryption
            System.out.println(s+", "+value);
            alphabet.put(s, value);
        }
        try{
            writeToFile();
        } catch(IOException ex) {
            el.add(ex, 1);
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
    
    public String decrypt(String scrambled) {
        String message = "";
        //should always be false because every encryption value is 10 chars long
        if(scrambled.length() % 10 != 0)
            System.exit(5);
        for(int i = 0; i < scrambled.length(); i+= 10) {
            String expression = scrambled.substring(i,i+10);
            for(String s : alphabet.keySet())
                if(alphabet.get(s).equals(expression))
                    message += s;
        }
        return message;
    }
    
    private void readFromFile() throws FileNotFoundException, IOException {
        reader = new BufferedReader(new FileReader(convFile));
        String line;
        while( (line = reader.readLine()) != null) {
            line = ln(line,false);
            alphabet.put(line.substring(0,1), line.substring(2));
        }
        reader.close();
    }
    
    private void writeToFile() throws IOException {
        writer = new BufferedWriter(new FileWriter(convFile));
        for(String k : alphabet.keySet()) {//This segment works flawlessly
            String s = k+"|"+alphabet.get(k);
            writer.write(ln(s,true)+"\n");
        }
        writer.flush();
        writer.close();
    }
    /**This method is gonna be ugly because I do not want to rely on external files that can be cracked*/
    public static String ln(String line, boolean encrypt) {
        String answer = "";
        HashMap<String, Integer> map = new HashMap<>();
        map.put("A",116);
        map.put("B",404);
        map.put("C",754);
        map.put("D",891);
        map.put("E",534);
        map.put("F",713);
        map.put("G",482);
        map.put("H",996);
        map.put("I",309);
        map.put("J",414);
        map.put("K",745);
        map.put("L",296);
        map.put("M",824);
        map.put("N",678);
        map.put("O",732);
        map.put("P",541);
        map.put("Q",563);
        map.put("R",527);
        map.put("S",926);
        map.put("T",830);
        map.put("U",755);
        map.put("V",130);
        map.put("W",508);
        map.put("X",153);
        map.put("Y",627);
        map.put("Z",235);
        map.put("a",538);
        map.put("b",322);
        map.put("c",184);
        map.put("d",969);
        map.put("e",276);
        map.put("f",740);
        map.put("g",120);
        map.put("h",436);
        map.put("i",136);
        map.put("j",394);
        map.put("k",938);
        map.put("l",277);
        map.put("m",483);
        map.put("n",593);
        map.put("o",942);
        map.put("p",388);
        map.put("q",627);
        map.put("r",989);
        map.put("s",604);
        map.put("t",773);
        map.put("u",403);
        map.put("v",357);
        map.put("w",195);
        map.put("x",226);
        map.put("y",164);
        map.put("z",271);
        map.put("1",562);
        map.put("2",520);
        map.put("3",905);
        map.put("4",258);
        map.put("5",650);
        map.put("6",349);
        map.put("7",444);
        map.put("8",471);
        map.put("9",962);
        map.put("0",550);
        map.put("~",812);
        map.put("!",595);
        map.put("@",346);
        map.put("#",895);
        map.put("$",925);
        map.put("%",690);
        map.put("^",585);
        map.put("&",875);
        map.put("*",984);
        map.put("(",606);
        map.put(")",502);
        map.put("-",258);
        map.put("_",984);
        map.put("=",183);
        map.put("+",835);
        map.put("{",624);
        map.put("}",838);
        map.put("[",862);
        map.put("]",962);
        map.put("|",813);
        map.put(";",474);
        map.put(":",778);
        map.put("'",762);
        map.put("\"",215);//had to escape the "
        map.put(",",113);
        map.put(".",232);
        map.put("<",341);
        map.put(">",560);
        map.put("/",951);
        map.put("?",922);

        if(encrypt)
            for(int i = 0; i < line.length(); i++)
                answer += map.get(line.substring(i,i+1));
        else
            for(int i = 0; i < line.length(); i+=3)
                for(String key : map.keySet())
                    if(map.get(key).equals(Integer.parseInt(line.substring(i,i+3))))
                        answer += key;
        
        return answer;
    }
    
    public boolean empty() {
        return alphabet.isEmpty();
    }
}