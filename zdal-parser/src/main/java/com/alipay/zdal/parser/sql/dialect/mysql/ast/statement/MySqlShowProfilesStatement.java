/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package com.alipay.zdal.parser.sql.dialect.mysql.ast.statement;

import com.alipay.zdal.parser.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author ����
 * @version $Id: MySqlShowProfilesStatement.java, v 0.1 2012-11-17 ����3:37:49 Exp $
 */
public class MySqlShowProfilesStatement extends MySqlStatementImpl {

    private static final long serialVersionUID = 1L;

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
