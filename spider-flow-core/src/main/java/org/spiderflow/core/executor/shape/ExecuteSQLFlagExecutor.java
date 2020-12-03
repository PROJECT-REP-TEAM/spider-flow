package org.spiderflow.core.executor.shape;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.Grammerable;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.core.io.HttpRequest;
import org.spiderflow.core.io.HttpResponse;
import org.spiderflow.core.mapper.NodeTaskStatusMapper;
import org.spiderflow.core.model.NodeTaskStatus;
import org.spiderflow.core.service.NodeTaskStatusService;
import org.spiderflow.core.utils.DataSourceUtils;
import org.spiderflow.core.utils.ExpressionUtils;
import org.spiderflow.core.utils.ExtractUtils;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.Grammer;
import org.spiderflow.model.SpiderNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * SQL执行器
 *
 * @author jmxd
 */
@Component
public class ExecuteSQLFlagExecutor implements ShapeExecutor, Grammerable {

    public static final String DATASOURCE_ID = "datasourceId";

    public static final String SQL = "sql";
    public static final String PK_Name = "pkName";
    public static final String TABLE_Name = "tableName";

    public static final String STATEMENT_TYPE = "statementType";

    public static final String STATEMENT_SELECT = "select";

    public static final String STATEMENT_SELECT_ONE = "selectOne";

    public static final String STATEMENT_SELECT_INT = "selectInt";

    public static final String STATEMENT_INSERT = "insert";

    public static final String STATEMENT_UPDATE = "update";

    public static final String STATEMENT_LAST_FLAG = "lastFLag";

    public static final String STATEMENT_DELETE = "delete";
    public static final String SELECT_RESULT_STREAM = "isStream";
    public static final String STATEMENT_INSERT_PK = "insertofPk";


    @Autowired
    private NodeTaskStatusMapper nodeTaskStatusMapper;


    private static final Logger logger = LoggerFactory.getLogger(ExecuteSQLFlagExecutor.class);

    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) throws IOException {

        String dsId = node.getStringJsonValue(DATASOURCE_ID);
        String pkName = node.getStringJsonValue(PK_Name);
        String tableName = node.getStringJsonValue(TABLE_Name);
        String flowId = (String) variables.get("flowId");

        if (StringUtils.isBlank(dsId)) {
            logger.warn("数据源ID为空！");
        } else if (StringUtils.isBlank(tableName)) {
            logger.warn("tableName为空！");
        } else if (StringUtils.isBlank(pkName)) {
            logger.warn("主键名称为空！");
        } else if (StringUtils.isBlank(flowId)) {
            logger.warn("flowId为空！");
        } else {

            Connection conn = null;
            PreparedStatement preparedStatement = null;
            try {

                if (conn == null || conn.isClosed()) {
                    conn = DataSourceUtils.getDataSource().getConnection();
                }
                conn.setAutoCommit(false);

                preparedStatement = conn.prepareStatement("select * from sp_lock where lock_name = '" + flowId + "' for update");
                preparedStatement.execute();

                Long lastId = nodeTaskStatusMapper.findByFlowId(flowId);

                JdbcTemplate template = new JdbcTemplate(DataSourceUtils.getDataSource(dsId));

                if (lastId == null) {
                    lastId = 0l;
                    NodeTaskStatus nodeTaskStatus = new NodeTaskStatus();
                    nodeTaskStatus.setBiz(tableName);
                    nodeTaskStatus.setFlowId(flowId);
                    nodeTaskStatus.setLastId(lastId);
                    nodeTaskStatus.setCreateDate(new Date());
                    nodeTaskStatusMapper.insert(nodeTaskStatus);
                }

                String sql = "select * from " + tableName + " where " + pkName + " > " + lastId + " limit 1";

                logger.debug("执行sql：{}", sql);
                Map<String, Object> rs = null;
                try {
                    Object[] params = new Object[0];
                    rs = template.queryForMap(sql, params);
                    variables.put("rs", rs);
                } catch (Exception e) {
                    variables.put("rs", null);
                    //logger.error("执行sql出错,异常信息:{}", e.getMessage(), e);
                   // ExceptionUtils.wrapAndThrow(e);
                }

                if (rs != null) {
                    try {
                        Object id = rs.get(pkName);
                        lastId = Long.valueOf(id + "");
                        nodeTaskStatusMapper.updateLastIdByFlowId(flowId, lastId);
                    } catch (Exception e) {
                        logger.error("执行update sql出错,异常信息:{}", e.getMessage(), e);
                        ExceptionUtils.wrapAndThrow(e);
                    }
                }
                conn.commit();
            } catch (Exception e) {
                logger.error("执行sql出错,异常信息:{}", e.getMessage(), e);
                ExceptionUtils.wrapAndThrow(e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                    }
                }

                if (null != preparedStatement) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException ignore) {
                    }
                }
            }

        }
    }


    private List<Object[]> convertParameters(Object[] params, int length) {
        List<Object[]> result = new ArrayList<>(length);
        int size = params.length;
        for (int i = 0; i < length; i++) {
            Object[] parameters = new Object[size];
            for (int j = 0; j < size; j++) {
                parameters[j] = getValue(params[j], i);
            }
            result.add(parameters);
        }
        return result;
    }

    private Object getValue(Object object, int index) {
        if (object == null) {
            return null;
        } else if (object instanceof List) {
            List<?> list = (List<?>) object;
            int size = list.size();
            if (size > 0) {
                return list.get(Math.min(list.size() - 1, index));
            }
        } else if (object.getClass().isArray()) {
            int size = Array.getLength(object);
            if (size > 0) {
                Array.get(object, Math.min(-1, index));
            }
        } else {
            return object;
        }
        return null;
    }

    @Override
    public String supportShape() {
        return "executeSqlFlag";
    }

    @Override
    public List<Grammer> grammers() {
        Grammer grammer = new Grammer();
        grammer.setComment("执行SQL结果2");
        grammer.setFunction("rs");
        grammer.setReturns(Arrays.asList("List<Map<String,Object>>", "int"));
        return Collections.singletonList(grammer);
    }


}
