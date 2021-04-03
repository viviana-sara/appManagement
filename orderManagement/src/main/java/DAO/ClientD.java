package DAO;

import Model.Client;
import connection.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ClientD extends AbstractDAO<Client> {
    /**
     * realizeaza operatiile abstracte din AbstractDAO plus inca una
     * @param type primeste numele clasei Client
     */

    public ClientD(Class<Client> type) {
        super(type);
    }

    private String createAddQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(type.getSimpleName());
        sb.append(" values (?,?,?) ");


        return sb.toString();
    }

    public void add(Client t) {
        /**
         * adauga un clinet nou
         */
        Connection connection = null;
        PreparedStatement prep = null;
        String query = createAddQuery();

        try {
            connection = ConnectionManagement.getConnection();
            prep = connection.prepareStatement(query);
            prep.setInt(1, t.getIdClient());
            prep.setString(2, t.getNameClient());
            prep.setString(3, t.getAddress());
            prep.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: addClient" + e.getMessage());
        } finally {
            ConnectionManagement.close(prep);
            ConnectionManagement.close(connection);
        }
    }



    private String createUpdateQuery(String id,String name,String address) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ");
        sb.append(type.getSimpleName());
        sb.append(" set " + name + " =?, " + address + " =? ");
        sb.append(" where " + id + " =? ");

        return sb.toString();
    }

    public void update(Client t){
        /**
         * schimba numele si adresa clientului cu id-ul la fel
         */
        Connection connection=null;
        PreparedStatement statement=null;

        String query=createUpdateQuery("idClient","nameClient", "address");

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setInt(1,t.getIdClient());
            statement.setString(2,t.getNameClient());
            statement.setString(3,t.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,type.getName()+" DAO:updateClient "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
        }
    }

    public void remove(String name){
        /**
         * sterge clientul
         */
        Connection connection=null;
        PreparedStatement statement=null;
        String query=crateRemoveQuery("nameClient");
        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setString(1,name);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName()+" DAO:remove "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
        }
    }


    public List<Client> createList(ResultSet result) throws SQLException {
        /**
         * creeaza clientii in funtie de rezultatul obtinut in urma executarii query-ului.
         */
        List<Client> list=new ArrayList<Client>();
        while (result.next()) {
            int id = result.getInt("idClient");
            String name = result.getString("nameClient");
            String address = result.getString("address");
            list.add(new Client(id, name, address));

        }
        return list;
    }

    public List<Client> findAll() {
        /**
         * returneaza toti clienti stocati in tabela
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result =null;

        String query=createList();

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            result=statement.executeQuery();
            List<Client> list= createList(result);
            return list;

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

    public Client findById(int id) {
        /**
         * cauta clientul in funtie de id
         */
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet= null;
        String query = createSelectQuery("idClient");
        try {
            connection = ConnectionManagement.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if(resultSet.next())
                return new Client(resultSet.getInt("idClient"),resultSet.getString("nameClient"),resultSet.getString("address"));
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



    public Client findByName(String name) {
        /**
         * cauta clientul in funtie de nume
         */
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = createSelectQuery("nameClient");
        try {
            connection = ConnectionManagement.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            result = statement.executeQuery();
            if (result.next())
                return new Client(result.getInt("idClient"),result.getString("nameClient"),result.getString("address"));
            else
                return null;

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:listFindByName " + e.getMessage());
        } finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(result);
            ConnectionManagement.close(statement);
        }
        return null;
    }

}
