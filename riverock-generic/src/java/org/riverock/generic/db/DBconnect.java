/*

 * org.riverock.generic -- Database connectivity classes

 * 

 * Copyright (C) 2004, Riverock Software, All Rights Reserved.

 * 

 * Riverock -- The Open-source Java Development Community

 * http://www.riverock.org

 * 

 * 

 * This library is free software; you can redistribute it and/or

 * modify it under the terms of the GNU Lesser General Public

 * License as published by the Free Software Foundation; either

 * version 2.1 of the License, or (at your option) any later version.

 *

 * This library is distributed in the hope that it will be useful,

 * but WITHOUT ANY WARRANTY; without even the implied warranty of

 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU

 * Lesser General Public License for more details.

 *

 * You should have received a copy of the GNU Lesser General Public

 * License along with this library; if not, write to the Free Software

 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 *

 */



/**

 *  ласс содержащий описани€ методов дл€ работы с базой данных.

 *

 * $Revision$ $Date$

 */

package org.riverock.generic.db;



import java.sql.SQLException;



import org.riverock.common.tools.MainTools;

import org.riverock.generic.db.definition.DefinitionService;

import org.riverock.generic.schema.config.DatabaseConnectionType;

import org.riverock.generic.config.GenericConfig;

import org.riverock.generic.exception.DatabaseException;

import org.riverock.common.config.ConfigException;



import org.apache.log4j.Logger;



public abstract class DBconnect extends DatabaseAdapter

{

    private static Logger log = Logger.getLogger("org.riverock.generic.db.DBconnect");



    public DBconnect(){}



    protected static DBconnect openDynamicConnect(String connectionName)

        throws DatabaseException, ConfigException

    {

        DBconnect db_ = null;



        String connName = getDBconnectClassName(connectionName);

        try

        {

            if (log.isDebugEnabled())

            {

                System.out.println("Call for create dynamic object " + connName);

                log.debug("Call for create dynamic object " + connName);

            }



            db_ = (DBconnect) MainTools.createCustomObject(connName);

            db_.init(GenericConfig.getDatabaseConnection(connectionName));

            db_.conn.setAutoCommit(false);

            db_.isDBOk = true;

            db_.isDynamicConnect = true;



            if (log.isDebugEnabled())

            {

                System.out.println("Success create dynamic object " + connName);

                log.debug("Success create dynamic object " + connName);

            }

        }

        catch (Exception e)

        {

            if (db_ != null && db_.conn != null)

            {

                try

                {

                    db_.conn.close();

                    db_.conn = null;

                }

                catch (Exception e02)

                {

                }

            }

            db_ = null;



            log.fatal("Error create instance for class " + connName);

            log.fatal("ConnectionName - " + connectionName);

            log.fatal("Error:", e);



            System.out.println("\nError create instance for class " + connName + "\nSee log for details");

            throw new DatabaseException("Exception get connection "+e.toString());

        }

        return db_;

    }



    private static DBconnect openConnectPrivate(String connectionName )

            throws DatabaseException, ConfigException

