package co.com.s4n.training.java.tallerMap;

import io.vavr.control.Try;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ServicioPremios {
    public static Try<Premio> entregarPremio(){
        return Try.of(()->{throw new Error("Error al reclamar Premio");});
    }
    public static Try<Premio> reclamarPremio(Premio premio){
        return Try.of(()->{throw new Error("Error al reclamar Premio");});
    }

    public static List<Premio> crearHamburguesas(int cantidad){
        return Arrays
                .stream(new int[cantidad])
                .mapToObj(i -> new Premio("hamburguesa"))
                .collect(Collectors.toList());
    }
    public static List<Premio> crearBonosCafe(int cantidad){
        return Arrays
                .stream(new int[cantidad])
                .mapToObj(i -> new Premio("café"))
                .collect(Collectors.toList());
    }
    public static List<Premio> crearBonosCine(int cantidad){
        return Arrays
                .stream(new int[cantidad])
                .mapToObj(i -> new Premio("cine"))
                .collect(Collectors.toList());
    }

    public static Try<Premio> obtenerHamburguesa( List<Premio> hamburguesas){
        return Try.of(()->hamburguesas.iterator().next());
    }
}
