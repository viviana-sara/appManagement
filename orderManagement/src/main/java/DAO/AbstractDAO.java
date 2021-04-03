package DAO;

import connection.ConnectionManagement;

import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO<T> {
    /**
     * este o clasa abstracta care contine operatiile pe care dorim sa le efectuam la tebele
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    protected final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO(Class<T> type) {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    protected String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    protected String createList(){
        StringBuilder sb=new StringBuilder();
        sb.append("select ");
        sb.append("* ");
        sb.append(" from ");
        sb.append(type.getSimpleName());

        return sb.toString();
    }

    protected String createMaxIdQuery(String id){
        StringBuilder sb= new StringBuilder();
        sb.append(" select ");
        sb.append(" max("+id+")  AS max ");
        sb.append(" from " );
        sb.append(type.getSimpleName());

        return sb.toString();
    }

    protected String createCountIdQuery(String id){
        StringBuilder sb= new StringBuilder();
        sb.append(" select ");
        sb.append(" count("+id+") AS count ");
        sb.append(" from " );
        sb.append(type.getSimpleName());

        return sb.toString();
    }
    protected String crateRemoveQuery(String name){
        StringBuilder sb=new StringBuilder();
        sb.append("delete from ");
        sb.append(type.getSimpleName());
        sb.append(" where " + name+ " = ?");

        return sb.toString();
    }

    public int getMaxId(String id) {
        /**
         * returneaza cel mai mare id, ca sa putem introduce pe urmatorul
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result=null;

        String query=createMaxIdQuery(id);

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            result=statement.executeQuery();
            result.next();

            return result.getInt("max");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName()+" DAO:getMaxId "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(result);
        }
        return 0;
    }


    public int getCount(String id) {
        /**
         * returneaza numarul de elemente din tabel, ca sa stim daca tabelul e gol sau nu
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result=null;

        String query=createCountIdQuery(id);

        try{
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            result=statement.executeQuery();
            result.next();

            return result.getInt("count");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName()+" DAO:getCount "+ e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(result);
        }
        return 0;
    }

    public String getIdOfNameQuery(String id, String name){
        StringBuilder sb=new StringBuilder();
        sb.append(" select ");
        sb.append(id);
        sb.append(" from ");
        sb.append(type.getSimpleName());
        sb.append(" where "+ name+" =?");

        return sb.toString();
    }

    public int getIdOfName(String name, String id, String nameOfCol){
        /**
         * aceasta metoda o folosim mai mult Order ca sa aflam id unui produs sau a unui client, stiind doar numele
         */
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet result=null;

        String query=getIdOfNameQuery(id,nameOfCol);
        try {
            connection=ConnectionManagement.getConnection();
            statement=connection.prepareStatement(query);
            statement.setString(1,name);
            result=statement.executeQuery();

            if(result.next())
                return result.getInt(id);
            else
                return -1;


        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " DAO:getCount " + e.getMessage());
        }
        finally {
            ConnectionManagement.close(connection);
            ConnectionManagement.close(statement);
            ConnectionManagement.close(result);
        }
        return -1;
    }

    public abstract List<T> createList(ResultSet result) throws SQLException;
    public abstract List<T> findAll();
    public abstract T findById(int id);
    public abstract void add(T t);
    public abstract void update(T t);
    public abstract void remove(String id);

}
