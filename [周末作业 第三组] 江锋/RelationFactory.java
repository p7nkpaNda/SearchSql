public class RelationFactory {
    public Relation getRelation(String attribute,String relationship,String para){
        if(relationship.equalsIgnoreCase("EQUAL")){
            return new RelationEqual(attribute,para);
        }else if(relationship.equalsIgnoreCase("NOTEQUAL")){
            return new RelationNotEqual(attribute,para);
        }else if(relationship.equalsIgnoreCase("CONTAINS")){
            return new RelationContains(attribute,para);
        }else if(relationship.equalsIgnoreCase("NOTCONTAINS")){
            return new RelationNotContains(attribute,para);
        }else{
            System.out.println("筛选条件错误");
            return null;
        }
    }
}
