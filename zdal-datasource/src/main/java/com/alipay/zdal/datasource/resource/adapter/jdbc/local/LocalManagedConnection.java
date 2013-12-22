/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.alipay.zdal.datasource.resource.adapter.jdbc.local;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.transaction.xa.XAResource;

import org.apache.log4j.Logger;

import com.alipay.zdal.datasource.resource.JBossResourceException;
import com.alipay.zdal.datasource.resource.ResourceException;
import com.alipay.zdal.datasource.resource.adapter.jdbc.BaseWrapperManagedConnection;
import com.alipay.zdal.datasource.resource.spi.LocalTransaction;

/**
 * LocalManagedConnection
 *
 * @author <a href="mailto:d_jencks@users.sourceforge.net">David Jencks</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 57189 $
 */
public class LocalManagedConnection extends BaseWrapperManagedConnection implements
                                                                        LocalTransaction {
    private static final Logger logger = Logger.getLogger(LocalManagedConnection.class);

    /**
     * @param mcf
     * @param con
     * @param props
     * @param transactionIsolation
     * @param psCacheSize
     * @throws SQLException
     */
    public LocalManagedConnection(final LocalManagedConnectionFactory mcf, final Connection con,
                                  final Properties props, final int transactionIsolation,
                                  final int psCacheSize) throws SQLException {
        super(mcf, con, props, transactionIsolation, psCacheSize);
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        return this;
    }

    public XAResource getXAResource() throws ResourceException {
        throw new JBossResourceException("Local tx only!");
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#commit()
     */
    public void commit() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                inManagedTransaction = false;
        }
        try {
            con.commit();
        } catch (SQLException e) {
            checkException(e);
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#rollback()
     */
    public void rollback() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction)
                inManagedTransaction = false;
        }
        try {
            con.rollback();
        } catch (SQLException e) {
            try {
                checkException(e);
            } catch (Exception e2) {
                logger.error(e2);
            }
        }
    }

    /** 
     * @see com.alipay.zdal.datasource.resource.spi.LocalTransaction#begin()
     */
    public void begin() throws ResourceException {
        synchronized (stateLock) {
            if (inManagedTransaction == false) {
                try {
                    if (underlyingAutoCommit) {
                        underlyingAutoCommit = false;
                        con.setAutoCommit(false);
                    }
                    checkState();
                    inManagedTransaction = true;
                } catch (SQLException e) {
                    checkException(e);
                }
            } else
                throw new JBossResourceException("Trying to begin a nested local tx");
        }
    }

    Properties getProps() {
        return props;
    }
}
