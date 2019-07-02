package hjs.component;

public enum Const {
    FilePath("D:/project_video");

    private String value;

    private Const(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
