package se.mau.mattiasjonsson.assignment3;

public class MorseCode {

    final static String [] MORSE = {".-", "-...", "-.-.", "-..", ".", "..-.","--.","....","..", ".---", "-.-",".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--..", ".--.-", ".-.-", "---.", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----", "    "};
    final static char [] CHARACTERS = {'A','B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z', 'Å', 'Ä', 'Ö', '1', '2', '3', '4', '6', '7', '8', '9', '0', ' '};

    /**
     * Converts plain text to morse code
     *
     * @param text the plain text
     * @return
     */
    public static String toMorse(String text){
        String result = "";
        for(char c : text.toCharArray())
            for(int i=0;i<CHARACTERS.length;i++)
                if(c == CHARACTERS[i])
                    result += MORSE[i];
        return result;
    }

    /**
     * Converts morse code to plain text
     *
     * @param text the morse code text
     * @return
     */
    public static String parse(String text){
        String result = "";
        for(String s : text.split("/"))
            for(int i=0;i<MORSE.length;i++)
                if(s.equals(MORSE[i]))
                    result += CHARACTERS[i];
        return result;
    }
}
