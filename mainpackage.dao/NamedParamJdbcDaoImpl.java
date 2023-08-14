package mainpackage.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import mainpackage.company.CompanyConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class NamedParamJdbcDaoImpl extends NamedParameterJdbcDaoSupport {
	
	public void loadAll() {
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

	    // Step 1: Retrieve all names from the source table
	    List<String> names = jdbcTemplate.queryForList("SELECT NAMEANDSURNAME FROM EXINTERN", String.class);

	    // Step 2: Delete all rows from the source table
	    jdbcTemplate.update("DELETE FROM EXINTERN");

	    // Step 3: Insert the retrieved names into the destination table
	    for (String name : names) {
	        jdbcTemplate.update("INSERT INTO INTERN (NAMEANDSURNAME) VALUES (?)", name);
	    }
	}

    public List<String> removeSelectedRows(String[] nameandsurnamesToRemove) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String deleteQuery = "DELETE FROM INTERN WHERE NAMEANDSURNAME = ?";
        String insertQuery = "INSERT INTO EXINTERN (NAMEANDSURNAME) VALUES (?)";
        List<String> l = new ArrayList<>();
        
        for (String nameandsurname : nameandsurnamesToRemove) {
            int affectedRows = jdbcTemplate.update(deleteQuery, nameandsurname);
            
            if (affectedRows > 0) {
                int IANUM = CompanyConfig.getIANUM();
                l.add("Intern " + nameandsurname + " is removed from table with internship application num " + IANUM);
                CompanyConfig.incrementAndSaveIANUMInDatabase(jdbcTemplate);
                
                jdbcTemplate.update(insertQuery, nameandsurname);
            }
        }
        return l;
    }
    
    public void fillInternTableFromCsv(String csvFilePath) {
        String tableName = "INTERN";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());

            while ((line = br.readLine()) != null) {
                
                String fieldValue = line.trim();

                String sql = "INSERT INTO " + tableName + " (NAMEANDSURNAME) VALUES (:fieldValue)";

                MapSqlParameterSource paramSource = new MapSqlParameterSource();
                paramSource.addValue("fieldValue", fieldValue);

                jdbcTemplate.update(sql, paramSource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> retrieveInternNandS() {
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
    	String sql = "SELECT NAMEANDSURNAME FROM INTERN";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public void fillTableFromCsv(String csvFilePath) {
        String tableName = "CUSTOMER";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());

            while ((line = br.readLine()) != null) {
                
                String fieldValue = line.trim();

                String sql = "INSERT INTO " + tableName + " (NAMEANDSURNAME) VALUES (:fieldValue)";

                MapSqlParameterSource paramSource = new MapSqlParameterSource();
                paramSource.addValue("fieldValue", fieldValue);

                jdbcTemplate.update(sql, paramSource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteCustomersUsingCsv(String csvFilePath) {
        String tableName = "CUSTOMER";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());

            while ((line = br.readLine()) != null) {
                String fieldValue = line.trim();

                String sql = "DELETE FROM " + tableName + " WHERE NAMEANDSURNAME = :fieldValue";

                MapSqlParameterSource paramSource = new MapSqlParameterSource();
                paramSource.addValue("fieldValue", fieldValue);

                jdbcTemplate.update(sql, paramSource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<String> retrieveNamesAndSurnames() {
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
    	String sql = "SELECT NAMEANDSURNAME FROM CUSTOMER";
        return jdbcTemplate.queryForList(sql, String.class);
    }
    
    public void dropCustomerTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "DROP TABLE CUSTOMER";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error dropping customer table: " + e.getMessage());
        }
    }
    
    public void createCustomerTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "CREATE TABLE CUSTOMER (NAMEANDSURNAME VARCHAR(50))";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error creating customer table: " + e.getMessage());
        }
    }
    
    public void createInternTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "CREATE TABLE INTERN (NAMEANDSURNAME VARCHAR(50))";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error creating intern table: " + e.getMessage());
        }
    }
    
    public void createExInternTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "CREATE TABLE EXINTERN (NAMEANDSURNAME VARCHAR(50))";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error creating expired intern table: " + e.getMessage());
        }
    }
    
    public void dropInternTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "DROP TABLE INTERN";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error dropping intern table: " + e.getMessage());
        }
    }
    
    public void dropExInternTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "DROP TABLE EXINTERN";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error dropping expired intern table: " + e.getMessage());
        }
    }
    
    public void createCompanyConfigTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "CREATE TABLE COMPANYCONFIG (IANUM INTEGER)";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error creating company configuration table: " + e.getMessage());
        }
    }
    
    public void dropCompanyConfigTable() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "DROP TABLE COMPANYCONFIG";
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException e) {
            System.err.println("Error dropping company configuration table: " + e.getMessage());
        }
    }
    
    public void changeIANUM(int num) {
    	JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
    	CompanyConfig.changeIANUMInDatabase(jdbcTemplate, num);
    }
}