import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage:\nMain file");
            return;
        }

        List<PatternFindInfo> patternFindInfo = new ArrayList<>();
        patternFindInfo.add(new PatternFindInfo(Pattern.compile("....+(\\s|$)"), "p1"));
        patternFindInfo.add(new PatternFindInfo(Pattern.compile(".+(ауеыоэяию)(\\s|$)"), "p2"));
        patternFindInfo.add(new PatternFindInfo(Pattern.compile("[ауеыоэяию]{2,}.*(\\s|$)"), "p3"));
        patternFindInfo.add(new PatternFindInfo(Pattern.compile("(\\D)\\1{1,}.*(\\s|$)"), "p4"));
        patternFindInfo.add(new PatternFindInfo(Pattern.compile("(\\D)(.)(\\D)\\s"), "For \"replase 2\""));

        List<PatternReplaceInfo> patternReplaceInfo = new ArrayList<>();
        patternReplaceInfo.add(new PatternReplaceInfo("\\s+", " "));
        //TODO разобраться с заменой
        patternReplaceInfo.add(new PatternReplaceInfo("(\\S)(\\S)(\\S)(\\s)", "\\1\\3\\2\\4"));

        try(BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
            String s;
            while ((s = in.readLine()) != null) {
                for(PatternFindInfo pInfo : patternFindInfo) {
                    find(s, pInfo);
                }
                for(PatternReplaceInfo pReplace : patternReplaceInfo) {
                    s = replace(s, pReplace);
                }
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        printInfo(patternFindInfo);

    }

    private static String replace(String s, PatternReplaceInfo pReplace) {
       return s.replaceAll(pReplace.getPatternString(), pReplace.getStrFoReplace());
    }

    private static void printInfo(List<PatternFindInfo> patternFindInfoList) {
        for(PatternFindInfo pInfo : patternFindInfoList) {
            System.out.println(pInfo.getName()+": "+pInfo.getCount());
        }
    }

    private static void find(String s, PatternFindInfo patternFindInfo) {
        Pattern pattern = patternFindInfo.getPattern();
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            patternFindInfo.incrementCount();
        }
    }
}
