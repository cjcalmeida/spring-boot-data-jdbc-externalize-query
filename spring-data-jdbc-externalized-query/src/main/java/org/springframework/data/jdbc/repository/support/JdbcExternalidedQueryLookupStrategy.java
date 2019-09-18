package org.springframework.data.jdbc.repository.support;

import com.cjcalmeida.spring.data.jdbc.externalize.query.JdbcExternalizedQueryMethod;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.EntityRowMapper;
import org.springframework.data.jdbc.repository.RowMapperMap;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.lang.reflect.Method;

public class JdbcExternalidedQueryLookupStrategy implements QueryLookupStrategy {

    private ApplicationEventPublisher publisher;
    private RelationalMappingContext context;
    private RelationalConverter converter;
    private DataAccessStrategy accessStrategy;
    private RowMapperMap rowMapperMap;
    private NamedParameterJdbcOperations operations;

    public JdbcExternalidedQueryLookupStrategy(ApplicationEventPublisher publisher, RelationalMappingContext context, RelationalConverter converter, DataAccessStrategy dataAccessStrategy, RowMapperMap rowMapperMap, NamedParameterJdbcOperations operations) {
        this.publisher = publisher;
        this.context = context;
        this.converter = converter;
        this.accessStrategy = dataAccessStrategy;
        this.rowMapperMap = rowMapperMap;
        this.operations = operations;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        JdbcExternalizedQueryMethod queryMethod = new JdbcExternalizedQueryMethod(method, metadata, factory, namedQueries);

        RowMapper<?> rowMapper = queryMethod.isModifyingQuery() ? null : createRowMapper(queryMethod);

        return new JdbcRepositoryQuery(publisher, context, queryMethod, operations, rowMapper);
    }

    private RowMapper<?> createRowMapper(JdbcQueryMethod queryMethod) {

        Class<?> returnedObjectType = queryMethod.getReturnedObjectType();

        RelationalPersistentEntity<?> persistentEntity = context.getPersistentEntity(returnedObjectType);

        if (persistentEntity == null) {
            return SingleColumnRowMapper.newInstance(returnedObjectType, converter.getConversionService());
        }

        return determineDefaultRowMapper(queryMethod);
    }

    private RowMapper<?> determineDefaultRowMapper(JdbcQueryMethod queryMethod) {

        Class<?> domainType = queryMethod.getReturnedObjectType();

        RowMapper<?> typeMappedRowMapper = rowMapperMap.rowMapperFor(domainType);

        return typeMappedRowMapper == null //
                ? new EntityRowMapper<>( //
                context.getRequiredPersistentEntity(domainType), //
                context, //
                converter, //
                accessStrategy) //
                : typeMappedRowMapper;
    }
}
