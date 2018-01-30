package cu.uci.gestionproductos.gestionproductosfincaellimonar;

/**
 * Created by tatos on 30/01/18.
 */

public class Importe {
    private float saldo;
    private String id;

    public Importe(String id,float saldo) {
        this.saldo = saldo;
        this.id = id;
    }

    public float getSaldo() {
        return saldo;
    }

    public void setSaldo(float saldo) {
        this.saldo = saldo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
