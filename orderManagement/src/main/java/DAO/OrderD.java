package DAO;

import Model.Order1;
import connection.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class OrderD extends AbstractDAO<Order1> {
    /**
     * realizeaza operatiile din metodele abstracte
     * @param type =Order1
     */

    public OrderD(Class<Order1> type) {

        super(type);
    }


    private String createAddQuery(){
        StringBuilder sb=new StringBuilder();
        sb.append(" insert into ");
        sb.append(type.getSimpleName());
        sb.append(" values (?,?,?,?) ");


        return sb.toString();
    }

    private String createUpdateQuery(String id,String quantity){
        StringBuilder sb=new StringBuilder();
        sb.append("update ");
        sb.append(type.getSimpleName());
        sb.append(" set " + quantity + " =? ");
        sb.append(" where " + id + " =? ");

        return sb.toString();
    }


    @Override
    public void add(Order1 order) {
        /**
         * adauga o comanda
         */
        Connection connection=null;
        PreparedStatement prep=null;

        String query=createAddQuery();
        try {
            connection = ConnectionManagement.getConnection();
            prep = connection.prepareStatement(query);
            prep.setInt(1, order.getIdOrder());
            prep.setInt(2, order.getIdOrderClient());
            prep.setInt(3, order.getIdOrderProduct());
            prep.setInt(4, order.getQuantityOrder());
            prep.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: addOrder" + e.getMessage());
        } finally {
            ConnectionManagement.close(prep);
            ConnectionManagement.close(connection);
        }
    }

    @Override
    public void update(Order1 order) {
        /**
         * schimba cantitatea comenzii cu id la fel
         */
        Connection connection=null;
        PreparedStatement statement=null;

        String query=createUpdateQuery("idOrder","quantityProduct");

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setInt(1,order.getQuantityOrder());
            statement.setInt(2,order.getIdOrder());
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,type.getName()+" DAO:updateOrder "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
        }
    }

    @Override
    public void remove(String id) {
/**
 * nu avem nevoie,deci nu am mai creat coprul metodei
 */
    }


    public List<Order1> createList(ResultSet result) throws SQLException {
        /**
         * formeaza lista de Comenzi in functie de rezultatul executiei query-ului
         */
        List<Order1> list=new ArrayList<Order1>();

        while (result.next()) {
            int id = result.getInt("idOrder");
            int idC=result.getInt("idOrderClient");
            int idP=result.getInt("idOrderProduct");
            int quantity=result.getInt("quantityOrder");
            list.add(new Order1(id, idC,idP,quantity));
        }
        return list;
    }

    public List<Order1> findAll() {
        /**
         * retruneza toate comezile stocate in baza de data
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result =null;

        String query=createList();

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            result=statement.executeQuery();
            return createList(result);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:list " + e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(result);
            ConnectionManagement.close(statement);
        }
        return null;
    }

    public Order1 findById(int id) {
        /**
         * returneaza Comanda cu id-ul la fel
         */
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet= null;
        String query = createSelectQuery("idOrder");
        try {
            connection = ConnectionManagement.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next())
                return new Order1(resultSet.getInt("idOrder"),resultSet.getInt("idOrderClient"),resultSet.getInt("idOrderProduct"),resultSet.getInt("quantityOrder"));
            else
                return null;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionManagement.close(resultSet);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(connection);
        }
        return null;
    }
}
