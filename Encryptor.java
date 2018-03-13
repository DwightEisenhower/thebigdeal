import java.util.*;
import java.io.*;
import java.util.regex.*;
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
        //if(!convFile.canRead() && !convFile.canWrite()) {
            //System.out.println("Cannot read or write");
            //System.exit(1);
        //} else {
            if(!convFile.exists()) {
                try {
                    if(!log.exists())
                        log.createNewFile();
                    convFile.createNewFile();
                    generateNewValues();
                    writeToFile();
                    System.out.println("File created, values generated and written.");
                } catch(IOException ex) {
                    el.add(ex,1);
                    System.out.println("The program has been unable to set up vital files.\nDo you wish to continue with a broken program or restart?\n(The probable cause of error is lack of writing/reading access)");
                }
            } else {
                try {
                    if(!log.exists())
                        log.createNewFile();
                    readFromFile();
                    System.out.println("Values read from file and set up.");
                } catch(FileNotFoundException ex) {
                    //this should already be taken care of by the "if" part of this statement but just in case...
                    el.add(ex, 3);
                    System.exit(3);
                } catch(IOException ex) {
                    el.add(ex, 2);
                    System.exit(4);
                }
            }
        //}
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
    
    public void run() {
        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        boolean end = false;
        System.out.println("Input reading started.");
        while(!end) {
            try {
                String input = console.readLine();
                if(input.equals("end") || input.equals("stop")) {
                    end = true;
                    break;
                }
                if(input.equals("printall")) {
                    for(String s : alphabet.keySet())
                        System.out.println(s+" = "+alphabet.get(s));
                }
                Matcher a = Pattern.compile("\\bencrypt\\s\\b(.+)\\b").matcher(input);
                Matcher b = Pattern.compile("\\bdecrypt\\s\\b(\\w+)\\b").matcher(input);
                if(a.find()) {
                    System.out.println("Matched, encrypting...");
                    System.out.println(encrypt(a.group(1)));
                }
                if(b.find()) {
                    System.out.println("Matched, decrypting...");
                    System.out.println(decrypt(b.group(1)));
                }
                el.log();
            } catch(IOException ex) {
                System.out.println("wth just happened here");
                el.log();
                System.exit(4);
            }
        }
        System.out.println("Program ended successfully.");
    }
    
    public static void main() {
        Encryptor e = new Encryptor();
        e.run();
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
        System.out.println("Reading...");
        reader = new BufferedReader(new FileReader(convFile));
        String line;
        while( (line = reader.readLine()) != null) {
            //line = ln(line,false);
            alphabet.put(line.substring(0,1), line.substring(2));
        }
        reader.close();
        System.out.println("Reading finished, values set.");
    }
    
    private void writeToFile() throws IOException {
        System.out.println("Writing...");
        writer = new BufferedWriter(new FileWriter(convFile));
        for(String k : alphabet.keySet()) {
            String s = k+"|"+alphabet.get(k);
            System.out.println(s);
            writer.write(s+"\n");
        }
        writer.flush();
        writer.close();
        System.out.println("Writing finished, check file.");
    }
    /**This method is gonna be ugly because I do not want to rely on external files*/
    public static String ln(String line, boolean encrypt) {
        String answer = "";
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a",34);
        map.put("b",12);
        map.put("c",63);
        map.put("d",23);
        map.put("e",35);
        map.put("f",15);
        map.put("g",90);
        map.put("h",91);
        map.put("i",64);
        map.put("j",85);
        map.put("k",73);
        map.put("l",42);
        map.put("m",97);
        map.put("n",86);
        map.put("o",71);
        map.put("p",72);
        map.put("q",98);
        map.put("r",79);
        map.put("s",82);
        map.put("t",59);
        map.put("u",57);
        map.put("v",41);
        map.put("w",55);
        map.put("x",53);
        map.put("y",10);
        map.put("z",26);
        map.put("A",94);
        map.put("B",81);
        map.put("C",70);
        map.put("D",31);
        map.put("E",74);
        map.put("F",51);
        map.put("G",13);
        map.put("H",50);
        map.put("I",20);
        map.put("J",62);
        map.put("K",25);
        map.put("L",60);
        map.put("M",49);
        map.put("N",46);
        map.put("O",66);
        map.put("P",61);
        map.put("Q",48);
        map.put("R",28);
        map.put("S",17);
        map.put("T",94);
        map.put("U",40);
        map.put("V",67);
        map.put("W",87);
        map.put("X",54);
        map.put("Y",11);
        map.put("Z",33);
        map.put("1",52);
        map.put("2",68);
        map.put("3",32);
        map.put("4",95);
        map.put("5",96);
        map.put("6",45);
        map.put("7",56);
        map.put("8",14);
        map.put("9",18);
        map.put("0",29);
        map.put("|",19);
        if(encrypt)
            for(int i = 0; i < line.length(); i++)
                answer += map.get(line.substring(i,i+1));
        else
            for(int i = 0; i < line.length(); i+=2)
                for(String key : map.keySet())
                    if(map.get(key).equals(Integer.parseInt(line.substring(i,i+2))))
                        answer += key;
        
        return answer;
    }
}