package com.example.demo;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.*;
import java.util.stream.Collectors;

public class DojoStreamTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }

    @Test
    void jugadoresMayoresA35(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<Player>> groupedPeople = list.stream()
                .filter(player -> player.age >= 35)
                .collect(Collectors.groupingBy(Player::getClub));

        groupedPeople.forEach((club, playerList) -> {
            System.out.println("Club: " + club);
            playerList.forEach(player -> System.out.println(player.getName() + " " + player.getAge()));
        });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> listaNueva = list.stream()
                .filter(player -> player.getNational().equals("France"))
                .max(Comparator.comparingInt(Player::getWinners));
        System.out.println("El mejor jugador de francia es: " + listaNueva.get().getName()
                + " con " + listaNueva.get().getWinners() + " partidos ganados");

    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, List<Player>> groupedPeople = list.stream()
                .collect(Collectors.groupingBy(Player::getNational));

        groupedPeople.forEach((nacionalidad, clubes) -> {
            System.out.println("Clubes de " + nacionalidad + ":");
            clubes.forEach(player -> System.out.println("_ " + player.getClub()));
        });
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> list = CsvUtilFile.getPlayers();
        Optional<Player> bestPlayer = list.stream()
                .max(Comparator.comparing(Player::getWinners));

        System.out.println("El club con el mejor jugador es: " +
                bestPlayer.get().getClub() + " y es " + bestPlayer.get().getName());
    }


    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Map<String, Player> mejoresJugadoresPorEquipo = list.stream()
                .collect(Collectors.groupingBy(Player::getNational,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(Player::getWinners)),
                                Optional::get)));

        mejoresJugadoresPorEquipo.forEach((nacionalidad, jugador) -> {
            System.out.println("El mejor jugador de  " + nacionalidad + " es: " + jugador.getName());

        });
    }
}
