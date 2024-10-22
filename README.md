# Android-Encryptor

# Encryptor App

**Encryptor App** is an Android application that allows users to input a string along with two arguments to generate an encrypted output. The app processes the input string based on the given arguments and returns a new, securely encrypted string. For example, entering `"Cat&5DogS"` with `Arg_1 = "5"` and `Arg_2 = "3"` will produce an output like `"nD0&oSB7v."` This application is developed using IntelliJ and Java.

## Encoding Rules:

1. Each character in the alphabet (A-Z, a-z, 0-9) is assigned a numeric value between 0 and 35:
   - Letters are case-insensitive ("A" = 0, "a" = 0, "B" = 1, etc.).
   - Digits "0" to "9" are mapped from 26 to 35.
   
2. The encoded value of a character is calculated using the formula:

where `x` is the numeric value of the character.

3. Spaces and special characters remain unchanged.

4. The transformation affects whether letters are capitalized or not, depending on the original case:
- If the original letter is uppercase, the output will be lowercase.
- If the original letter is lowercase, the output will be uppercase.

---

## Example:

- **Input:**
- Entry Text: `"Cat & 5 DogS"`
- Arg Input 1: `5`
- Arg Input 2: `3`

- **Output:**
- Text Encrypted: `"nD0 & o sB7v"`

### Explanation:

- `"C"` (value = 2) becomes `"n"` (lowercase because `"C"` is capitalized).
- `"a"` (value = 0) becomes `"D"` (capitalized because `"a"` is lowercase).
- `"5"` (value = 31) becomes `"o"` (lowercase).
- Non-alphanumeric characters like spaces and `"&"` remain unchanged.
