package representation;

public class NGramModel {
    public NGramModel() {
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {

        this.length = length;
    }

    public NGramModel(int length) {

        this.length = length;
    }

    protected int length;

}
