package Main; // Asegúrate de que este sea el paquete correcto de tu LoginController

import Main.User;
import Main.AuthService;
import Main.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorMessageLabel;

    private MainApp mainApp;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            User authenticatedUser = AuthService.authenticate(username, password);

            // Si la autenticación es exitosa, siempre vamos al menú principal
            if (mainApp != null) {
                // Pasamos el usuario autenticado al método que muestra el menú principal
                mainApp.showMainMenu(authenticatedUser);
            }

        } catch (IllegalArgumentException e) {
            errorMessageLabel.setTextFill(Color.RED);
            errorMessageLabel.setText(e.getMessage());
        } catch (Exception e) {
            errorMessageLabel.setTextFill(Color.RED);
            errorMessageLabel.setText("Ocurrió un error inesperado. Intente de nuevo.");
            e.printStackTrace();
        }
    }
}