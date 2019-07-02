package com.dsgroup7.app.Repository;

import com.dsgroup7.app.Entity.Commodity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name="Commodity")
@Qualifier("commodityRepository")
public interface CommodityRepository extends CrudRepository<Commodity, Integer> {
    Commodity save(Commodity commodity);

    Commodity findById(int id);
}
