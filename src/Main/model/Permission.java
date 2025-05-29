package Main.model; // O Main; si no usas subpaquetes

import java.io.Serializable;
import java.util.Objects; // Para el método equals y hashCode

public class Permission implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name; // Nombre del permiso [cite: 6]
    private String description; // Explicación de la acción que permite realizar [cite: 7]

    public Permission() {}

    public Permission(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Permission(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name; // Conveniente para mostrar en ComboBox o ListView
    }

    // Implementar equals y hashCode para comparar objetos Permission correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return id == that.id; // Comparar por ID si es un objeto persistido
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}