package cu.uci.gestionproductos.gestionproductosfincaellimonar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tatos on 30/01/18.
 */

public class TuplaInfo extends ArrayAdapter<Producto> implements View.OnClickListener{
    private ArrayList<Producto> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtNombre;
        TextView txtDisponibilidad;
        Button deleteBtn;
        Button venderBtn;
    }

    public TuplaInfo(ArrayList<Producto> data, Context context) {
        super(context, R.layout.tupla_producto_lista, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        final int position=(Integer) v.getTag();
        Object object= getItem(position);
        final Producto dataModel=(Producto) object;

        switch (v.getId())
        {
            case R.id.btnDelete:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AccessDBProducto db = new AccessDBProducto(mContext);
                                db.delete(dataModel.getId());
                                dataSet.remove(position);
                                notifyDataSetChanged();
                                notifyDataSetInvalidated();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Estas seguro que quieres eliminar el producto?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            case R.id.btnVender:
                AccessDBProducto db = new AccessDBProducto(mContext);
                db.vender(dataModel.getId());
                db.updateImporte(dataModel.getPrecio());
                notifyDataSetChanged();
                notifyDataSetInvalidated();
                Intent intent = new Intent(mContext, DisponibilidadProductosActivity.class);
                mContext.startActivity(intent);

                break;

        }
    }
    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Producto dataModel = getItem(position);
        TuplaInfo.ViewHolder viewHolder;
        final View result;

        if (convertView == null) {

            viewHolder = new TuplaInfo.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tupla_producto_lista, parent, false);
            viewHolder.txtNombre = (TextView) convertView.findViewById(R.id.labelNombre);
            viewHolder.txtDisponibilidad = (TextView) convertView.findViewById(R.id.labelDisponibilidad);
            viewHolder.venderBtn = (Button) convertView.findViewById(R.id.btnVender);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.btnDelete);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TuplaInfo.ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtNombre.setText(dataModel.getNombre() + " ($"+dataModel.getPrecio()+")");
        viewHolder.txtDisponibilidad.setText(String.valueOf(dataModel.getDisponibilidad()));
        viewHolder.venderBtn.setOnClickListener(this);
        viewHolder.venderBtn.setTag(position);

        viewHolder.deleteBtn.setOnClickListener(this);
        viewHolder.deleteBtn.setTag(position);

        return convertView;
    }

}
