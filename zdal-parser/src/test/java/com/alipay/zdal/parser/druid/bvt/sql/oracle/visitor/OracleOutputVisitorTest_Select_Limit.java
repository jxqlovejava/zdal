package com.alipay.zdal.parser.druid.bvt.sql.oracle.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alipay.zdal.parser.sql.ast.SQLStatement;
import com.alipay.zdal.parser.sql.dialect.oracle.parser.OracleStatementParser;
import com.alipay.zdal.parser.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alipay.zdal.parser.visitor.ZdalOracleSchemaStatVisitor;

/**
 * 
 * @author xiaoqing.zhouxq
 * @version $Id: OracleOutputVisitorTest_orderBy.java, v 0.1 2012-5-17 ����10:23:06 xiaoqing.zhouxq Exp $
 */
public class OracleOutputVisitorTest_Select_Limit extends TestCase {

    public void test_0() throws Exception {
        String sql = "select id, name from users where id>? and name=? and ROWNUM < 10";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        ZdalOracleSchemaStatVisitor visitor = new ZdalOracleSchemaStatVisitor();
        stmt.accept(visitor);

        System.out.println(sql);
        System.out.println("Tables : " + visitor.getTables());
        System.out.println("fields : " + visitor.getColumns());
        System.out.println("alias : " + visitor.getAliasMap());
        System.out.println("conditions : " + visitor.getConditions());
        System.out.println("orderBy : " + visitor.getOrderByColumns());
        System.out.println("groupBy : " + visitor.getGroupByColumns());
        System.out.println("variant : " + visitor.getVariants());
        System.out.println("relationShip : " + visitor.getRelationships());
        System.out.println("bindColumns : " + visitor.getBindVarConditions());
        System.out.println("rownums : " + visitor.getRownums());
        System.out.println("--------------------------------");

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("users"));

        Assert.assertEquals(2, visitor.getColumns().size());

        StringBuilder buf = new StringBuilder();
        OracleOutputVisitor outputVisitor = new OracleOutputVisitor(buf);
        stmt.accept(outputVisitor);

    }
}
