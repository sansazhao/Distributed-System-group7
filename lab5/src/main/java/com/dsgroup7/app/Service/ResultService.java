package com.dsgroup7.app.Service;

import com.dsgroup7.app.Entity.Result;
import com.dsgroup7.app.Repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("resultService")
public class ResultService {
    @Autowired
    private ResultRepository resultRepository;

    public void addResult(Result result) {
        resultRepository.save(result);
    }
}
