package iansantos.registration.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String email;
    private String password;
    private String cpf;

    public User(String name, String email, String cpf, String password) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.password = password;
    }

    public String getName() {
        return name = (name.substring(0, 1).toUpperCase() + name.substring(1)).trim();
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email = email.toLowerCase().trim();
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password.trim();
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("Nome: %s \nEmail: %s \nCPF: %s \nSenha: %s", getName(), getEmail(), getCpf(), getPassword());
    }
}
