package org.spiderflow.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.spiderflow.core.model.ExecuteFlag;

public interface ExecuteFlagMapper extends BaseMapper<ExecuteFlag> {

    @Select("select id,flag,flowId,createTime from sp_execute_flag where flag = #{flowId} for update")
    ExecuteFlag nexData(String flowId);
}
