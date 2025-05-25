package Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.paint.Color;
import Main.User;
import Main.MainApp;
import Main.Util.ValidadorParametros;

import java.io.File;
import java.io.IOException; // Necesario para manejar las excepciones de E/S
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class ConfiguracionSistemaController {

    @FXML private TextField nombreEmpresaField;
    @FXML private Button seleccionarLogoButton;
    @FXML private ComboBox<String> idiomaComboBox;
    @FXML private ComboBox<String> zonaHorariaComboBox;
    @FXML private Spinner<Integer> tiempoVencimientoSpinner;
    @FXML private ListView<String> nivelesPrioridadListView;
    @FXML private Button guardarButton;
    @FXML private Label mensajeLabel;

    @FXML private Button agregarPrioridadButton;
    @FXML private Button editarPrioridadButton;
    @FXML private Button eliminarPrioridadButton;

    private MainApp mainApp;
    private User adminUser;
    private File selectedLogoFile;
    private ObservableList<String> prioridadesObservableList;

    // Se mantiene una referencia al objeto de configuración actual
    private ConfiguracionSistema currentConfig;

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
        cargarConfiguracionExistente(); // Llama a cargar la configuración al iniciar
    }

    @FXML
    private void initialize() {
        ObservableList<String> idiomas = FXCollections.observableArrayList("Español", "Inglés");
        idiomaComboBox.setItems(idiomas);
        idiomaComboBox.setValue("Español");

        ObservableList<String> zonasHorarias = FXCollections.observableArrayList(TimeZone.getAvailableIDs());
        zonaHorariaComboBox.setItems(zonasHorarias);
        zonaHorariaComboBox.setValue(TimeZone.getDefault().getID());

        tiempoVencimientoSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 30));

        prioridadesObservableList = FXCollections.observableArrayList();
        nivelesPrioridadListView.setItems(prioridadesObservableList);
        prioridadesObservableList.addAll("Alta", "Media", "Baja");

        // Opcional: Deshabilitar botones de edición/eliminación al inicio si no hay selección
        nivelesPrioridadListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean isItemSelected = (newVal != null);
            if (editarPrioridadButton != null) editarPrioridadButton.setDisable(!isItemSelected);
            if (eliminarPrioridadButton != null) eliminarPrioridadButton.setDisable(!isItemSelected);
        });
        // Asegurarse de que estén deshabilitados al inicio si la lista está vacía o no hay selección
        if (editarPrioridadButton != null) editarPrioridadButton.setDisable(true);
        if (eliminarPrioridadButton != null) eliminarPrioridadButton.setDisable(true);
    }

    @FXML
    private void handleSeleccionarLogo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Logo de la Empresa");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.jpg", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (selectedFile != null) {
            this.selectedLogoFile = selectedFile;
            mensajeLabel.setText("Logo seleccionado: " + selectedFile.getName());
            mensajeLabel.setTextFill(Color.BLACK);
        } else {
            // Si el usuario cancela, no limpia el logo previamente seleccionado
            // Si deseas limpiarlo, descomenta la línea de abajo:
            // this.selectedLogoFile = null;
            mensajeLabel.setText("Ningún logo nuevo seleccionado.");
            mensajeLabel.setTextFill(Color.GRAY);
        }
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        String nombreEmpresa = nombreEmpresaField.getText();
        String idioma = idiomaComboBox.getValue();
        String zonaHoraria = zonaHorariaComboBox.getValue();
        int tiempoVencimiento = tiempoVencimientoSpinner.getValue();
        List<String> nivelesPrioridad = new ArrayList<>(prioridadesObservableList); // Copia para la validación

        try {
            // Validaciones
            ValidadorParametros.validarNombreEmpresa(nombreEmpresa);
            ValidadorParametros.validarLogoEmpresa(selectedLogoFile); // Se valida si se ha seleccionado uno
            ValidadorParametros.validarIdioma(idioma);
            ValidadorParametros.validarZonaHoraria(zonaHoraria);
            ValidadorParametros.validarTiempoVencimiento(tiempoVencimiento);
            ValidadorParametros.validarNivelesPrioridad(nivelesPrioridad);

            // Si todas las validaciones pasan, creamos o actualizamos el objeto de configuración
            if (currentConfig == null) {
                currentConfig = new ConfiguracionSistema(); // Crear nueva si no hay una cargada
            }
            currentConfig.setNombreEmpresa(nombreEmpresa);
            currentConfig.setLogoEmpresaPath(selectedLogoFile != null ? selectedLogoFile.getAbsolutePath() : null);
            currentConfig.setIdiomaPredeterminado(idioma);
            currentConfig.setZonaHoraria(zonaHoraria);
            currentConfig.setTiempoVencimientoTicketsInactivosDias(tiempoVencimiento);
            currentConfig.setNivelesPrioridadTickets(nivelesPrioridad); // Usa el setter que copia la lista

            // GUARDAR en el archivo serializable
            ConfiguracionService.guardarConfiguracion(currentConfig);

            mensajeLabel.setText("Configuración guardada exitosamente.");
            mensajeLabel.setTextFill(Color.GREEN);

            // TODO: Registro de historial de cambios:
            // Logear que el adminUser realizó esta modificación.
            // Para esto necesitarías una clase de log/auditoría.

        } catch (IllegalArgumentException e) {
            mensajeLabel.setText(e.getMessage());
            mensajeLabel.setTextFill(Color.RED);
        } catch (IOException e) {
            mensajeLabel.setText("Error al guardar la configuración: " + e.getMessage());
            mensajeLabel.setTextFill(Color.RED);
            e.printStackTrace();
        } catch (Exception e) {
            mensajeLabel.setText("Ocurrió un error inesperado al guardar la configuración.");
            mensajeLabel.setTextFill(Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAgregarPrioridad(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Agregar Nivel de Prioridad");
        dialog.setHeaderText("Ingrese el nuevo nivel de prioridad");
        dialog.setContentText("Nivel:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nivel -> {
            String trimmedNivel = nivel.trim();
            if (!trimmedNivel.isEmpty() && !prioridadesObservableList.contains(trimmedNivel)) {
                prioridadesObservableList.add(trimmedNivel);
                mensajeLabel.setText("Nivel de prioridad '" + trimmedNivel + "' agregado.");
                mensajeLabel.setTextFill(Color.BLACK);
            } else if (prioridadesObservableList.contains(trimmedNivel)) {
                mensajeLabel.setText("Este nivel de prioridad ya existe.");
                mensajeLabel.setTextFill(Color.ORANGE);
            } else {
                mensajeLabel.setText("El nivel de prioridad no puede estar vacío.");
                mensajeLabel.setTextFill(Color.RED);
            }
        });
    }
    
    // Método para manejar el botón Regresar
    @FXML
    private void handleRegresar(ActionEvent event) {
        if (mainApp != null && adminUser != null) {
            // Regresar al menú principal, pasando el usuario autenticado
            mainApp.showMainMenu(adminUser);
        }
    }

    @FXML
    private void handleEditarPrioridad(ActionEvent event) {
        String selectedPrioridad = nivelesPrioridadListView.getSelectionModel().getSelectedItem();
        if (selectedPrioridad != null) {
            TextInputDialog dialog = new TextInputDialog(selectedPrioridad);
            dialog.setTitle("Editar Nivel de Prioridad");
            dialog.setHeaderText("Modifique el nivel de prioridad");
            dialog.setContentText("Nivel:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newNivel -> {
                String trimmedNewNivel = newNivel.trim();
                if (!trimmedNewNivel.isEmpty() && !prioridadesObservableList.contains(trimmedNewNivel)) {
                    int index = prioridadesObservableList.indexOf(selectedPrioridad);
                    if (index != -1) {
                        prioridadesObservableList.set(index, trimmedNewNivel);
                        mensajeLabel.setText("Nivel de prioridad '" + selectedPrioridad + "' editado a '" + trimmedNewNivel + "'.");
                        mensajeLabel.setTextFill(Color.BLACK);
                    }
                } else if (prioridadesObservableList.contains(trimmedNewNivel) && !trimmedNewNivel.equals(selectedPrioridad)) {
                    mensajeLabel.setText("Este nivel de prioridad ya existe.");
                    mensajeLabel.setTextFill(Color.ORANGE);
                } else if (trimmedNewNivel.isEmpty()) {
                    mensajeLabel.setText("El nivel de prioridad no puede estar vacío.");
                    mensajeLabel.setTextFill(Color.RED);
                }
            });
        } else {
            mensajeLabel.setText("Seleccione un nivel de prioridad para editar.");
            mensajeLabel.setTextFill(Color.ORANGE);
        }
    }

    @FXML
    private void handleEliminarPrioridad(ActionEvent event) {
        String selectedPrioridad = nivelesPrioridadListView.getSelectionModel().getSelectedItem();
        if (selectedPrioridad != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar el nivel de prioridad '" + selectedPrioridad + "'?");
            alert.setContentText("Esta acción no se puede deshacer.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                prioridadesObservableList.remove(selectedPrioridad);
                mensajeLabel.setText("Nivel de prioridad '" + selectedPrioridad + "' eliminado.");
                mensajeLabel.setTextFill(Color.BLACK);
                // Validar si quedan menos de 3 prioridades después de eliminar
                try {
                    ValidadorParametros.validarNivelesPrioridad(prioridadesObservableList);
                } catch (IllegalArgumentException e) {
                    mensajeLabel.setTextFill(Color.ORANGE);
                    mensajeLabel.setText(e.getMessage()); // Muestra la advertencia si quedan menos de 3
                }
            }
        } else {
            mensajeLabel.setText("Seleccione un nivel de prioridad para eliminar.");
            mensajeLabel.setTextFill(Color.ORANGE);
        }
    }

    // Método para cargar la configuración existente desde el archivo
    private void cargarConfiguracionExistente() {
        try {
            ConfiguracionSistema loadedConfig = ConfiguracionService.cargarConfiguracion();
            if (loadedConfig != null) {
                currentConfig = loadedConfig; // Guarda la configuración cargada
                nombreEmpresaField.setText(currentConfig.getNombreEmpresa());
                // Si el path del logo no es nulo, intenta "recordar" el archivo seleccionado
                if (currentConfig.getLogoEmpresaPath() != null && !currentConfig.getLogoEmpresaPath().isEmpty()) {
                    File logo = new File(currentConfig.getLogoEmpresaPath());
                    if (logo.exists()) {
                        selectedLogoFile = logo;
                        mensajeLabel.setText("Logo cargado: " + logo.getName());
                        mensajeLabel.setTextFill(Color.BLACK);
                    } else {
                        selectedLogoFile = null;
                        mensajeLabel.setText("Logo configurado no encontrado en la ruta: " + currentConfig.getLogoEmpresaPath());
                        mensajeLabel.setTextFill(Color.ORANGE);
                    }
                } else {
                    selectedLogoFile = null;
                    mensajeLabel.setText(""); // Limpiar mensaje si no hay logo
                }
                idiomaComboBox.setValue(currentConfig.getIdiomaPredeterminado());
                zonaHorariaComboBox.setValue(currentConfig.getZonaHoraria());
                tiempoVencimientoSpinner.getValueFactory().setValue(currentConfig.getTiempoVencimientoTicketsInactivosDias());
                prioridadesObservableList.setAll(currentConfig.getNivelesPrioridadTickets()); // Carga las prioridades guardadas
                System.out.println("Configuración inicial cargada.");
            } else {
                currentConfig = new ConfiguracionSistema(); // Inicia con un objeto vacío si no hay archivo
                System.out.println("No se encontró configuración previa. Iniciando con valores por defecto.");
            }
        } catch (IOException | ClassNotFoundException e) {
            mensajeLabel.setText("Error al cargar la configuración existente: " + e.getMessage());
            mensajeLabel.setTextFill(Color.RED);
            e.printStackTrace();
            currentConfig = new ConfiguracionSistema(); // Asegurarse de tener un objeto de configuración
        }
    }
}