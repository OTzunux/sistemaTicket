package Main.model; // O Main; si no usas subpaquetes

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Para el método equals y hashCode

public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name; // Nombre del rol [cite: 4]
    private String description; // Descripción opcional del rol [cite: 5]
    private List<Permission> permissions; // Permisos asignados a este rol

    public Role() {
        this.permissions = new ArrayList<>();
    }

    public Role(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = new ArrayList<>();
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
        this.permissions = new ArrayList<>();
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

    public List<Permission> getPermissions() {
        return new ArrayList<>(permissions); // Retorna una copia defensiva
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = new ArrayList<>(permissions); // Acepta una copia defensiva
    }

    public void addPermission(Permission permission) {
        if (!this.permissions.contains(permission)) {
            this.permissions.add(permission);
        }
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    @Override
    public String toString() {
        return name; // Conveniente para mostrar en ComboBox o ListView
    }

    // Es importante implementar equals y hashCode para comparar objetos Role correctamente
    // especialmente si los usas en colecciones como List o para verificar duplicados.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id == role.id; // Comparar por ID si es un objeto persistido
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}