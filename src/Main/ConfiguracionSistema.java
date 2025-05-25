package Main; // Asegúrate de que este sea el paquete correcto. Si usas Main.model, cambia a package Main.model;

import java.io.Serializable; // Recomendado para la serialización (guardar/cargar en archivo)
import java.util.ArrayList; // Para la lista de prioridades
import java.util.List;

public class ConfiguracionSistema implements Serializable {

    private static final long serialVersionUID = 1L; // ID para la serialización

    private String nombreEmpresa;
    private String logoEmpresaPath; // Almacena la ruta absoluta al archivo del logo
    private String idiomaPredeterminado;
    private String zonaHoraria;
    private int tiempoVencimientoTicketsInactivosDias;
    private List<String> nivelesPrioridadTickets; // Usamos List para las prioridades

    // Constructor vacío (necesario para algunas operaciones de JavaFX o frameworks)
    public ConfiguracionSistema() {
        this.nivelesPrioridadTickets = new ArrayList<>(); // Inicializa la lista para evitar NullPointerException
    }

    // Constructor con todos los campos (opcional, pero útil)
    public ConfiguracionSistema(String nombreEmpresa, String logoEmpresaPath, String idiomaPredeterminado,
                                String zonaHoraria, int tiempoVencimientoTicketsInactivosDias,
                                List<String> nivelesPrioridadTickets) {
        this.nombreEmpresa = nombreEmpresa;
        this.logoEmpresaPath = logoEmpresaPath;
        this.idiomaPredeterminado = idiomaPredeterminado;
        this.zonaHoraria = zonaHoraria;
        this.tiempoVencimientoTicketsInactivosDias = tiempoVencimientoTicketsInactivosDias;
        // Creamos una nueva ArrayList para evitar que la lista externa modifique directamente la interna
        this.nivelesPrioridadTickets = new ArrayList<>(nivelesPrioridadTickets);
    }

    // --- Getters y Setters para todos los atributos ---

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getLogoEmpresaPath() {
        return logoEmpresaPath;
    }

    public void setLogoEmpresaPath(String logoEmpresaPath) {
        this.logoEmpresaPath = logoEmpresaPath;
    }

    public String getIdiomaPredeterminado() {
        return idiomaPredeterminado;
    }

    public void setIdiomaPredeterminado(String idiomaPredeterminado) {
        this.idiomaPredeterminado = idiomaPredeterminado;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public int getTiempoVencimientoTicketsInactivosDias() {
        return tiempoVencimientoTicketsInactivosDias;
    }

    public void setTiempoVencimientoTicketsInactivosDias(int tiempoVencimientoTicketsInactivosDias) {
        this.tiempoVencimientoTicketsInactivosDias = tiempoVencimientoTicketsInactivosDias;
    }

    public List<String> getNivelesPrioridadTickets() {
        // Retornamos una copia defensiva para que la lista original no sea modificada desde fuera
        return new ArrayList<>(nivelesPrioridadTickets);
    }

    public void setNivelesPrioridadTickets(List<String> nivelesPrioridadTickets) {
        // Al setear, también creamos una nueva ArrayList
        this.nivelesPrioridadTickets = new ArrayList<>(nivelesPrioridadTickets);
    }

    // Puedes añadir un método toString para depuración si lo deseas
    @Override
    public String toString() {
        return "ConfiguracionSistema{" +
               "nombreEmpresa='" + nombreEmpresa + '\'' +
               ", logoEmpresaPath='" + logoEmpresaPath + '\'' +
               ", idiomaPredeterminado='" + idiomaPredeterminado + '\'' +
               ", zonaHoraria='" + zonaHoraria + '\'' +
               ", tiempoVencimientoTicketsInactivosDias=" + tiempoVencimientoTicketsInactivosDias +
               ", nivelesPrioridadTickets=" + nivelesPrioridadTickets +
               '}';
    }
}