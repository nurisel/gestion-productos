package cu.uci.gestionproductos.gestionproductosfincaellimonar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisponibilidadProductosActivity extends AppCompatActivity {
    Button add;
    ListView listView;
    TextView textSaldo;
    ArrayList<Producto> dataModels;
    private static TuplaInfo adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disponibilidad_productos);
        add();
        load();
        textSaldo= (TextView)findViewById(R.id.saldo);
        AccessDBProducto db = new AccessDBProducto(getBaseContext());
        float saldo =  db.getTotalSaldo();
        textSaldo.setText("Dinero recaudado: $"+String.valueOf(saldo));

    }


    public void load(){


        listView=(ListView)findViewById(R.id.listProducto);
        dataModels= new ArrayList<>();
        AccessDBProducto db = new AccessDBProducto(getBaseContext());
        List<Producto> list =  db.getAll();
        int cont = 0;
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getDisponibilidad() == 0){
                cont++;
            }
        }
        Toast.makeText(DisponibilidadProductosActivity.this,"Existen "+cont+" sin disponibilidad.",Toast.LENGTH_SHORT).show();

        for (int i = 0; i < list.size() ; i++)
            dataModels.add(list.get(i));

        adapter= new TuplaInfo(dataModels,DisponibilidadProductosActivity.this);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Producto dataModel= dataModels.get(position);

            }
        });
    }

    public void add(){
        add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegistrarProductoActivity.class);
                startActivity(intent);
            }
        });
    }
}
