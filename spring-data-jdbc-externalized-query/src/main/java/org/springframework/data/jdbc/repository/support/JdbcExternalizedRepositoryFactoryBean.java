package org.springframework.data.jdbc.repository.support;

import com.cjcalmeida.spring.data.jdbc.externalize.query.JdbcExternalizedQueryRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.DataAccessStrategy;
import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.jdbc.repository.RowMapperMap;
import org.springframework.data.relational.core.conversion.RelationalConverter;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.io.Serializable;

public class JdbcExternalizedRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends JdbcRepositoryFactoryBean {

    private ApplicationEventPublisher publisher;
    private RelationalMappingContext mappingContext;
    private RelationalConverter converter;
    private DataAccessStrategy dataAccessStrategy;
    private RowMapperMap rowMapperMap = RowMapperMap.EMPTY;
    private NamedParameterJdbcOperations operations;

    protected JdbcExternalizedRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        super.setApplicationEventPublisher(publisher);
        this.publisher = publisher;
    }

    @Autowired
    @Override
    protected void setMappingContext(RelationalMappingContext mappingContext) {
        super.setMappingContext(mappingContext);
        this.mappingContext = mappingContext;
    }

    @Autowired(required = false)
    @Override
    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        super.setDataAccessStrategy(dataAccessStrategy);
        this.dataAccessStrategy = dataAccessStrategy;
    }

    @Autowired(required = false)
    @Override
    public void setRowMapperMap(RowMapperMap rowMapperMap) {
        super.setRowMapperMap(rowMapperMap);
        this.rowMapperMap = rowMapperMap;
    }

    @Autowired
    @Override
    public void setJdbcOperations(NamedParameterJdbcOperations operations) {
        super.setJdbcOperations(operations);
        this.operations = operations;
    }

    @Autowired
    @Override
    public void setConverter(RelationalConverter converter) {
        super.setConverter(converter);
        this.converter = converter;
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        JdbcExternalizedQueryRepositoryFactory factory = new JdbcExternalizedQueryRepositoryFactory(dataAccessStrategy, mappingContext, converter, publisher, operations);
        factory.setRowMapperMap(rowMapperMap);
        return factory;
    }

    @Override
    public void afterPropertiesSet() {
        if (dataAccessStrategy == null) {
            SqlGeneratorSource sqlGeneratorSource = new SqlGeneratorSource(mappingContext);
            this.dataAccessStrategy = new DefaultDataAccessStrategy(sqlGeneratorSource, mappingContext, converter,
                    operations);
        }
        if (rowMapperMap == null) {
            this.rowMapperMap = RowMapperMap.EMPTY;
        }
        super.afterPropertiesSet();
    }
}
