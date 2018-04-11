package co.com.s4n.training.java.tallerMap;

public  class Premio {
    public String nombre;

    public Premio(String nombre) {
        this.nombre = nombre;
    }
    public Premio() {
    }

    @Override
    public boolean equals(Object p) {
        return this.nombre.equals(p);
    }
}