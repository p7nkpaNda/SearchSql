public class ConnectorFactory {
    public Connector getConnector(String connectorship){
        if(connectorship.equalsIgnoreCase("AND")){
            return new ConnectorAnd();
        }else if(connectorship.equalsIgnoreCase("OR")){
            return new ConnectorOr();
        }else{
            System.out.println("连接条件错误");
            return null;
        }
    }
}
