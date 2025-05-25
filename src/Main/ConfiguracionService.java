package Main; // O package Main.dao; si tienes esa estructura

import java.io.*;

public class ConfiguracionService {

    // Nombre del archivo donde se guardará la configuración
    private static final String CONFIG_FILE_NAME = "sistema_configuracion.ser";

    /**
     * Guarda el objeto ConfiguracionSistema en un archivo serializable.
     * @param config El objeto ConfiguracionSistema a guardar.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public static void guardarConfiguracion(ConfiguracionSistema config) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(CONFIG_FILE_NAME);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(config);
            System.out.println("Configuración guardada en: " + CONFIG_FILE_NAME);
        }
    }

    /**
     * Carga el objeto ConfiguracionSistema desde un archivo serializable.
     * @return El objeto ConfiguracionSistema cargado, o null si el archivo no existe o hay un error.
     * @throws IOException Si ocurre un error de lectura.
     * @throws ClassNotFoundException Si la clase del objeto serializado no se encuentra.
     */
    public static ConfiguracionSistema cargarConfiguracion() throws IOException, ClassNotFoundException {
        File configFile = new File(CONFIG_FILE_NAME);
        if (!configFile.exists()) {
            System.out.println("Archivo de configuración no encontrado. Se creará uno nuevo al guardar.");
            return null; // Retorna null si el archivo no existe
        }

        try (FileInputStream fileIn = new FileInputStream(CONFIG_FILE_NAME);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {
            ConfiguracionSistema config = (ConfiguracionSistema) objectIn.readObject();
            System.out.println("Configuración cargada desde: " + CONFIG_FILE_NAME);
            return config;
        }
    }
}