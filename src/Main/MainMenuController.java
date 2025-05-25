package Main; // Asegúrate de que este sea el paquete correcto

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import Main.User; // Asegúrate de que esta sea la ruta correcta a tu clase User
import Main.MainApp; // Asegúrate de que esta sea la ruta correcta a tu clase MainApp

public class MainMenuController {

    @FXML
    private Label welcomeLabel; // Corresponde al fx:id en el FXML

    private MainApp mainApp;
    private User loggedInUser; // Para almacenar el usuario que ha iniciado sesión

    // Método para inyectar la referencia a MainApp
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    // Método para pasar el usuario logueado al controlador
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (user != null) {
            welcomeLabel.setText("¡Bienvenido, " + user.getUsername() + " (" + user.getRole() + ")!");
        }
    }

    @FXML
    private void handleConfigurarParametros(ActionEvent event) {
        if (mainApp != null && loggedInUser != null && "ADMINISTRADOR".equals(loggedInUser.getRole())) {
            mainApp.showConfiguracionSistema(loggedInUser);
        } else {
            // Esto no debería ocurrir si el login ya validó el rol, pero es buena práctica
            System.out.println("Acceso denegado. Solo administradores pueden configurar parámetros.");
            // Podrías mostrar una alerta al usuario
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScreen(); // Vuelve a la pantalla de login
        }
    }

    // Otros métodos para futuros casos de uso si lo necesitas
    // @FXML
    // private void handleGestionarRoles(ActionEvent event) { ... }
    // ...
}