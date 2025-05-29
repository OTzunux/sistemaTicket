package Main; // O Main.controller.dialog;

import Main.model.Permission;
import Main.service.PermissionService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

public class PermissionEditDialogController {

    @FXML private TextField permissionNameField;
    @FXML private TextArea permissionDescriptionArea;
    @FXML private Label messageLabel;

    private Stage dialogStage;
    private Permission permission; // El permiso a editar (null si es nuevo)
    private boolean okClicked = false;
    private PermissionService permissionService;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
        if (permission != null) {
            permissionNameField.setText(permission.getName());
            permissionDescriptionArea.setText(permission.getDescription());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        String name = permissionNameField.getText();
        String description = permissionDescriptionArea.getText();

        try {
            if (permission == null) { // Crear nuevo permiso
                permission = new Permission(name, description);
                permissionService.createPermission(permission);
            } else { // Modificar permiso existente (aunque no se pidió, es buena práctica)
                // Aquí deberías tener un método updatePermission en PermissionService
                // permission.setName(name);
                // permission.setDescription(description);
                // permissionService.updatePermission(permission);
                messageLabel.setText("La edición de permisos no está implementada completamente en este CU.");
                messageLabel.setTextFill(Color.ORANGE);
                return; // No cierra el diálogo si no hay actualización real
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