    {

        String connName = getDBconnectClassName(connectionName);



        synchronized (initFlag)

        {



            DBconnect _db_ = null;

            if (!initFlag.booleanValue())

            {

                initFlag = new Boolean(true);

                try

                {



                    for (int i = 0; i < 3; i++)

                    {

                        try

                        {

                            if (log.isDebugEnabled())

                            {

                                System.out.println("Call for create static object " + connName);

                                log.debug("Call for create static object " + connName);

                            }



                            _db_ = (DBconnect) MainTools.createCustomObject(connName);

                            _db_.init(GenericConfig.getDatabaseConnection(connectionName));

                            _db_.isDBOk = true;

                            _db_.isDynamicConnect = false;



                            if (log.isDebugEnabled())

                            {

                                System.out.println("Success create static object " + connName);

                                log.debug("Success create static object " + connName);

                            }

                            break;

                        }

                        catch (Exception e)

                        {

                            log.fatal("Error create instance for class " + connName);

                            log.fatal("ConnectionName - " + connectionName);

                            log.fatal("Error:", e);



                            System.out.println("\nError create instance for class " + connName + "\nSee log for details");

                            System.out.println("\nconnectionName - " + connectionName);

                            if (_db_ != null && _db_.conn != null)

                            {

                                try

                                {

                                    _db_.conn.close();

                                    _db_.conn = null;

                                }

                                catch (Exception e02){}

                            }

                            _db_ = null;

                        }

                    }

                } // try

                finally

                {

                    initFlag = new Boolean(false);

                }

            } // if (!initFlag)



            if (_db_!=null && isNeedValidateStructure)

            {

                try

                {

                    DefinitionService.validateDatabaseStructure( _db_ );

                }

                catch (Exception exception)

                {

                    log.error("Exception in ", exception);

                    throw new DatabaseException("Exception applay new definition"+exception.toString());

                }

            }



            return _db_;

        } // synchronized (initFlag)

    }



    protected synchronized static DBconnect openConnect(String connectionName)

        throws DatabaseException, ConfigException

    {

        if (connectionName==null)

        {

            log.error("Call DatabaseAdapter.openConnect(String connectionName) with connectionName==null");

            return null;

        }



        DBconnect __db__ = (DBconnect)connectHashtable.get(connectionName);



        boolean isNew = true;

        if (__db__ == null)

        {

            __db__ = openConnectPrivate(connectionName);

        }

        else

        {

            try

            {

                // провер€ем что данный коннект к базе не закрыт

                // если закрыт, то удал€ем текущий объект и создаем новый

                if (__db__.getIsClosed())

                {

                    try

                    {

                        __db__.conn.close();

                        __db__.conn = null;

                        __db__ = null;

                    }

                    catch (Exception e01){}



                    __db__ = openConnectPrivate(connectionName);

                }

                else

                    isNew = false;

            }

            catch (SQLException e1)

            {

                connectHashtable.remove( connectionName );

                if (__db__ != null && __db__.conn != null)

                {

                    try

                    {

                        __db__.conn.close();

                        __db__.conn = null;

                    }

                    catch (Exception e01){}

                }

                __db__ = null;

            }

        }



        if (isNew && __db__ != null)

            connectHashtable.put(connectionName, __db__);



        return __db__;

    }



    /**

     * —оздает новый коннект к базе данных. ≈сли параметер getIsCssDynamic == true, то открываетс€

     * новый коннект к базе. »наче используетс€ статический коннект. ѕри использовании

     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

     * ƒл€ создани€ коннекта используютс€ данные из DatabaseConnection определенные по умолчанию

     * @param isDynamic - boolean. Ќужно создать динамический коннект или статический.

     * @return - DatabaseAdapter

     * @see DatabaseConnectionType

     */

//    public static DatabaseAdapter getInstance(boolean isDynamic)

//        throws DatabaseException, ConfigException

//    {

//        return getInstance(isDynamic, GenericConfig.getDefaultConnectionName());

//    }



    /**

     * —оздает новый коннект к базе данных. ≈сли параметер getIsCssDynamic == true, то открываетс€

     * новый коннект к базе. »наче используетс€ статический коннект. ѕри использовании

     * статического коннекта, jdbc драйвер должен создавать треад-сейв коннекты.

     * ƒл€ создани€ коннекта используютс€ данные из @link DatabaseConnection.

     * @param isDynamic - boolean. Ќужно создать динамический коннект или статический.

     * @param connectionName - String. »м€ данных конфигурации коннекта к базе.

     * @return - DatabaseAdapter

     */

//    public static DatabaseAdapter getInstance(boolean isDynamic, String connectionName)

//        throws DatabaseException, ConfigException

//    {

//        if (isDynamic)

//            return openDynamicConnect(connectionName);

//        else

//            return openConnect(connectionName);

//    }

}

