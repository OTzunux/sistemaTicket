package Main.Util; // O package Main.util; si tienes esa estructura

import java.io.File;
import java.util.List;
import java.util.Arrays;
import java.util.TimeZone;

public class ValidadorParametros {

    // Lista de idiomas soportados (debería coincidir con los del ComboBox)
    private static final List<String> IDIOMAS_SOPORTADOS = Arrays.asList("Español", "Inglés");

    /**
     * Valida el nombre de la empresa.
     * @param nombre El nombre de la empresa.
     * @throws IllegalArgumentException si el nombre es inválido.
     */
    public static void validarNombreEmpresa(String nombre) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la empresa no puede estar vacío."); //[cite: 3]
        }
        if (nombre.trim().length() < 3 || nombre.trim().length() > 100) {
            throw new IllegalArgumentException("El nombre de la empresa debe contener entre 3 y 100 caracteres."); //[cite: 3]
        }
    }

    /**
     * Valida el archivo del logo de la empresa.
     * @param logoFile El objeto File del logo.
     * @throws IllegalArgumentException si el logo es inválido.
     */
    public static void validarLogoEmpresa(File logoFile) throws IllegalArgumentException {
        if (logoFile == null) {
            // Esto es opcional, si el logo no es estrictamente requerido siempre.
            // Para este CU, asumimos que no es requerido para guardar si no se selecciona.
            // Si fuera requerido, lanzar excepción aquí.
            return;
        }

        long maxSizeInBytes = 2 * 1024 * 1024; // 2MB [cite: 4]
        if (logoFile.length() > maxSizeInBytes) {
            throw new IllegalArgumentException("El tamaño del logo no debe superar los 2MB."); //[cite: 4]
        }

        String fileName = logoFile.getName().toLowerCase();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".png")) {
            throw new IllegalArgumentException("El logo debe ser una imagen JPG o PNG."); //[cite: 4]
        }
    }

    /**
     * Valida el idioma seleccionado.
     * @param idioma El idioma seleccionado.
     * @throws IllegalArgumentException si el idioma no es válido.
     */
    public static void validarIdioma(String idioma) throws IllegalArgumentException {
        if (idioma == null || !IDIOMAS_SOPORTADOS.contains(idioma)) {
            throw new IllegalArgumentException("El idioma seleccionado no es válido."); //[cite: 5]
        }
    }

    /**
     * Valida la zona horaria. (El ComboBox ya filtra, pero es bueno tener una capa de validación).
     * @param zonaHoraria La zona horaria seleccionada.
     * @throws IllegalArgumentException si la zona horaria no es válida.
     */
    public static void validarZonaHoraria(String zonaHoraria) throws IllegalArgumentException {
        if (zonaHoraria == null || !Arrays.asList(TimeZone.getAvailableIDs()).contains(zonaHoraria)) {
            throw new IllegalArgumentException("La zona horaria seleccionada no es válida."); //[cite: 5]
        }
    }

    /**
     * Valida el tiempo de vencimiento de tickets.
     * @param tiempo El tiempo en días.
     * @throws IllegalArgumentException si el tiempo no está dentro del rango permitido.
     */
    public static void validarTiempoVencimiento(int tiempo) throws IllegalArgumentException {
        if (tiempo < 1 || tiempo > 365) {
            throw new IllegalArgumentException("El tiempo de vencimiento de tickets debe ser entre 1 y 365 días."); 
        }
    }

    /**
     * Valida que se hayan definido al menos tres niveles de prioridad.
     * @param niveles La lista de niveles de prioridad.
     * @throws IllegalArgumentException si no hay al menos tres niveles.
     */
    public static void validarNivelesPrioridad(List<String> niveles) throws IllegalArgumentException {
        if (niveles == null || niveles.size() < 3) {
            throw new IllegalArgumentException("Debe definir al menos tres niveles de prioridad para los tickets."); 
        }
    }
}