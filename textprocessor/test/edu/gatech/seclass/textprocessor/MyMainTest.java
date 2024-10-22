package edu.gatech.seclass.textprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MyMainTest {
    // Place all  of your tests in this class, optionally using MainTest.java as an example

    private final String usageStr =
            "Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE"
                    + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

    /*
     * Test Utilities
     */

    private Path createFile(String contents) throws IOException {
        return createFile(contents, "input.txt");
    }

    private Path createFile(String contents, String fileName) throws IOException {
        Path file = tempDirectory.resolve(fileName);
        Files.write(file, contents.getBytes(StandardCharsets.UTF_8));

        return file;
    }

    private String getFileContent(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Test Cases
     */

    //Frame 1: -o flag: output file test
    @Test
    public void Test1() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                    + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                    + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("outputfile.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    //Frame 2: -o flag: throw error, output name already exists
    @Test
    public void Test2() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("outputfile.txt");
        String[] args = {"-o", "outputfile.txt", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 3: -k flag: k exist and repeat
    @Test
    public void Test3() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "THIS IS THE SECOND LINE 789 789." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-k", "the", "-k", "THE", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 4: -r flag: r exist and repeat
    @Test
    public void Test4() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LN 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-r", "line", "ln", "-r", "LINE", "LN",  inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 5: -k & -r flag: error -k & -r conflict
    @Test
    public void Test5() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-r", "first", "1st" , inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 6: -k flag: error number of parameters 0
    @Test
    public void Test6() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 7: -k flag: error number of parameters >0 && <>1
    @Test
    public void Test7() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "123", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 8: -k flag exists but k's content is empty
    @Test
    public void Test8() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 9: -k flag: search case sensitive Upper test
    @Test
    public void Test9() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "THIS IS THE SECOND LINE 789 789." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "THE", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 10: -r flag: error number of parameters 0
    @Test
    public void Test10() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 11: -r flag: error number of parameters <>2
    @Test
    public void Test11() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 12: -r flag: error first parameter is empty
    @Test
    public void Test12() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "", "first", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 13: -r flag: replace case sensitive Upper test
    @Test
    public void Test13() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS the SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "THE", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 14: -i flag: error single i
    @Test
    public void Test14() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 15: -i flag: i exist and repeat
    @Test
    public void Test15() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-i", "-i", "-k", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 16: -i flag: num of para <>0
    @Test
    public void Test16() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 17: -s flag: s exist and repeat
    @Test
    public void Test17() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456.!" + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-s", "?", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }





    //Frame 18: -s flag: error number of parameter 0
    @Test
    public void Test18() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    //Frame 19: -s flag: error number of parameter <>1
    @Test
    public void Test19() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", "!!", "??", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 20: -s flag: error content is empty
    @Test
    public void Test20() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 21: -n flag: exist and repeat
    @Test
    public void Test21() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456." + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "03 This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-n", "3", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 22: -w flag: exist and repeat
    @Test
    public void Test22() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456." + System.lineSeparator()
                + "THISISTHESECONDLINE789789." + System.lineSeparator()
                + "Thisisthethirdline000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = { "-w", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 23: -n & -w flag: error conflict
    @Test
    public void Test23() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "5", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 24: -n flag: error number of parameter 0
    @Test
    public void Test24() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 25: -n flag: error number of parameter <>1
    @Test
    public void Test25() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "5", "3", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 26: -n flag: error not integer
    @Test
    public void Test26() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "5.2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 27: -n flag: error not in the range of 1-9
    @Test
    public void Test27() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "11", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 28: -w flag: error number of parameter <>0
    @Test
    public void Test28() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-w", "5",inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 29: input is empty
    @Test
    public void Test29() throws IOException {
        String input = "" + System.lineSeparator();
        String expected = "" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "k", "123", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    //Frame 30: input with newline terminated
    @Test
    public void Test30() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator();

        String expected = "1 this is the first line 123 456." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 31: input without newline terminated
    @Test
    public void Test31() throws IOException {
        String input = "this is the first line 123 456.";

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 32: -k, -i, -s, -n flag exist
    @Test
    public void Test32() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456.!" + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "03 This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", "-s", "!", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 33: -k, -i, -s, -w flag exist
    @Test
    public void Test33() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456.!" + System.lineSeparator()
                + "THISISTHESECONDLINE789789.!" + System.lineSeparator()
                + "Thisisthethirdline000000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", "-s", "!", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 34: -k, -i, -s flag exist
    @Test
    public void Test34() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456.!" + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 35: -k, -i, -n flag exist
    @Test
    public void Test35() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "001 this is the first line 123 456." + System.lineSeparator()
                + "002 THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "003 This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", "-n", "3", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 36: -k, -i, -w flag exist
    @Test
    public void Test36() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456." + System.lineSeparator()
                + "THISISTHESECONDLINE789789." + System.lineSeparator()
                + "Thisisthethirdline000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 37: -k, -i flag exist
    @Test
    public void Test37() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-k", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 38: -k, -s, -n flag exist
    @Test
    public void Test38() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456.!" + System.lineSeparator()
                + "03 This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-s", "!", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    //Frame 39: -k, -s, -w flag exist
    @Test
    public void Test39() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456.!" + System.lineSeparator()
                + "Thisisthethirdline000000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-s", "!", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 40: -k, -s flag exist
    @Test
    public void Test40() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456.!" + System.lineSeparator()
                + "This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 41: -k, -n flag exist
    @Test
    public void Test41() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456." + System.lineSeparator()
                + "03 This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 42: -k, -w flag exist
    @Test
    public void Test42() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456." + System.lineSeparator()
                + "Thisisthethirdline000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 43: -k flag exists
    @Test
    public void Test43() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-k", "the", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }




    //Frame 44: -r, -i, -s, -n flag exist
    @Test
    public void Test44() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first ln 123 456.!" + System.lineSeparator()
                + "02 THIS IS THE SECOND ln 789 789.!" + System.lineSeparator()
                + "03 This is the third ln 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", "-s", "!", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 45: -r, -i, -s, -w flag exist
    @Test
    public void Test45() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstln123456.!" + System.lineSeparator()
                + "THISISTHESECONDln789789.!" + System.lineSeparator()
                + "Thisisthethirdln000000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", "-s", "!", "-w" , inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 46: -r, -i, -s flag exist
    @Test
    public void Test46() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first ln 123 456.!" + System.lineSeparator()
                + "THIS IS THE SECOND ln 789 789.!" + System.lineSeparator()
                + "This is the third ln 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 47: -r, -i, -n flag exist
    @Test
    public void Test47() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first ln 123 456." + System.lineSeparator()
                + "02 THIS IS THE SECOND ln 789 789." + System.lineSeparator()
                + "03 This is the third ln 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 48: -r, -i, -w flag exist
    @Test
    public void Test48() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstln123456." + System.lineSeparator()
                + "THISISTHESECONDln789789." + System.lineSeparator()
                + "Thisisthethirdln000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 49: -r, -i flag exist
    @Test
    public void Test49() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first ln 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND ln 789 789." + System.lineSeparator()
                + "This is the third ln 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-i", "-r", "line", "ln", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 50: -r, -s, -n flag exist
    @Test
    public void Test50() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first ln 123 456.!" + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "03 This is the third ln 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", "-s", "!", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 51: -r, -s, -w flag exist
    @Test
    public void Test51() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstln123456.!" + System.lineSeparator()
                + "THISISTHESECONDLINE789789.!" + System.lineSeparator()
                + "Thisisthethirdln000000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", "-s", "!", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 52: -r, -s flag exist
    @Test
    public void Test52() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first ln 123 456.!" + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "This is the third ln 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 53: -r, -n flag exist
    @Test
    public void Test53() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first ln 123 456." + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "03 This is the third ln 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 54: -r, -w flag exist
    @Test
    public void Test54() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstln123456." + System.lineSeparator()
                + "THISISTHESECONDLINE789789." + System.lineSeparator()
                + "Thisisthethirdln000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", "-w" , inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }



    //Frame 55: -r flag exist
    @Test
    public void Test55() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first ln 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third ln 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-r", "line", "ln", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 56: -s, -n flag exist
    @Test
    public void Test56() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456.!" + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "03 This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", "!", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 57: -s, -w flag exist
    @Test
    public void Test57() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456.!" + System.lineSeparator()
                + "THISISTHESECONDLINE789789.!" + System.lineSeparator()
                + "Thisisthethirdline000000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", "!", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 58: -s flag exists
    @Test
    public void Test58() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "this is the first line 123 456.!" + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789.!" + System.lineSeparator()
                + "This is the third line 000 000.!" + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    //Frame 59: -n flag exists
    @Test
    public void Test59() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "01 this is the first line 123 456." + System.lineSeparator()
                + "02 THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "03 This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    //Frame 60: -w flag exists
    @Test
    public void Test60() throws IOException {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        String expected = "thisisthefirstline123456." + System.lineSeparator()
                + "THISISTHESECONDLINE789789." + System.lineSeparator()
                + "Thisisthethirdline000000." + System.lineSeparator();

        Path inputFile = createFile(input);

        String[] args = {"-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    //Frame 61 args is empty error
    @Test
    public void Test61() throws Exception {
        String input = "this is the first line 123 456." + System.lineSeparator()
                + "THIS IS THE SECOND LINE 789 789." + System.lineSeparator()
                + "This is the third line 000 000." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertEquals(usageStr, capture.stderr());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }



}
