package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PrimaryController {

    @FXML FlowPane flow;

    public void carregar(){
        try {
            var url = new URL("https://rickandmortyapi.com/api/character");
            var con = url.openConnection();
            con.connect();
            var is = con.getInputStream();
            var reader = new BufferedReader(new InputStreamReader(is));
            var json = reader.readLine();

            var lista = jsonParaLista(json);
            mostrarPersonagens(lista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mostrarPersonagens(List<Personagem> lista) {
        flow.setHgap(20);
        flow.setVgap(20);
        lista.forEach(p -> {
            var image = new ImageView(new Image(p.getImage()));
            image.setFitWidth(100);
            image.setFitHeight(100);
            var labelName = new Label(p.getName());
            var labelSpecies = new Label(p.getSpecies());
            flow.getChildren().add(new VBox(image, labelName,labelSpecies));
        });
    }

    private List<Personagem> jsonParaLista(String json) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var results = mapper.readTree(json).get("results");
        List<Personagem> lista = new ArrayList<>();
        results.forEach(personagem -> {
            try {
                var p = mapper.readValue(personagem.toString(), Personagem.class);
                lista.add(p);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return lista;
    }
}
