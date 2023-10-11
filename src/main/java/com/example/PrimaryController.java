package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class PrimaryController implements Initializable {

    @FXML Pagination pagination;
    private int pagina = 1;

    public FlowPane carregar(){
        try {
            var url = new URL("https://www.mmobomb.com/api1/games");
            var con = url.openConnection();
            con.connect();
            var is = con.getInputStream();
            var reader = new BufferedReader(new InputStreamReader(is));
            var json = reader.readLine();

            var lista = jsonParaLista(json);
            mostrarPersonagens(lista);
            return mostrarPersonagens(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlowPane mostrarPersonagens(List<Jogo> lista) {
        var flow = new FlowPane();
        flow.setHgap(20);
        flow.setVgap(20);
        
        lista.forEach(p -> {
            var image = new ImageView(new Image(p.getThumbnail()));
            image.setFitWidth(100);
            image.setFitHeight(100);
            var labelName = new Label(p.getTitle());
            var labelSpecies = new Label(p.getPlatform());
            flow.getChildren().add(new VBox(image, labelName,labelSpecies));
        });
        return flow;
    }

    private List<Jogo> jsonParaLista(String json) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var results = mapper.readTree(json);
        List<Jogo> lista = new ArrayList<>();
        results.forEach(personagem -> {
            try {
                var p = mapper.readValue(personagem.toString(), Jogo.class);
                lista.add(p);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return lista;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        pagination.setPageFactory(pag -> {
            pagina = pag + 1;
            return carregar();
        });
    }
}
