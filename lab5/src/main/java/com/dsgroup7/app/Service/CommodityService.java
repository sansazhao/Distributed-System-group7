package com.dsgroup7.app.Service;

import com.dsgroup7.app.Entity.Commodity;
import com.dsgroup7.app.Repository.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commodityService")
public class CommodityService {
    @Autowired
    private CommodityRepository commodityRepository;

    public Commodity getCommodity(int id) {
        return commodityRepository.findById(id);
    }

    public void updateCommodity(Commodity commodity) {
        commodityRepository.save(commodity);
    }
}
