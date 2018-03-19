import java.util.*;
public class Generator {
    public static void main() {
        ArrayList<Integer> list = new ArrayList<>();
        for(int i = 100; i < 1000; i++)
            list.add(i);
        String[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890~!@#$%^&*()-_=+{}[]|;:'\",.<>/?".split("");
        for(int i = 0; i < chars.length; i++)
            System.out.println("map.put("+chars[i]+","+list.remove((int)(Math.random()*list.size()))+");");
    }
}