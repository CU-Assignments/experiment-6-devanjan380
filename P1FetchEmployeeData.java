import java.sql.*;

public class FetchEmployeeData {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://localhost:3306/your_database"; // Replace 'your_database' with the actual database name
        String user = "root"; // Replace with your MySQL username
        String password = "password"; // Replace with your MySQL password

        // JDBC variables
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Step 1: Load MySQL JDBC Driver (Optional for modern JDBC versions)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Step 2: Establish connection
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

            // Step 3: Create and execute query
            stmt = conn.createStatement();
            String query = "SELECT * FROM Employee";
            rs = stmt.executeQuery(query);

            // Step 4: Process and display results
            System.out.println("EmpID\tName\t\tSalary");
            while (rs.next()) {
                int empID = rs.getInt("EmpID");
                String name = rs.getString("Name");
                double salary = rs.getDouble("Salary");

                System.out.println(empID + "\t" + name + "\t" + salary);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Step 5: Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
