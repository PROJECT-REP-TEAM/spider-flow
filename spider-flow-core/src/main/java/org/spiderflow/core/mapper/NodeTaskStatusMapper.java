package org.spiderflow.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.spiderflow.core.model.Function;
import org.spiderflow.core.model.NodeTaskStatus;

@Mapper
public interface NodeTaskStatusMapper extends BaseMapper<NodeTaskStatus> {

    @Select("select last_id from `sp_node_task_status` where flow_id = #{flowId}")
    Long findByFlowId(@Param("flowId") String flowId);

    @Update("update sp_node_task_status set last_id = #{lastId} where flow_id = #{flowId}")
    int updateLastIdByFlowId(@Param("flowId") String flowId, @Param("lastId") Long lastId);
}
