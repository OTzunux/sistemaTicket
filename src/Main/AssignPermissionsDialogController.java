package Main; // O Main.controller.dialog;

import Main.model.Role;
import Main.model.Permission;
import Main.service.RoleService;
import Main.service.PermissionService;
import Main.dao.RolePermissionDAO; // Necesitamos el DAO para la asignación directa

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssignPermissionsDialogController {

    @FXML private Label roleNameLabel;
    @FXML private ListView<Permission> availablePermissionsListView;
    @FXML private ListView<Permission> assignedPermissionsListView;
    @FXML private Label messageLabel;

    private Stage dialogStage;
    private Role role;
    private RoleService roleService;
    private PermissionService permissionService;
    private RolePermissionDAO rolePermissionDAO; // Usar el DAO directamente aquí para la asignación

    private ObservableList<Permission> availablePermissions = FXCollections.observableArrayList();
    private ObservableList<Permission> assignedPermissions = FXCollections.observableArrayList();

    public AssignPermissionsDialogController() {
        this.rolePermissionDAO = new RolePermissionDAO();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setRole(Role role) {
        this.role = role;
        roleNameLabel.setText("Asignar permisos a: " + role.getName());
        loadPermissions();
    }

    public boolean isOkClicked() {
        return true; // Siempre se considerará "OK" si no hay excepciones al cerrar
    }

    private void loadPermissions() {
        try {
            // Cargar todos los permisos disponibles
            List<Permission> allPermissions = permissionService.getAllPermissions();
            availablePermissions.setAll(allPermissions);

            // Cargar los permisos ya asignados a este rol
            List<Permission> currentAssignedPermissions = role.getPermissions();
            assignedPermissions.setAll(currentAssignedPermissions);

            // Eliminar los permisos ya asignados de la lista de disponibles
            availablePermissions.removeAll(assignedPermissions);

        } catch (SQLException e) {
            messageLabel.setText("Error al cargar permisos: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAssignPermission() {
        Permission selectedPermission = availablePermissionsListView.getSelectionModel().getSelectedItem();
        if (selectedPermission != null) {
            try {
                rolePermissionDAO.assignPermissionToRole(role.getId(), selectedPermission.getId());
                assignedPermissions.add(selectedPermission);
                availablePermissions.remove(selectedPermission);
                messageLabel.setText("Permiso '" + selectedPermission.getName() + "' asignado.");
                messageLabel.setTextFill(Color.BLACK);
                role.addPermission(selectedPermission); // Actualizar el objeto Role en memoria
            } catch (SQLException e) {
                messageLabel.setText("Error al asignar permiso: " + e.getMessage());
                messageLabel.setTextFill(Color.RED);
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Seleccione un permiso para asignar.");
            messageLabel.setTextFill(Color.ORANGE);
        }
    }

    @FXML
    private void handleRemovePermission() {
        Permission selectedPermission = assignedPermissionsListView.getSelectionModel().getSelectedItem();
        if (selectedPermission != null) {
            try {
                rolePermissionDAO.removePermissionFromRole(role.getId(), selectedPermission.getId());
                availablePermissions.add(selectedPermission);
                assignedPermissions.remove(selectedPermission);
                messageLabel.setText("Permiso '" + selectedPermission.getName() + "' desasignado.");
                messageLabel.setTextFill(Color.BLACK);
                role.removePermission(selectedPermission); // Actualizar el objeto Role en memoria
            } catch (SQLException e) {
                messageLabel.setText("Error al desasignar permiso: " + e.setTextFill(Color.RED);getMessage());
                messageLabel.setTextFill(Color.RED);
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Seleccione un permiso para desasignar.");
            messageLabel.setTextFill(Color.ORANGE);
        }
    }

    @FXML
    private void handleOk() {
        // Los cambios ya se guardan directamente en la BD al asignar/desasignar.
        // Aquí solo cerramos el diálogo y actualizamos el objeto rol en memoria.
        // La recarga de datos en el controlador principal refrescará la tabla.
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}