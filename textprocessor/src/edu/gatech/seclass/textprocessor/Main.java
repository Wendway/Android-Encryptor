package edu.gatech.seclass.textprocessor;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    // Empty Main class for compiling Individual Project
    // During Deliverable 1 and Deliverable 2, DO NOT ALTER THIS CLASS or implement it

    public static void main(String[] args) {

        String inputContent;

        boolean ifOutput = false;
        String output = null;
        
        boolean ifInsensitive = false;
        
        boolean ifKeepString = false;
        String keepString = null;
        
        boolean ifReplace = false;
        String replaceOld = null;
        String replaceNew = null;
        
        boolean ifSuffix = false;
        String addSuffix = null;
        
        boolean ifPadding = false;
        int nPadding = 0;
        
        boolean ifWhite = false;



        if(args.length == 0) {usage();return;}

        for (int i=0; i <(args.length-1); i++){

            if(args[i].equals("-o")) {ifOutput = true; continue;}

            if(args[i].equals("-i")) {ifInsensitive = true; continue;}

            if(args[i].equals("-k")) {
                ifKeepString = true;
                if (i> (args.length - 3) ) {usage();return;}
                keepString = args[i+1]; continue;}

            if(args[i].equals("-r")) {
                ifReplace = true;
                if (i > (args.length - 4)) {usage();return;}
                replaceOld = args[i+1]; replaceNew = args[i+2];
                if (replaceOld.equals("")) {usage();return;}
                continue;}

            if(args[i].equals("-s")) {ifSuffix = true;
                addSuffix = args[i+1];
                if (addSuffix.equals("")) {usage();return;}
                continue;}

            if(args[i].equals("-n")) {
                ifPadding = true;
                try {nPadding = Integer.parseInt(args[i+1]);}
                catch (NumberFormatException e) {usage();return;};
                if (nPadding >9 || nPadding <1){usage();return;}
                continue;}

            if(args[i].equals("-w")) {ifWhite = true;
                if ((args.length - i) % 2 ==1) {usage();return;}
            }

        }

        //read input content


        try {inputContent = new String(Files.readAllBytes(Paths.get(args[args.length-1])));}
        catch (IOException e) {usage(); return;}


        //split file content by newline
        String[] lines = inputContent.split(System.lineSeparator());

        // -i single error
        if (ifInsensitive && !ifKeepString && !ifReplace) {usage();return;}

        // -k -r conflict error
        if (ifKeepString && ifReplace) {usage();return;}

        // -n -w conflict error
        if (ifPadding && ifWhite) {usage();return;}

        //-n padding
        if(ifPadding && !ifWhite) {
            for (int i = 0; i < lines.length; i++) {
                String s = "";
                s = "" + (i+1);
                while(s.length()<nPadding){s = "0" + s;}
                lines[i] = s + " "+ lines[i];}
        }

        //-k keep line
        if (ifKeepString && !ifReplace && !ifInsensitive){
            StringBuilder newString = new StringBuilder();
            for (int i = 0; i < lines.length; i++){
                if (lines[i].contains(keepString)) {
                    newString.append(lines[i]);
                    newString.append(System.lineSeparator());
                }
            }
            String[] arrayOfString = newString.toString().split(System.lineSeparator());
            lines = arrayOfString;
        }

//        // -i & -k
        if (ifKeepString && !ifReplace && ifInsensitive){
            StringBuilder newString = new StringBuilder();
            for (int i = 0; i < lines.length; i++){
                if (lines[i].toLowerCase().contains(keepString.toLowerCase())) {
                    newString.append(lines[i]);
                    newString.append(System.lineSeparator());
                }
            }
            String[] arrayOfString = newString.toString().split(System.lineSeparator());
            lines = arrayOfString;
        }

        // -r replace
        if (ifReplace &&!ifKeepString && !ifInsensitive ){
        for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll(replaceOld, replaceNew);}}


        // -r & -i
        if (ifReplace &&!ifKeepString && ifInsensitive){
            for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll("(?i)"+replaceOld, replaceNew);}}

        //-s add suffix
        if(ifSuffix) {for (int i = 0; i < lines.length; i++) {lines[i] = lines[i]+addSuffix;}}

        //-w white remove
        if(ifWhite && !ifPadding) {for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll("\\s", "");}}



        //-o
        if (ifOutput) {


        }


        //write output content
        String outputContent = String.join(System.lineSeparator(),lines);
        System.out.println(outputContent);


    }

    private static void usage() {
        System.err.println("Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE");
    }
    


}
