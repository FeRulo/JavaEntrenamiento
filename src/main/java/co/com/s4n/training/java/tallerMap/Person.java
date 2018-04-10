package co.com.s4n.training.java.tallerMap;

import io.vavr.control.Option;

public class Person{
    public String primerNombre;
    public Option<String> segundoNombre;
    public String primerApellido;
    public Option<String> segundoApellido;

    public Person(String primerNombre, String segundoNombre, String primerApellido, String segundoApellido) {
        this.primerNombre = primerNombre;
        this.segundoNombre = Option.of(segundoNombre);
        this.primerApellido = primerApellido;
        this.segundoApellido = Option.of(segundoApellido);
    }

    @Override
    public String toString() {
        return this.primerNombre + " " + this.segundoNombre.getOrElse("") +
                this.primerApellido + " " + this.segundoApellido.getOrElse("");
    }
}