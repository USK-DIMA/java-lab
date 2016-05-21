import java.util.regex.Pattern;

public class PatternReplaceInfo {

    private Pattern pattern;

    private String strFoReplace;

    private String patternString;

    public PatternReplaceInfo(String patternString, String strFoReplace) {
        this.patternString=patternString;
        this.strFoReplace = strFoReplace;
        this.pattern = null;
    }

    public Pattern getPattern() {
        if(pattern==null){
            pattern = Pattern.compile(patternString);
        }
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getStrFoReplace() {
        return strFoReplace;
    }

    public void setStrFoReplace(String strFoReplace) {
        this.strFoReplace = strFoReplace;
    }

    public String getPatternString() {
        return patternString;
    }
}
