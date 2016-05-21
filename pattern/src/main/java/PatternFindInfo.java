import java.util.regex.Pattern;


public class PatternFindInfo {
    private Pattern pattern;
    private int count;
    private String name ;

    public PatternFindInfo(Pattern pattern, String name) {
        this.pattern = pattern;
        this.count = 0;
        this.name = name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementCount(){
        count++;
    }

}
