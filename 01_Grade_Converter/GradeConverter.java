import java.io.*;

// Class to convert unit marks to grades
class GradeConverter
{
    private static final String NULL = null;

    // Converts a numerical mark (0 to 100) into a textual grade
    // Returns "Invalid" if the number is invalid
    String convertMarkToGrade(final int mark) {
        if(mark == -1 || mark > 100){ return "Invalid"; }

        if(mark < 50) { return "Fail"; }

        if(mark < 60) { return "Pass"; }

        if(mark < 70) { return "Merit"; }

        return "Distinction";
    }

    // Reads a mark from a String and returns the mark as an int (0 to 100)
    // Returns -1 if the string is invalid
    int convertStringToMark(final String text)
    {
        if(text == NULL||text =="" ) { return -1; }
        int [] markarr = new int[text.length()];
        int finalscore=0, j=0, isunitdigit=0,power_num = 0;
        for(int i=text.length()-1;i>=0;i--){
            if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '%' && text.charAt(i) != '.'){
                return -1;
            }
            if(text.charAt(i) == '.'){
                isunitdigit = j;
            }
            if(text.charAt(i) != '%'&& text.charAt(i) != '.'){
                markarr[j] = convertCharToInt(text.charAt(i));
                j++;
            }
        }
        for(int i =0;i<isunitdigit;i++){
            if(markarr[i]>=5){ markarr[i+1]++; }
        }
        for(int i =isunitdigit;i<markarr.length;i++){
            finalscore+= markarr[i] * Math.pow(10, power_num);
            power_num++;
        }
        if(finalscore>100 || finalscore<0){ return -1; }
        else { return finalscore; }
    }

    // Convert a single character to an int (0 to 9)
    // Returns -1 if char is not numerical
    int convertCharToInt(final char c) {
        switch (c) {
        case '0':
            return'a' - 'a';

        case '1':
            return 'b' - 'a';

        case '2':
            return 'c' - 'a';

        case '3':
            return 'd' - 'a';

        case '4':
            return 'e' - 'a';

        case '5':
            return 'f' - 'a';

        case '6':
            return 'g' - 'a';

        case '7':
            return 'h' - 'a';

        case '8':
            return 'i' - 'a';

        case '9':
            return 'j' - 'a';

        default:
            return -1;
        }
    }

    public static void main(final String[] args) throws IOException {
        final GradeConverter converter = new GradeConverter();
        while (true) {
            System.out.print("Please enter your mark: ");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            final String input = reader.readLine();
            final int mark = converter.convertStringToMark(input);
            final String grade = converter.convertMarkToGrade(mark);
            System.out.println("A mark of " + input + " is " + grade);
        }
    }
}
