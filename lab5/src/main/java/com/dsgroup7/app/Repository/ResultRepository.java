package com.dsgroup7.app.Repository;

import com.dsgroup7.app.Entity.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name="Result")
@Qualifier("resultRepository")
public interface ResultRepository extends CrudRepository<Result, Integer> {
    Result save(Result result);
}
