package edu.gatech.seclass.textprocessor;

import org.junit.vintage.engine.discovery.IsPotentialJUnit4TestClass;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {

        String inputContent = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This Is the third line 000 000." + System.lineSeparator()
                + "This Iss the 4th line 000 000." + System.lineSeparator();
        String[] lines = inputContent.split(System.lineSeparator());
        // lines is a java.lang.string

//        -k
        String keepString = "Is";

        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < lines.length; i++){
            if (lines[i].toLowerCase().contains(keepString.toLowerCase())) {
                //newString += lines[i] + System.lineSeparator();
                newString.append(lines[i]);
                newString.append(System.lineSeparator());
            }
        }

        String[] arrayOfString = newString.toString().split(System.lineSeparator());
        lines = arrayOfString;


        int n;
        n = 3;
        DecimalFormat linenumber = new DecimalFormat("0000");
        System.out.println(linenumber.format(n));


//        int x = 4;
//        String s = "";
//        s = ""+x;
//        while (s.length()<6) {s = "0" + s;}
//        System.out.println(s);


        for (int i = 0; i < lines.length; i++) {
            String s = "";
            s = "" + (i+1);
            while(s.length()<2){s = "0" + s;}
            lines[i] = s + " "+ lines[i];}








        String outputContent = String.join(System.lineSeparator(),lines);
        System.out.println(outputContent);





//        newString.deleteCharAt(newString.length() - 1);
//        System.out.println(newString.toString());
//        System.out.println("aa");



        // Expected:
        //This Is the third line 000 000.
        // This Iss the 4th line 000 000.

        //Actual:
        //This Is the third line 000 000. This Iss the 4th line 000 000.



// Reference  String[] n = new String[]{"google","microsoft","apple"};
//            List<String> list =  new ArrayList<String>();
//            Collections.addAll(list, n);
//            System.out.println("list"+list);
//            Iterator<String> iter = list.iterator();
//            while(iter.hasNext()){
//                if(iter.next().contains("le"))
//                    iter.remove();
//            }
//
//            System.out.println("list"+list);
//
//        lines = keepLines(lines,keepString);

//        List<String> list1 = new ArrayList<>();
//        List<String> list2 = new ArrayList<>();
//        Collections.addAll(list1, lines);
////        Collections.addAll(, lines);
//        Iterator<String> iter = list1.iterator();
//        while(iter.hasNext()){
//                if(iter.next().contains(keepString))
//                    iter.remove();
//            }
//        list2.removeAll(list1);
////        lines = list2.toArray(new String[0]);



//        for (int i = 0; i < lines.length; i++) {
//            if (lines[i].contains(keepString)) { newList.toString();}}



//https://stackoverflow.com/questions/9933403/subtracting-one-arraylist-from-another-arraylist


//        String replaceOld = "SEcOND";
//        String replaceNew = "2nd";
//        for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll("(?i)"+replaceOld, replaceNew);}




//        String Checkout = "D:\\\\iFs\\\\APP\\\\Checkout";
//        String DeleteLine = "D:\\IFS\\APP\\Checkout\\trvexp\\client\\Ifs.App\\text.txt";
//        String f = DeleteLine.replaceAll("(?i)"+Checkout, "aaa");
//        System.out.println(f);




//        String[] str_array = {"item1","item2","item3"};
//        List<String> list = new ArrayList<String>(Arrays.asList(str_array));
//        list.remove(2);
//        str_array = list.toArray(new String[0]);
//        System.out.println(Arrays.toString(str_array));


    }



//    private static String keepLines(String input, String content) {
//        List<String> list = new ArrayList<>();
//        Collections.addAll(list, input);
//        List<String> listTemp = new ArrayList<>();
//        Collections.addAll(listTemp, input);
//        Iterator<String> iter = listTemp.iterator();
//        while(iter.hasNext()){
//            if(iter.next().contains(content))
//                iter.remove();
//        }
//        list.removeAll(listTemp);
//        String[]output = list.toArray(new String[0]);
//        return output;
//    }

}



