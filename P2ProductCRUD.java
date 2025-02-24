import java.sql.*;
import java.util.Scanner;

public class P2ProductCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database"; // Replace with your DB name
    private static final String USER = "root"; // Replace with your MySQL username
    private static final String PASSWORD = "password"; // Replace with your MySQL password

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {
             
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load driver

            while (true) {
                System.out.println("\n===== Product Management System =====");
                System.out.println("1. Add Product");
                System.out.println("2. View Products");
                System.out.println("3. Update Product");
                System.out.println("4. Delete Product");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addProduct(conn, scanner);
                        break;
                    case 2:
                        viewProducts(conn);
                        break;
                    case 3:
                        updateProduct(conn, scanner);
                        break;
                    case 4:
                        deleteProduct(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to add a new product
    private static void addProduct(Connection conn, Scanner scanner) {
        System.out.print("Enter Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        
        String query = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Start transaction
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
            
            conn.commit(); // Commit transaction
            System.out.println("Product added successfully!");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error adding product. Transaction rolled back.");
            e.printStackTrace();
        }
    }

    // Method to view all products
    private static void viewProducts(Connection conn) {
        String query = "SELECT * FROM Product";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
             
            System.out.println("\nProductID | ProductName | Price | Quantity");
            while (rs.next()) {
                System.out.println(rs.getInt("ProductID") + " | " +
                                   rs.getString("ProductName") + " | " +
                                   rs.getDouble("Price") + " | " +
                                   rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a product
    private static void updateProduct(Connection conn, Scanner scanner) {
        System.out.print("Enter Product ID to update: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter new Product Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        
        String query = "UPDATE Product SET ProductName=?, Price=?, Quantity=? WHERE ProductID=?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Start transaction
            
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, productId);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Product updated successfully!");
            } else {
                conn.rollback(); // Rollback if no rows updated
                System.out.println("No product found with that ID. Update failed.");
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error updating product. Transaction rolled back.");
            e.printStackTrace();
        }
    }

    // Method to delete a product
    private static void deleteProduct(Connection conn, Scanner scanner) {
        System.out.print("Enter Product ID to delete: ");
        int productId = scanner.nextInt();
        
        String query = "DELETE FROM Product WHERE ProductID=?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false); // Start transaction
            
            pstmt.setInt(1, productId);
            int rows = pstmt.executeUpdate();
            
            if (rows > 0) {
                conn.commit(); // Commit transaction
                System.out.println("Product deleted successfully!");
            } else {
                conn.rollback(); // Rollback if no rows deleted
                System.out.println("No product found with that ID. Deletion failed.");
            }
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.out.println("Error deleting product. Transaction rolled back.");
            e.printStackTrace();
        }
    }
}
