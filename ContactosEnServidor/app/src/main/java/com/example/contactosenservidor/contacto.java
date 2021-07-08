package com.example.contactosenservidor;

public class contacto {

    int contacto_id;
    String nombre;
    String telefono;
    String gmail;
    int foto;

    public contacto(int id, String nom, String tel, String g, int f){

        contacto_id = id;
        nombre = nom;
        telefono = tel;
        foto = f;
        gmail = g;
    }

    public int getContacto_id() {
        return contacto_id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getGmail() {
        return gmail;
    }

    public int getFoto() {
        return foto;
    }

    public void setContacto_id(int contacto_id) {
        this.contacto_id = contacto_id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

}
