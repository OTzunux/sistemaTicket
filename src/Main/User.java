package Main;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password; // NOTA: En un sistema real, usarías hashing para contraseñas.
    private String role;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Setters (puedes añadir si necesitas modificar)
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}