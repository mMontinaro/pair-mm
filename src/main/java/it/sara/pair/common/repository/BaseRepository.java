package it.sara.pair.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean 
public interface BaseRepository<T, D> extends JpaRepository<T, D> {
    // This interface can be used to define common methods for all repositories
    // For this project, it is not useful, but if it revisited, it can be used to define and easily code shared repo behavior
    // I'm keeping this class for familiaritiy's sake.
}
