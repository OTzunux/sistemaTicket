package Main;

import Main.model.Role;
import Main.model.Permission;
import Main.service.RoleService;
import Main.service.PermissionService; // Aunque el RoleService ya tiene acceso a los permisos
import Main.User; // Si tu clase User está en Main

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GestionRolesPermisosController {

    @FXML private TableView<Role> rolesTableView;
    @FXML private TableColumn<Role, Integer> roleIdColumn;
    @FXML private TableColumn<Role, String> roleNameColumn;
    @FXML private TableColumn<Role, String> roleDescriptionColumn;
    @FXML private TableColumn<Role, String> rolePermissionsColumn; // Para mostrar una cadena de permisos

    @FXML private TableView<Permission> permissionsTableView;
    @FXML private TableColumn<Permission, Integer> permissionIdColumn;
    @FXML private TableColumn<Permission, String> permissionNameColumn;
    @FXML private TableColumn<Permission, String> permissionDescriptionColumn;

    @FXML private Label messageLabel;
    @FXML private TabPane mainTabPane; // Referencia al TabPane

    private MainApp mainApp;
    private User loggedInUser;
    private RoleService roleService;
    private PermissionService permissionService;

    private ObservableList<Role> rolesData = FXCollections.observableArrayList();
    private ObservableList<Permission> permissionsData = FXCollections.observableArrayList();

    public GestionRolesPermisosController() {
        roleService = new RoleService();
        permissionService = new PermissionService();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        // Solo administradores pueden acceder [cite: 21]
        if (loggedInUser == null || !("ADMINISTRADOR".equalsIgnoreCase(loggedInUser.getRole()))) { // Asume que User tiene getRoleName()
            showAlert(Alert.AlertType.ERROR, "Acceso Denegado", "Solo los administradores pueden gestionar roles y permisos.");
            if (mainApp != null) {
                mainApp.showMainMenu(loggedInUser); // O regresarlo al login
            }
        }
        loadData(); // Cargar datos si el usuario es un administrador
    }

    @FXML
    private void initialize() {
        // Configurar TableView de Roles
        roleIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        roleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        roleDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        // Custom cell value factory para mostrar los nombres de los permisos
        rolePermissionsColumn.setCellValueFactory(cellData -> {
            List<String> permNames = cellData.getValue().getPermissions().stream()
                                             .map(Permission::getName)
                                             .collect(Collectors.toList());
            return new SimpleStringProperty(String.join(", ", permNames));
        });
        rolesTableView.setItems(rolesData);

        // Configurar TableView de Permisos
        permissionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        permissionNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        permissionDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        permissionsTableView.setItems(permissionsData);

        // Listener para seleccionar un rol y precargar sus permisos para edición/asignación (opcional)
        rolesTableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                // Aquí podrías cargar los permisos del rol seleccionado en otra parte de la UI
                // para la asignación o edición, si tu diseño lo requiere.
            });

        loadData(); // Cargar datos al inicializar (antes de que se establezca el usuario, lo que puede causar doble carga)
        // Mejor cargar datos solo una vez en setLoggedInUser. Comentar esta línea si loadData() se llama en setLoggedInUser
    }

    private void loadData() {
        try {
            // Cargar roles
            rolesData.clear();
            rolesData.addAll(roleService.getAllRoles());

            // Cargar permisos
            permissionsData.clear();
            permissionsData.addAll(permissionService.getAllPermissions());

            messageLabel.setText(""); // Limpiar mensaje de error
        } catch (SQLException e) {
            messageLabel.setText("Error al cargar datos: " + e.getMessage());
            messageLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewRole() {
        showRoleEditDialog(null);
    }

    @FXML
    private void handleEditRole() {
        Role selectedRole = rolesTableView.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            showRoleEditDialog(selectedRole);
        } else {
            showAlert(Alert.AlertType.WARNING, "Ningún Rol Seleccionado", "Por favor, seleccione un rol para modificar.");
        }
    }

    private void showRoleEditDialog(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("role-edit-dialog.fxml")); // Necesitarás crear este FXML
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(role == null ? "Crear Nuevo Rol" : "Modificar Rol");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            RoleEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRoleService(roleService); // Pasa el servicio al diálogo
            controller.setRole(role); // Pasa el rol a editar (null para nuevo)

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                loadData(); // Recargar datos si se guardó algo
                messageLabel.setText("Rol guardado exitosamente.");
                messageLabel.setTextFill(Color.GREEN);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Cargar Diálogo", "No se pudo cargar la ventana de edición de rol.");
        }
    }

    @FXML
    private void handleDeleteRole() {
        Role selectedRole = rolesTableView.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar el rol '" + selectedRole.getName() + "'?");
            alert.setContentText("Esta acción no se puede deshacer y eliminará sus asignaciones de permisos.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    roleService.deleteRole(selectedRole.getId()); 
                    rolesData.remove(selectedRole); // Remover de la lista observable
                    messageLabel.setText("Rol '" + selectedRole.getName() + "' eliminado exitosamente.");
                    messageLabel.setTextFill(Color.GREEN);
                } catch (IllegalArgumentException e) { // Mensaje de error de validación (ej. rol con usuarios) [cite: 17, 18, 19]
                    showAlert(Alert.AlertType.ERROR, "Error al Eliminar Rol", e.getMessage());
                    messageLabel.setText(e.getMessage());
                    messageLabel.setTextFill(Color.RED);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Error de Base de Datos", "No se pudo eliminar el rol: " + e.getMessage());
                    messageLabel.setText("Error de base de datos al eliminar rol.");
                    messageLabel.setTextFill(Color.RED);
                    e.printStackTrace();
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Ningún Rol Seleccionado", "Por favor, seleccione un rol para eliminar.");
        }
    }

    @FXML
    private void handleNewPermission() {
        showPermissionEditDialog(null);
    }

    private void showPermissionEditDialog(Permission permission) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("permission-edit-dialog.fxml")); // Necesitarás crear este FXML
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(permission == null ? "Crear Nuevo Permiso" : "Modificar Permiso");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            PermissionEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPermissionService(permissionService); // Pasa el servicio al diálogo
            controller.setPermission(permission); // Pasa el permiso a editar (null para nuevo)

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                loadData(); // Recargar datos si se guardó algo
                messageLabel.setText("Permiso guardado exitosamente.");
                messageLabel.setTextFill(Color.GREEN);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Cargar Diálogo", "No se pudo cargar la ventana de edición de permiso.");
        }
    }

    @FXML
    private void handleAssignPermissions() {
        Role selectedRole = rolesTableView.getSelectionModel().getSelectedItem();
        if (selectedRole != null) {
            showAssignPermissionsDialog(selectedRole);
        } else {
            showAlert(Alert.AlertType.WARNING, "Ningún Rol Seleccionado", "Por favor, seleccione un rol para asignar permisos.");
        }
    }

    private void showAssignPermissionsDialog(Role role) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("assign-permissions-dialog.fxml")); // Necesitarás crear este FXML
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Asignar Permisos a: " + role.getName());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AssignPermissionsDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRoleService(roleService); // Pasa el servicio de roles
            controller.setPermissionService(permissionService); // Pasa el servicio de permisos
            controller.setRole(role); // Pasa el rol para el que se asignarán permisos

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                loadData(); // Recargar datos para mostrar los permisos actualizados del rol
                messageLabel.setText("Permisos asignados/desasignados exitosamente al rol '" + role.getName() + "'.");
                messageLabel.setTextFill(Color.GREEN);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Cargar Diálogo", "No se pudo cargar la ventana de asignación de permisos.");
        }
    }


    @FXML
    private void handleRegresar(ActionEvent event) {
        if (mainApp != null && loggedInUser != null) {
            mainApp.showMainMenu(loggedInUser); // Regresar al menú principal [cite: 8]
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}