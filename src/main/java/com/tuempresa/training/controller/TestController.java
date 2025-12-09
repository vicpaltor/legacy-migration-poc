package com.tuempresa.training.controller;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Component
@ViewScoped
public class TestController implements Serializable {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String mensaje;
    private String inputUsuario;

    //ARRANCAR LA PANTALLA ---
    @PostConstruct
    public void init() {
        // Nada más entrar, buscamos qué hay en la BBDD
        cargarUltimaTarea();
    }

    //ACCIÓN DEL BOTÓN
    public void procesarAccion() {
        try {
            // LLAMADA AL PROCEDIMIENTO (INSERTAR)
            insertarTarea();
            // REFRESCO DE DATOS (QUERY)
            cargarUltimaTarea();

        } catch (Exception e) {
            this.mensaje = "Error crítico: " + e.getMessage();
            e.printStackTrace();
        }
    }

    private void insertarTarea() {

        // A) LLAMADA AL PROCEDIMIENTO (INSERTAR)
        String llamadaPLSQL = "{call PR_CREAR_TAREA(?, ?)}";

        jdbcTemplate.execute(llamadaPLSQL, (CallableStatementCallback<String>) cs -> {
            cs.setString(1, this.inputUsuario);         // Parámetro 1: Descripción (IN)
            cs.registerOutParameter(2, Types.VARCHAR);  // Parámetro 2: Mensaje Salida (OUT)
            cs.execute();
            return cs.getString(2); // No usamos esto ahora, pero ahí viene el mensaje de Oracle
        });

        // LIMPIEZA
        this.inputUsuario = ""; // Borramos lo que escribió el usuario para que quede limpio

    }

    // --- 3. MÉTODO AUXILIAR (LÓGICA DE LECTURA) ---
    private void cargarUltimaTarea() {
        try {

            String sql = "SELECT DESCRIPTION FROM MORPHIS_TASKS ORDER BY ID DESC FETCH FIRST 1 ROWS ONLY";

            // queryForList es más seguro que queryForObject por si la tabla está vacía
            List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql);

            if (!resultados.isEmpty()) {
                // Sacamos el valor de la columna DESCRIPTION de la primera fila
                String descripcion = (String) resultados.get(0).get("DESCRIPTION");
                this.mensaje = "Último registro en BBDD: " + descripcion;
            } else {
                this.mensaje = "La tabla está vacía. ¡Añade tu primera tarea!";
            }

        } catch (Exception e) {
            // Si falla el SQL moderno, intentamos el clásico por si acaso
            this.mensaje = "Error leyendo (o tabla vacía): " + e.getMessage();
        }
    }

    // Getters y Setters
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getInputUsuario() { return inputUsuario; }
    public void setInputUsuario(String inputUsuario) { this.inputUsuario = inputUsuario; }
}