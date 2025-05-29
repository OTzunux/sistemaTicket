package Main; // Asegúrate de que este sea el paquete correcto

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button; // Necesitamos importar Button
import javafx.scene.control.Label;
import Main.User; // Asegúrate de que esta sea la ruta correcta a tu clase User
import Main.MainApp; // Asegúrate de que esta sea la ruta correcta a tu clase MainApp

public class MainMenuController {

    @FXML
    private Label welcomeLabel; // Corresponde al fx:id en el FXML

    // Asegúrate de que estos fx:id existan en tu main-menu-view.fxml
    @FXML
    private Button gestionRolesPermisosButton; // Nuevo FXML ID para el botón de Roles y Permisos
    @FXML
    private Button gestionTicketsButton; // Asumiendo que ya tienes o tendrás un botón para gestión de tickets
    @FXML
    private Button configurarParametrosButton; // Asumiendo que ya tienes o tendrás un botón para configurar parámetros
    @FXML
    private Button generarReportesButton; // Asumiendo que ya tienes o tendrás un botón para generar reportes
    @FXML
    private Button logoutButton; // Asumiendo que ya tienes un botón para logout

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
            // Asegúrate de que tu clase User tiene un método getRoleName() que devuelve "ADMINISTRADOR", "TECNICO", etc.
            // Si tu método es getRole() que devuelve el nombre del rol, úsalo.
            welcomeLabel.setText("¡Bienvenido, " + user.getUsername() + " (" + user.getRole() + ")!");
            updateButtonVisibility(); // Actualiza la visibilidad de los botones al establecer el usuario
        }
    }

    // Método auxiliar para habilitar/deshabilitar botones según el rol
    private void updateButtonVisibility() {
        if (loggedInUser == null) {
            // Si no hay usuario logueado, deshabilitar todo excepto quizás el botón de salir
            if (gestionRolesPermisosButton != null) gestionRolesPermisosButton.setDisable(true);
            if (gestionTicketsButton != null) gestionTicketsButton.setDisable(true);
            if (configurarParametrosButton != null) configurarParametrosButton.setDisable(true);
            if (generarReportesButton != null) generarReportesButton.setDisable(true);
            return;
        }

        String roleName = loggedInUser.getRole(); // Obtén el nombre del rol del usuario

        // Lógica de habilitación/deshabilitación basada en roles
        // 1. Botón "Gestionar Roles y Permisos": Solo para ADMINISTRADOR
        if (gestionRolesPermisosButton != null) {
            gestionRolesPermisosButton.setDisable(!("ADMINISTRADOR".equalsIgnoreCase(roleName)));
        }

        // 2. Botón "Configurar Parámetros del Sistema": Solo para ADMINISTRADOR
        if (configurarParametrosButton != null) {
            configurarParametrosButton.setDisable(!("ADMINISTRADOR".equalsIgnoreCase(roleName)));
        }

        // 3. Botón "Gestión de Tickets": Para ADMINISTRADOR y TECNICO, USUARIO
        // Puedes refinar esto basándote en permisos específicos si la clase User los carga
        if (gestionTicketsButton != null) {
            boolean canAccessTickets = "ADMINISTRADOR".equalsIgnoreCase(roleName) ||
                                       "TECNICO".equalsIgnoreCase(roleName) ||
                                       "USUARIO".equalsIgnoreCase(roleName);
            gestionTicketsButton.setDisable(!canAccessTickets);
        }

        // 4. Botón "Generar Reportes": Por ejemplo, para ADMINISTRADOR y TECNICO
        if (generarReportesButton != null) {
            boolean canGenerateReports = "ADMINISTRADOR".equalsIgnoreCase(roleName) ||
                                         "TECNICO".equalsIgnoreCase(roleName);
            generarReportesButton.setDisable(!canGenerateReports);
        }

        // El botón de logout siempre debería estar habilitado si hay un usuario logueado
        if (logoutButton != null) {
            logoutButton.setDisable(false);
        }
    }


    @FXML
    private void handleConfigurarParametros(ActionEvent event) {
        // La visibilidad del botón ya se maneja con updateButtonVisibility(),
        // pero una doble verificación aquí no está de más para seguridad.
        if (mainApp != null && loggedInUser != null && "ADMINISTRADOR".equalsIgnoreCase(loggedInUser.getRole())) {
            mainApp.showConfiguracionSistema(loggedInUser);
        } else {
            // Esto no debería ocurrir si el login ya validó el rol y el botón está deshabilitado
            // Puedes mostrar una alerta o simplemente imprimir en consola para depuración
            System.out.println("Acceso denegado. Solo administradores pueden configurar parámetros.");
            // showAlert(Alert.AlertType.WARNING, "Acceso Denegado", "No tienes permisos para acceder a esta función.");
        }
    }

    @FXML
    private void handleGestionRolesPermisos(ActionEvent event) {
        if (mainApp != null && loggedInUser != null && "ADMINISTRADOR".equalsIgnoreCase(loggedInUser.getRole())) {
            mainApp.showGestionRolesPermisos(loggedInUser);
        } else {
            System.out.println("Acceso denegado. Solo administradores pueden gestionar roles y permisos.");
        }
    }

    // Ejemplo para gestión de tickets (si tienes el CU implementado)
    @FXML
    private void handleGestionTickets(ActionEvent event) {
        // Lógica de acceso para gestión de tickets
        if (mainApp != null && loggedInUser != null) {
            // Aquí podrías verificar permisos más granulares,
            // pero por ahora asumimos que si el botón está habilitado, puede acceder.
            System.out.println("Navegando a Gestión de Tickets...");
            // mainApp.showGestionTickets(loggedInUser); // Descomentar cuando implementes el CU de tickets
        }
    }

    // Ejemplo para generar reportes (si tienes el CU implementado)
    @FXML
    private void handleReportes(ActionEvent event) {
        // Lógica de acceso para reportes
        if (mainApp != null && loggedInUser != null) {
            System.out.println("Navegando a Generar Reportes...");
            // mainApp.showReportes(loggedInUser); // Descomentar cuando implementes el CU de reportes
        }
    }


    @FXML
    private void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScreen(); // Vuelve a la pantalla de login
        }
    }

    // Puedes añadir un método initialize si necesitas configurar algo al cargar el FXML
    // Por ejemplo, deshabilitar botones al inicio antes de que se establezca el usuario.
    @FXML
    private void initialize() {
        // Esto asegura que los botones están deshabilitados por defecto hasta que se cargue el usuario.
        if (gestionRolesPermisosButton != null) gestionRolesPermisosButton.setDisable(true);
        if (gestionTicketsButton != null) gestionTicketsButton.setDisable(true);
        if (configurarParametrosButton != null) configurarParametrosButton.setDisable(true);
        if (generarReportesButton != null) generarReportesButton.setDisable(true);
        if (logoutButton != null) logoutButton.setDisable(false); // Logout siempre habilitado si el menú está visible
    }
}