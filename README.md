# Android-Encryptor

Encryptor App is an Android application that allows users to input a string along with two arguments to generate an encrypted output. The app processes the input string based on the given arguments and returns a new, securely encrypted string. For example, entering "Cat&5DogS" with Arg_1 = "5" and Arg_2 = "3" will produce an output like "nD0&oSB7v." This application is developed using IntelliJ and Java, 

The encoding rule applies as follows:

Each character in the alphabet (A-Z, a-z, 0-9) is assigned a numeric value between 0 and 35, where letters are case-insensitive ("A" = 0, "a" = 0, "B" = 1, etc.), and digits "0"-"9" are mapped from 26 to 35.
The encoded value of a character is calculated using the formula:
E(x) = (Arg_1 * x + Arg_2) % 36, where x is the numeric value of the character.
Spaces and special characters remain unchanged, but the transformation affects whether letters are capitalized or not, depending on the original case.
Example:

Input:
Entry Text = "Cat & 5 DogS"
Arg Input 1 = 5
Arg Input 2 = 3
Output:
Text Encrypted = "nD0 & o sB7v"
Explanation:

"C" (value = 2) becomes "n" (lowercase because "C" is capitalized).
"a" (value = 0) becomes "D" (capitalized because "a" is lowercase).
"5" (value = 31) becomes "o" (lowercase).
Non-alphanumeric characters like spaces and "&" remain unchanged.
