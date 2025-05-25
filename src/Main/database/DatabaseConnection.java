package Main.database; // O el paquete donde estés colocando tus clases de base de datos

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Tu string de conexión completo de Neon
    private static final String JDBC_URL = "jdbc:postgresql://ep-still-wave-a5u4z8tk-pooler.us-east-2.aws.neon.tech/tickets?user=tickets_owner&password=npg_Ds1TrEYPAMl9&sslmode=require";

    /**
     * Establece y retorna una nueva conexión a la base de datos.
     * @return Objeto Connection si la conexión es exitosa.
     * @throws SQLException Si ocurre un error al intentar conectar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Cargar el driver de PostgreSQL (a menudo ya no es estrictamente necesario en JDK modernos
            // y con el driver en el classpath, pero no está de más dejarlo para compatibilidad).
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver de PostgreSQL JDBC no encontrado.");
            e.printStackTrace();
            throw new SQLException("Driver de base de datos no disponible.", e);
        }
        // Usamos el DriverManager para obtener la conexión directamente con el URL completo
        return DriverManager.getConnection(JDBC_URL);
    }

    /**
     * Cierra la conexión a la base de datos.
     * @param connection La conexión a cerrar.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}