package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane; // Importa el tipo de layout que usaste en FXMLs
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Tickets de Servicio");

        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login.fxml"));
            AnchorPane loginLayout = loader.load();

            Scene scene = new Scene(loginLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            LoginController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la pantalla de login: " + e.getMessage());
        }
    }

    public void showMainMenu(User loggedInUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("main-menu-view.fxml"));
            AnchorPane mainMenuLayout = loader.load();

            Scene scene = new Scene(mainMenuLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistema de Tickets de Servicio - Menú Principal");
            primaryStage.setResizable(true);

            MainMenuController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedInUser(loggedInUser);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la pantalla del menú principal: " + e.getMessage());
        }
    }

    // Método para mostrar la pantalla de configuración del sistema
    public void showConfiguracionSistema(User adminUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("configuracion-sistema-view.fxml"));
            AnchorPane configuracionLayout = loader.load();

            Scene scene = new Scene(configuracionLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistema de Tickets de Servicio - Configuración");

            ConfiguracionSistemaController controller = loader.getController();
            controller.setMainApp(this);
            controller.setAdminUser(adminUser); // Pasamos el usuario administrador al controlador

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la pantalla de configuración del sistema: " + e.getMessage());
        }
    }
    
    // Nuevo método para mostrar la pantalla de gestión de roles y permisos
    public void showGestionRolesPermisos(User adminUser) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gestion-roles-permisos-view.fxml"));
            AnchorPane gestionLayout = loader.load();

            Scene scene = new Scene(gestionLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Sistema de Tickets de Servicio - Gestión de Roles y Permisos");

            GestionRolesPermisosController controller = loader.getController();
            controller.setMainApp(this);
            controller.setLoggedInUser(adminUser); // Pasa el usuario administrador

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error al cargar la pantalla de gestión de roles y permisos: " + e.getMessage());
        }
    }

    // ****** ESTE MÉTODO ES NECESARIO PARA EL FILECHOOSER ******
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    // *********************************************************

    public static void main(String[] args) {
        launch();
    }
}