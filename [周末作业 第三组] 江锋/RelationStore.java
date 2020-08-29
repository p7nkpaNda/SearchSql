import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RelationStore {
    Map<Relation, String> relations;

    RelationStore() {
        relations = new LinkedHashMap<>();
    }

    public void add(Relation relation) {
        relations.put(relation, relation.action());
    }

    public void delete(Relation relation) {
        if (relation != null) {
            relations.remove(relation);
            relation = null;
        }
    }

    public void changeAttribute(Relation relation,String attribute){
        relation.setAttribute(attribute);
        add(relation);
    }
    public void changePara(Relation relation,String para){
        relation.setPara(para);
        add(relation);
    }

    public List<String> getRelations() {
        List<String> relationsList = new ArrayList<>();
        for (Relation key : relations.keySet()) {
            relationsList.add(relations.get(key));
        }
        return relationsList;
    }
}
