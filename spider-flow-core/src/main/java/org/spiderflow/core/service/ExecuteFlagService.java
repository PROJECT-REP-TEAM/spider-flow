package org.spiderflow.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spiderflow.core.mapper.ExecuteFlagMapper;
import org.spiderflow.core.model.ExecuteFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExecuteFlagService extends ServiceImpl<ExecuteFlagMapper, ExecuteFlag> {

    @Autowired
    private ExecuteFlagMapper executeFlagMapper;

    @Transactional
    public Map<String, Object> nextData(String flowId, String pkName, String sql) {

        String flag = "null";

        ExecuteFlag executeFlag = executeFlagMapper.nexData(flowId);
        if (executeFlag == null) {
            executeFlag = new ExecuteFlag();
            executeFlag.setFlowId(flowId);
            executeFlag.setFlag("-1");
            executeFlagMapper.insert(executeFlag);
        }

        return new HashMap<>();
    }
}
