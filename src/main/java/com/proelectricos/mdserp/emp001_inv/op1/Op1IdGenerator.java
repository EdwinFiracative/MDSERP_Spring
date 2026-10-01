package com.proelectricos.mdserp.emp001_inv.op1;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.*;

public class Op1IdGenerator implements IdentifierGenerator {


    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {

            Serializable result = null;
            Connection connection = null;
            Statement statement = null;
            ResultSet resultSet = null;


            String prefix = "emp";
            try {
                connection = session.getJdbcConnectionAccess().obtainConnection();
                statement = connection.createStatement();
                try {
                    /*
                     * uncomment below line if you are using mysql and the sequence DOES NOT EXIST.
                     * As we all know MySql does not support sequence, instead there is AUTO INCREMENT
                     * if you are using other databases change SQL according to that
                     * e.g. Oracle: "SELECT "+sequenceName+".NEXTVAL FROM DUAL"
                     * PostgreSQL: "SELECT  NEXTVAL('+sequenceName+"')
                     * */
                    //statement.executeUpdate("UPDATE " + DEFAULT_SEQUENCE_NAME + " SET next_val=LAST_INSERT_ID(next_val+1)");


                    resultSet = statement.executeQuery("SELECT max(id) FROM emp001_inv.dbo.op1" );
                } catch (Exception e) {

                    System.out.println("In catch, cause : Table is not available.");
                    // if sequence is not found then creating the sequence
                    // Below code is for MySql database you change according to your database

                }
                if (resultSet.next()) {

                    int nextValue = resultSet.getInt(1);
                    String suffix = String.format("%04d", nextValue);
                    result = nextValue+1;
                    try {
                        PreparedStatement st = connection.prepareStatement("UPDATE emp001_inv.dbo.ids SET id = ? WHERE tabla = 'OP1'");
                    st.setInt(1, nextValue+1);
                    st.executeUpdate();
                    } catch (SQLException e ) {
                        e.printStackTrace();
                    }


                    System.out.println("Custom generated sequence is : " + result);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

