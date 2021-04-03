package DAO;

import Model.Products;
import connection.ConnectionManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class ProductD extends AbstractDAO<Products> {
    /**
     * adauga corpul metodelor abstracte si are inca doua alte metode
     * @param type=Products
     */

    public ProductD(Class<Products> type) {
        super(type);
    }
    private String createAddQuery(){
        StringBuilder sb=new StringBuilder();
        sb.append("insert ");
        sb.append(" into ");
        sb.append(type.getSimpleName());
        sb.append(" values ");
        sb.append("( ?,");
        sb.append(" ?,");
        sb.append(" ?,");
        sb.append(" ?)");
        return sb.toString();
    }

    private String createUpdateQuery(String id,String quantity){
        StringBuilder sb=new StringBuilder();
        sb.append("update ");
        sb.append(type.getSimpleName());
        sb.append(" set " + quantity + " =?");
        sb.append(" where " + id + " =?");

        return sb.toString();
    }
    private String createQuantity(String quantity, String nameProd){
        StringBuilder sb= new StringBuilder();
        sb.append("select ");
        sb.append(" sum("+quantity+") ");
        sb.append(" from "+type.getSimpleName());
        sb.append(" where "+nameProd+" =?");

        return sb.toString();
    }
    @Override
    public void add(Products products) {
        /**
         * adauga un nou produs
         */
        Connection connection=null;
        PreparedStatement prep=null;
        ResultSet res=null;
        String query=createAddQuery();
                try {
                    connection = ConnectionManagement.getConnection();
                    prep = connection.prepareStatement(query);
                    prep.setInt(1, products.getIdProduct());
                    prep.setString(2, products.getNameProduct());
                    prep.setInt(3, products.getQuantity());
                    prep.setDouble(4, products.getPrice());
                    prep.executeUpdate();

                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, type.getName() + "DAO: addProduct " + e.getMessage());
                } finally {
                    ConnectionManagement.close(res);
                    ConnectionManagement.close(prep);
                    ConnectionManagement.close(connection);
                }
            }

            @Override
            public void update(Products p) {
                /**
                 * schimba cantitatea
                 */
        Connection connection=null;
        PreparedStatement statement=null;
        String query=createUpdateQuery("idProducts","quantity");
        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setInt(1,p.getQuantity());
            statement.setInt(2,p.getIdProduct());
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,type.getName()+" DAO:updateProduct "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
        }
    }
    @Override
    public void remove(String name) {
        /**
         * sterge produsul
         */
        Connection connection=null;
        PreparedStatement statement=null;
        String query=crateRemoveQuery("nameProduct");
        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setString(1,name);
            statement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName()+" DAO:removeProduct "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
        }
    }
    public int quantityProd(String name){
        /**
         * returneaza cantitatea produsului
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result=null;
        String query=createQuantity("quantity", "nameProduct");
        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setString(1,name);
            result=statement.executeQuery();
            result.next();
            return result.getInt("sum(quantity)");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName()+" DAO:quantityProduct "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(result);
        }
        return 0;
    }

    public List<Products> createList(ResultSet result) throws SQLException {
        /**
         * creaza lista de produse in urma executari query-ului
         */
        List<Products> list=new ArrayList<Products>();
        while (result.next()){
            int id=result.getInt("idProducts");
            String name=result.getString("nameProduct");
            int quantity=result.getInt("quantity");
            double price=result.getDouble("price");
            list.add(new Products(id,name,quantity,price));
        }
        return list;
    }

    public Products findById(int id){
        /**
         * returneaza prodului cu id ==id iar daca nu null
         */
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet= null;
        Products p=null;
        String query = createSelectQuery("idProducts");
        try {
            connection = ConnectionManagement.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next())
                return new Products(resultSet.getInt("idProducts"), resultSet.getString("nameProduct"), resultSet.getInt("quantity"), resultSet.getDouble("price"));
            else
                return null;

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionManagement.close(resultSet);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(connection);
        }
        return p;
    }

    public List<Products> findAll() {
        /**
         * returneaza toate produsele
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result =null;
        String query=createList();
        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            result=statement.executeQuery();
            List<Products> list= createList(result);
            return list;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:listProduct " + e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(result);
            ConnectionManagement.close(statement);
        }
        return null;
    }
    public String findByNameQuery(){
        StringBuilder sb= new StringBuilder();
        sb.append("select ");
        sb.append(" * ");
        sb.append(" from products ");
        sb.append( " where nameProduct =? and price=?");
        return sb.toString();
    }

    public Products findByName(String name, double price) {
        /**
         * returneaza produsul cu acelasi pret si nume
         */
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        String query = findByNameQuery();
        try {
            connection = ConnectionManagement.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setDouble(2,price);
            result = statement.executeQuery();
            if (result.next())
                return new Products(result.getInt("idProducts"), result.getString("nameProduct"), result.getInt("quantity"), result.getDouble("price"));
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

