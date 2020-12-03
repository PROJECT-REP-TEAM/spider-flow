package org.spiderflow.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.spiderflow.core.mapper.NodeTaskStatusMapper;
import org.spiderflow.core.model.NodeTaskStatus;
import org.springframework.stereotype.Service;

@Service
public class NodeTaskStatusService extends ServiceImpl<NodeTaskStatusMapper, NodeTaskStatus> {
}
