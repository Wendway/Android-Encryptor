package edu.gatech.seclass.textprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TextProcessor implements TextProcessorInterface{

    String input = null;

    String output = null;

    boolean ifInsensitive = false;

    String keepString = null;

    String replaceOld = null;
    String replaceNew = null;

    String addSuffix = null;

    Integer nPadding = null;

    boolean ifWhite = false;



    @Override
    public void reset() {
        input = null;

        output = null;

        ifInsensitive = false;

        keepString = null;

        replaceOld = null;
        replaceNew = null;

        addSuffix = null;

        nPadding = 0;

        ifWhite = false;

    }

    @Override
    public void setFilepath(String filepath) {
        input = filepath;
    }

    @Override
    public void setOutputFile(String outputFile) {
        output = outputFile;
    }

    @Override
    public void setCaseInsensitive(boolean caseInsensitive) {
        ifInsensitive = caseInsensitive;
    }

    @Override
    public void setKeepLines(String keepLines) {
        keepString = keepLines;
    }

    @Override
    public void setReplaceText(String oldString, String newString) {
        replaceOld = oldString;
        replaceNew = newString;
    }


    @Override
    public void setAddPaddedLineNumber(int padding) {
        nPadding = padding;
    }


    @Override
    public void setRemoveWhitespace(boolean removeWhitespace) {
        ifWhite = removeWhitespace;
    }

    @Override
    public void setSuffixLines(String suffixLines) {
        addSuffix = suffixLines;
    }

    @Override
    public void textprocessor() throws TextProcessorException {

        //read input content

        String inputContent;
        try {inputContent = new String(Files.readAllBytes(Paths.get(input)));}
        catch (IOException e) {throw new TextProcessorException("Cannot read the file");}


        //split file content by newline
        String[] lines = inputContent.split(System.lineSeparator());

        // -i single error
        if (ifInsensitive && keepString.isEmpty() && replaceOld.isEmpty()) {
            throw new TextProcessorException("-i must be used with -k or -r");
        }

        // -k -r conflict error
        if (!keepString.isEmpty() && !replaceOld.isEmpty()) {
            throw new TextProcessorException("-k -r cannot be used in the same time");
        }

        // -r replaceOld empty error
        if (replaceOld != null && replaceOld.equals("")){
            throw new TextProcessorException("-r oldString cannot be empty");
        }

        // -n -w conflict error
        if ( nPadding!=null && ifWhite) {
            throw new TextProcessorException("-n -w cannot be used in the same time");
        }

        // -n 1-9 out range error
        if ( nPadding!=null && (nPadding < 1 || nPadding >9)){
            throw new TextProcessorException("-n must be in the range of 1 to 9");
        }

        //-n padding
        if( nPadding!=null && !ifWhite) {
            for (int i = 0; i < lines.length; i++) {
                String s = "";
                s = "" + (i+1);
                while(s.length()<nPadding){s = "0" + s;}
                lines[i] = s + " "+ lines[i];}
        }

        //-k keep line
        if (!keepString.isEmpty() && replaceOld.isEmpty() && !ifInsensitive){
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
        if (!keepString.isEmpty() && replaceOld.isEmpty() && ifInsensitive){
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
        if (!replaceOld.isEmpty() && keepString.isEmpty() && !ifInsensitive ){
            for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll(replaceOld, replaceNew);}}


        // -r & -i
        if (!replaceOld.isEmpty() && keepString.isEmpty() && ifInsensitive){
            for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll("(?i)"+replaceOld, replaceNew);}}

        //-s add suffix
        if(!addSuffix.isEmpty()) {for (int i = 0; i < lines.length; i++) {lines[i] = lines[i]+addSuffix;}}

        //-w white remove
        if(ifWhite && nPadding == null) {for (int i = 0; i < lines.length; i++) {lines[i] = lines[i].replaceAll("\\s", "");}}

        String outputContent = String.join(System.lineSeparator(),lines);
        System.out.println(outputContent);

    }

}
