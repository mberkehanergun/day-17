package mainpackage.company;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class CompanyConfig {
    private static int IANUM = 1;

    public static int getIANUM() {
        return IANUM;
    }

    public static void incrementAndSaveIANUMInDatabase(JdbcTemplate jdbcTemplate) {
        try {
            String updateQuery = "UPDATE COMPANYCONFIG SET IANUM = ?";
            jdbcTemplate.update(updateQuery, ++IANUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeIANUMInDatabase(JdbcTemplate jdbcTemplate, int newValue) {
        try {
        	IANUM = newValue;
            String updateQuery = "UPDATE COMPANYCONFIG SET IANUM = ?";
            jdbcTemplate.update(updateQuery, IANUM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}