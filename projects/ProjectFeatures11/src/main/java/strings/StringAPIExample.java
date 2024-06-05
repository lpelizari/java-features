package strings;

public class StringAPIExample {
    public static void main(String[] args) {

        String str = " Hello World ";
        System.out.println("isBlank.......: " + str.isBlank());
        System.out.println("lines.........: " + str.lines().count());
        System.out.println("strip.........: " + str.strip());
        System.out.println("stripLeading..: " + str.stripLeading());
        System.out.println("stripTrailing.: " + str.stripTrailing());
        System.out.println("repeat........: " + str.repeat(3));
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing isBlank()");
        String str1 = "";
        String str2 = "   ";
        String str3 = "abc";
        System.out.println(str1.isBlank()); // true
        System.out.println(str2.isBlank()); // true
        System.out.println(str3.isBlank()); // false
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing strip() | stripLeading() | stripTrailing()");
        String str4 = "   Hello World!   ";
        System.out.println("\"" + str4.strip() + "\"");        // "Hello World!"
        System.out.println("\"" + str4.stripLeading() + "\""); // "Hello World!   "
        System.out.println("\"" + str4.stripTrailing() + "\"");// "   Hello World!"
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing lines()");
        String str5 = "Line1\nLine2\rLine3\r\nLine4";
        str5.lines().forEach(System.out::println);
        // Output:
        // Line1
        // Line2
        // Line3
        // Line4
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing repeat(int count)");
        String str6 = "abc-";
        String repeatedStr = str6.repeat(3);
        System.out.println(repeatedStr);
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing strip() versus trim()");
        String str7 = "\u2000 Hello \u2000"; // \u2000 é um espaço em branco unicode
        System.out.println("\"" + str7.strip() + "\""); // "Hello"
        System.out.println("\"" + str7.trim() + "\"");  // "\u2000 Hello \u2000"
        System.out.println("\n#################\n");

        //--------------------------------------

        System.out.println("Testing isEmpty() versus isBlank()");
        String emptyStr = "";
        String blankStr = "   ";
        System.out.println(emptyStr.isEmpty()); // true
        System.out.println(blankStr.isEmpty()); // false
        System.out.println(emptyStr.isBlank()); // true
        System.out.println(blankStr.isBlank()); // true
        System.out.println("\n#################\n");

        //--------------------------------------
    }
}
