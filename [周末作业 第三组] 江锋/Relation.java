public abstract class Relation {
    String attribute;
    String para;

    public abstract String action();

    public abstract void setAttribute(String attribute);

    public abstract void setPara(String para);

}
