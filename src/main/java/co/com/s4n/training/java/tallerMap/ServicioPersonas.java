package co.com.s4n.training.java.tallerMap;

import io.vavr.control.Option;

public class ServicioPersonas{
    public static Option<Premio> entregarRegalo(Person p){
        return p.segundoApellido.filter(s -> s.length() == 5)
                .map(s -> new Premio());
    }
}