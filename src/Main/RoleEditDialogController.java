package Main; // O Main.controller.dialog;

import Main.model.Role;
import Main.service.RoleService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RoleEditDialogController {

    @FXML private TextField roleNameField;
    @FXML private TextArea roleDescriptionArea;
    @FXML private Label messageLabel;

    private Stage dialogStage;
    private Role role; // El rol a editar (null si es nuevo)
    private boolean okClicked = false;
    private RoleService roleService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            roleNameField.setText(role.getName());
            roleDescriptionArea.setText(role.getDescription());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        String name = roleNameField.getText();
        String description = roleDescriptionArea.getText();

        try {
            if (role == null) { // Crear nuevo rol
                role = new Role(name, description);
                roleService.createRole(role);
            } else { // Modificar rol existente
                role.setName(name);
                role.setDescription(description);
                roleService.updateRole(role);
            }
            okClicked = true;
            dialogStage.close();
        } catch (IllegalArgumentException e) { // Validación de datos [cite: 20]
            messageLabel.setText(e.getMessage());
            messageLabel.setTextFill(Color.RED);
        } catch (SQLException e) {
            messageLabel.setText("Error de base de datos: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            e.printStackTrace();
        } catch (Exception e) {
            messageLabel.setText("Ocurrió un error inesperado: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}