package no.api.youngman.neo4j;

import java.util.Iterator;
import java.util.Map;

public interface CypherExecutor {
    Iterator<Map<String,Object>> query(String statement, Map<String, Object> params);
    Iterator<Map<String,Object>> query(String statement);
}

