import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConnectorStore {
    Map<Connector, String> connectors;

    ConnectorStore() {
        connectors = new LinkedHashMap<>();
    }

    public void add(Connector connector) {
        connectors.put(connector, connector.action());
    }

    public void delete(Connector connector) {
        if (connector != null) {
            connectors.remove(connector);
            connector = null;
        }
    }

    public void change(Connector connector) {
        if (connectors.get(connector).equals("and")) {
            connectors.put(connector, "or");
        } else {
            connectors.put(connector, "and");
        }
    }

    public List<String> getConnectors() {
        List<String> connectorsList = new ArrayList<>();
        for (Connector key : connectors.keySet()) {
            connectorsList.add(connectors.get(key));
        }
        return connectorsList;
    }
}
