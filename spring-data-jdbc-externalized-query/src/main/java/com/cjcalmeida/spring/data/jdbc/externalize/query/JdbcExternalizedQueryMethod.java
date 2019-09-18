package com.cjcalmeida.spring.data.jdbc.externalize.query;

import org.springframework.data.jdbc.repository.support.JdbcQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.lang.reflect.Method;

public class JdbcExternalizedQueryMethod extends JdbcQueryMethod {

    private NamedQueries queries;
    private Method method;

    public JdbcExternalizedQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries queries) {
        super(method, metadata, factory);
        this.queries = queries;
        this.method = method;
    }

    @Override
    public String getAnnotatedQuery() {
        String query = super.getAnnotatedQuery();
        if(queries.hasQuery(query)){
            query = queries.getQuery(query);
        }
        return query;
    }
}
