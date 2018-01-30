package cu.uci.gestionproductos.gestionproductosfincaellimonar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RegistrarProductoActivity extends AppCompatActivity {
    Button btnAdd;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_producto);
        back();create();
    }

    public void create(){
        btnAdd = (Button) findViewById(R.id.btnCrear);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView nombre = (TextView) findViewById(R.id.campoNombre);
                TextView disp = (TextView) findViewById(R.id.campoDisponibilidad);
                TextView precio = (TextView) findViewById(R.id.campoPrecio);

                boolean error = false;

                if(nombre.getText().toString().trim().isEmpty() || disp.getText().toString().trim().isEmpty()|| precio.getText().toString().trim().isEmpty()){
                    Toast.makeText(RegistrarProductoActivity.this,"Los campos no pueden estar en blanco.",Toast.LENGTH_SHORT).show();
                    error=true;
                }


                AccessDBProducto db = new AccessDBProducto(getBaseContext());
                List<Producto> list = db.getAll();





                if(!error){
                    Producto e = new Producto("",nombre.getText().toString(),Integer.valueOf(disp.getText().toString()),Float.valueOf(precio.getText().toString()));
                    long id = db.add(e);
                    Toast.makeText(RegistrarProductoActivity.this,"Servidor registrado con exito."+String.valueOf(id),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(), DisponibilidadProductosActivity.class);
                    startActivity(intent);
                }
            }
        });
    }



    public void back(){
        btnBack = (Button) findViewById(R.id.atrasBtn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DisponibilidadProductosActivity.class);
                startActivity(intent);
            }
        });
    }
}
