public class RelationNotEqual extends Relation{
    String attribute;
    String para;

    RelationNotEqual(String attribute,String para){
        this.attribute = attribute;
        this.para = para;
        super.attribute = attribute;
        super.para = para;
    }
    @Override
    public String action() {
        return this.attribute+"!=\""+this.para+"\"";
    }

    @Override
    public void setAttribute(String attribute) {
        this.attribute = attribute;
        super.attribute =attribute;

    }

    @Override
    public void setPara(String para) {
        this.para = para;
        super.para = para;
    }

}
