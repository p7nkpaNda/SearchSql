import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchSql {
    static List<Pair<Relation,Connector>> alive = new ArrayList<>();
    static RelationStore relationStore = new RelationStore();
    static ConnectorStore connectorStore = new ConnectorStore();
    static RelationFactory relationFactory = new RelationFactory();
    static ConnectorFactory connectorFactory = new ConnectorFactory();


    static void initialize(String attribute,String relationship,String para){
        Relation relation = relationFactory.getRelation(attribute,relationship,para);
        if (null==relation){
            System.exit(1);
        }
        relationStore.add(relation);
        alive.add(new Pair<Relation,Connector>(relation,null));
/*        System.out.println("添加了第一条筛选条件\n" +
                "目前的筛选条件有：" + relationStore.getRelations());*/
    }

    static void addcriteria(String connectorship,String attribute,String relationship,String para){
        Connector connector = connectorFactory.getConnector(connectorship);
        if (null==connector){
            System.exit(1);
        }
        connectorStore.add(connector);
        Relation relation = relationFactory.getRelation(attribute,relationship,para);
        if (null==relation){
            System.exit(1);
        }
        relationStore.add(relation);
        alive.add(new Pair<Relation,Connector>(relation,connector));
/*        System.out.println("又添加了一条筛选条件\n"+
                "目前的筛选条件有：" + relationStore.getRelations());*/

    }

    //删除第几条筛选条件，从1开始
    static void delcriteria(int index){
        if(index==1 && alive.size()>1){
            connectorStore.delete(alive.get(index).getValue());
            Relation relation = alive.get(index).getKey();
            alive.remove(index);
            alive.add(index,new Pair<>(relation,null));
        }
        relationStore.delete(alive.get(index-1).getKey());
        connectorStore.delete(alive.get(index-1).getValue());
        alive.remove(index-1);
    }

    //改变第几个AND/OR的连接关系，从1开始
    static void changeConnector(int index){
        connectorStore.change(alive.get(index).getValue());
    }

    //改变第几条筛选条件的attribute属性,从1开始
    static void changeAttribute(int index,String attribute){
        relationStore.changeAttribute(alive.get(index-1).getKey(),attribute);
    }

    //改变第几条筛选条件的para属性,从1开始
    static void changePara(int index,String para){
        relationStore.changePara(alive.get(index-1).getKey(),para);
    }

    //AND的优先级大于OR,这里按照一层层添加括号。
    static void createsql(List<String> relationsList,List<String> connectorsList){
        String prefixsql = "select * from Attribute where ";
        String sql = "";

        System.out.println("筛选条件： "+relationsList);
        System.out.println("连接符： "+connectorsList);
        if (relationsList.size()==0){
            System.out.println("筛选条件为零！");
            System.exit(1);
        }
        if(relationsList.size()==connectorsList.size()+1){
            sql += relationsList.get(0);
            for(int i = 0;i<connectorsList.size();i++){
                sql = String.format("(%s %s %s)", sql, connectorsList.get(i), relationsList.get(i+1));
            }
        }else{
            System.out.println("筛选条件有错误！无法匹配！");
            System.exit(1);
        }
        System.out.println("您需要的sql语句如下：");
        System.out.println(prefixsql+sql);
    }

    static void step1(){
        System.out.println("您好，请问您想要怎样的筛选条件? 这里的格式形如“CompanyName equal HTSC”\n" +
                "变量选择有：[1-CompanyName，2-ContactName，3-ContactTitle，4-Phone]\n" +
                "关系选择有：[1-Equal，2-NotEqual，3-Contains，4-NotContains]\n" +
                "以及输入您的匹配参数,"+
                "请严格按照(1 1 HTSC)这样的格式输入请您参数，否则无法正确读取！\n"+
                "按q可退出程序！");
        boolean attributeflag = false;
        Attribute attribute = Attribute.CompanyName;
        boolean relationshipflag = false;
        Relationship relationship = Relationship.Equal;
        String para = "";
        Scanner sc = new Scanner(System.in);
        while(!attributeflag || !relationshipflag){
            attributeflag = false;
            relationshipflag = false;
            String[] input = sc.nextLine().trim().split(" ",3);
            if (input.length<2){
                if (input[0].equals("q")){
                    System.exit(0);
                }
                System.out.println("输入的参数不够，请正确输入参数");
                continue;
            }
            switch (input[0]){
                case "1":
                    attribute = Attribute.CompanyName;
                    attributeflag = true;
                    break;
                case "2":
                    attribute = Attribute.ContactName;
                    attributeflag = true;
                    break;
                case "3":
                    attribute = Attribute.ContactTitle;
                    attributeflag = true;
                    break;
                case "4":
                    attribute = Attribute.Phone;
                    attributeflag = true;
                    break;
                default:
                    System.out.println("请选择正确的变量！");
            }
            switch (input[1]){
                case "1":
                    relationship = Relationship.Equal;
                    relationshipflag = true;
                    break;
                case "2":
                    relationship = Relationship.NotEqual;
                    relationshipflag = true;
                    break;
                case "3":
                    relationship = Relationship.Contains;;
                    relationshipflag = true;
                    break;
                case "4":
                    relationship = Relationship.NotContains;;
                    relationshipflag = true;
                    break;
                default:
                    System.out.println("请选择正确的关系！");
            }
            if(input.length>2){
                para = input[2];
            }

        }
        SearchSql.initialize(attribute.toString(),relationship.toString(),para);
    }


    static void step2(){
        boolean flag = false;
        Scanner sc = new Scanner(System.in);
        while (!flag){
            System.out.println("您好，请问您还需要什么操作？[1-添加，2-删除，3-修改连接符，4-打印SQL语句]");
            System.out.println("目前的筛选条件有：" + relationStore.getRelations());
            System.out.println("目前的有效连接符有：" + connectorStore.getConnectors());
            int input = sc.nextInt();
            switch (input){
                case 1:
                    addOperation();
                    break;
                case 2:
                    delOperation();
                    break;
                case 3:
                    changeConOperation();
                    break;
                case 4:
                    createsql(relationStore.getRelations(),connectorStore.getConnectors());
                    flag = true;
                    break;
                default:
                    System.out.println("请选择正确的操作！");
            }
        }
    }

    static void addOperation(){
        Scanner sc = new Scanner(System.in);
        System.out.println("您好，请问您还想要添加怎样的筛选条件? 格式如之前所示\n" +
                "变量选择有：[1-CompanyName，2-ContactName，3-ContactTitle，4-Phone]\n" +
                "关系选择有：[1-Equal，2-NotEqual，3-Contains，4-NotContains]\n" +
                "连接符选择有：[1-and，2-or]\n" +
                "以及输入您的匹配参数\n"+
                "按q可退出本次操作");
        boolean attributeflag = false;
        Attribute attribute = Attribute.CompanyName;
        boolean relationshipflag = false;
        Relationship relationship = Relationship.Equal;
        String para = "";
        boolean connectorshipflag = false;
        String connectorship = "";
        boolean flag = true;
        while(!attributeflag || !relationshipflag || !connectorshipflag){
            attributeflag = false;
            relationshipflag = false;
            String[] input = sc.nextLine().trim().split(" ",4);
            if (input.length<3 && input.length!=0){
                if (input[0].equals("q")){
                    return;
                }
                System.out.println("添加筛选条件的输入参数不够，请正确输入参数");
                continue;
            }
            switch (input[0]){
                case "1":
                    attribute = Attribute.CompanyName;
                    attributeflag = true;
                    break;
                case "2":
                    attribute = Attribute.ContactName;
                    attributeflag = true;
                    break;
                case "3":
                    attribute = Attribute.ContactTitle;
                    attributeflag = true;
                    break;
                case "4":
                    attribute = Attribute.Phone;
                    attributeflag = true;
                    break;
                default:
                    System.out.println("请选择正确的变量！");
            }
            switch (input[1]){
                case "1":
                    relationship = Relationship.Equal;
                    relationshipflag = true;
                    break;
                case "2":
                    relationship = Relationship.NotEqual;
                    relationshipflag = true;
                    break;
                case "3":
                    relationship = Relationship.Contains;;
                    relationshipflag = true;
                    break;
                case "4":
                    relationship = Relationship.NotContains;;
                    relationshipflag = true;
                    break;
                default:
                    System.out.println("请选择正确的关系！");
            }
            switch (input[2]){
                case "1":
                    connectorship = "and";
                    connectorshipflag = true;
                    break;
                case "2":
                    connectorship = "or";
                    connectorshipflag = true;
                    break;
                default:
                    System.out.println("请选择正确的连接符！");
            }
            if(input.length>3){
                para = input[3];
            }

        }
        if(flag ==true) {
            System.out.println("test");
            SearchSql.addcriteria(connectorship, attribute.toString(), relationship.toString(), para);
        }
    }


    static void delOperation(){
        Scanner sc = new Scanner(System.in);
        List<String> relationsList = relationStore.getRelations();
        System.out.println("目前的SQL筛选语句如下,一共有"+relationsList.size()+"条");
        System.out.println(relationsList);
        System.out.println("您好，请问您想要删除哪条条件呢?(从1开始)");
        System.out.println("按q可退出本次操作");

        int index = 0;
        while(true){
            String input = sc.nextLine().trim();
            if(input.equals("q")){
                return;
            }
            index = Integer.parseInt(input);
            if(index>relationsList.size() || index <=0){
                System.out.println("请输入正确的参数！");
                continue;
            }
            break;

        }
        System.out.println("test");
        delcriteria(index);
    }

    static void changeConOperation(){
        Scanner sc = new Scanner(System.in);
        List<String> relationsList = relationStore.getRelations();
        List<String> connectorsList = connectorStore.getConnectors();
        System.out.println("目前有效的连接符有"+connectorsList.size()+"个");
        System.out.println("如下是目前的筛选条件和相应的连接符：");
        System.out.println(relationsList);
        System.out.println(connectorsList);
        System.out.println("您好，请问您想要修改哪个连接符呢?");
        int index = 0;
        while ((index = sc.nextInt()) >connectorsList.size()){
            System.out.println("请输入正确的参数！");
        }
        changeConnector(index);

    }
    public static void main(String[] args) {
        //询问需要的第一条筛选条件
        step1();
        //打印当前筛选条件，并循环询问下一步操作，包括添加，删除，修改，打印SQL语句。
        step2();

    }
}
