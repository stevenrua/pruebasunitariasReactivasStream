package com.example.demo;


import org.junit.jupiter.api.Test;
import org.w3c.dom.ls.LSOutput;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class DojoReactiveTest {

    @Test
    void converterData(){
        List<Player> list = CsvUtilFile.getPlayers();
        assert list.size() == 18207;
    }



    @Test
    void jugadoresMayoresA35(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .filter(player -> player.age >=35)
                .groupBy(Player::getClub)
                .subscribe(group -> {
                    System.out.println("Equipo " + group.key() + ":");
                    group.subscribe(p -> System.out.println("--" + p.getName() + " " + p.getAge()));
                });

    }

    @Test
    void mejorJugadorConNacionalidadFrancia(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .filter(player -> player.getNational().equals("France"))
                .reduce((player1, player2) -> player1.getWinners() >=
                        player2.getWinners() ? player1 : player2)
                .subscribe(player -> System.out.println("El mejor jugador de francia es: " + player.getName()));

    }

    @Test
    void clubsAgrupadosPorNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .groupBy(Player::getNational)
                .subscribe(group -> {
                    System.out.println("Clubes de " + group.key() + ":");
                    group.subscribe(p -> System.out.println("_ " + p.getClub()));
                });
    }

    @Test
    void clubConElMejorJugador(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .reduce((jugador1, jugador2) -> jugador1.getWinners() >=
                        jugador2.getWinners() ? jugador1 : jugador2)
                .subscribe(bestplayer -> System.out.println("El mejor jugador es: " + bestplayer.getName() +
                        " del país de " + bestplayer.getNational()));
    }


    @Test
    void mejorJugadorSegunNacionalidad(){
        List<Player> list = CsvUtilFile.getPlayers();
        Flux.fromIterable(list)
                .groupBy(Player::getNational)
                .flatMap(bestplayernation ->
                        bestplayernation.reduce((j1, j2) -> j1.getWinners() >= j2.getWinners() ? j1 : j2))
                .map(bestplayer -> {
                    System.out.println("El mejor jugador de " + bestplayer.getNational() + " es :");
                    System.out.println("--" + bestplayer.getName());
                    return bestplayer;
                }).subscribe();

    }

}
