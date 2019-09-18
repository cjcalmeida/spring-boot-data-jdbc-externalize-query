package com.cjcalmeida.spring.data.jdbc.externalize.query;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.repository.RowMapperMap;
import org.springframework.data.jdbc.repository.support.JdbcExternalidedQueryLookupStrategy;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Optional;

public class JdbcExternalizedQueryRepositoryFactory extends JdbcRepositoryFactory {

    private DataAccessStrategy dataAccessStrategy;
    private RelationalMappingContext context;
    private RelationalConverter converter;
    private ApplicationEventPublisher publisher;
    private NamedParameterJdbcOperations operations;

    private RowMapperMap rowMapperMap;

    public JdbcExternalizedQueryRepositoryFactory(DataAccessStrategy dataAccessStrategy, RelationalMappingContext context, RelationalConverter converter, ApplicationEventPublisher publisher, NamedParameterJdbcOperations operations) {
        super(dataAccessStrategy, context, converter, publisher, operations);
        this.dataAccessStrategy = dataAccessStrategy;
        this.context = context;
        this.converter = converter;
        this.publisher = publisher;
        this.operations = operations;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        super.getQueryLookupStrategy(key, evaluationContextProvider);
        return Optional.of(new JdbcExternalidedQueryLookupStrategy(publisher, context, converter, dataAccessStrategy, rowMapperMap, operations));
    }

    @Override
    public void setRowMapperMap(RowMapperMap rowMapperMap) {
        super.setRowMapperMap(rowMapperMap);
        this.rowMapperMap = rowMapperMap;
    }
}
