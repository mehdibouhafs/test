package views.formdata;

/**
 * Created by MBS on 29/07/2016.
 */
public class ParamformData01 {

    public String separator;

    public int numberLine;


    public ParamformData01() {
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public int getNumberLine() {
        return numberLine;
    }

    public void setNumberLine(int numberLine) {
        this.numberLine = numberLine;
    }

    @Override
    public String toString() {
        return "ParamformData01{" +
                "separator='" + separator + '\'' +
                ", numberLine=" + numberLine +
                '}';
    }
}
