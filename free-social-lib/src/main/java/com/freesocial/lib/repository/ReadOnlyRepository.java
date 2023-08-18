package com.freesocial.lib.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * To be extended by read only repositories
 * @param <T> Class type
 * @param <ID> Class ID
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
}
