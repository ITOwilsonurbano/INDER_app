package com.itosoftware.inderandroid.Adapters;


        import android.content.Context;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;

        import com.android.volley.RequestQueue;
        import com.android.volley.toolbox.ImageLoader;
        import com.itosoftware.inderandroid.Api.Reservations.Reservations;
        import com.itosoftware.inderandroid.Api.SportsScenarios.SportsScenarios;
        import com.itosoftware.inderandroid.R;


public class ReservationAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<Reservations> datos;

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public ReservationAdapter(Context context, ArrayList datos) {
        super(context, R.layout.reservation_row, datos);
        // Guardamos los parámetros en variables de clase.
        this.context = context;
        this.datos = datos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View item = inflater.inflate(R.layout.reservation_row, null);

        TextView nameSport = (TextView) item.findViewById(R.id.reservation_row_one_name_sport);


        SportsScenarios sportsScenario =  this.datos.get(position).getSportId();


        nameSport.setText(sportsScenario.getInfo().get("nombre").toString());

        View circle = (View) item.findViewById(R.id.reservation_circle);
        TextView status = (TextView) item.findViewById(R.id.reservation_status);
        
        if(this.datos.get(position).getStatus().equals("rechazado")) {
            circle.setBackgroundResource(R.drawable.circle_red);
            status.setText("Rechazada");
            status.setTextColor(getContext().getResources().getColor(R.color.Rojo));
        }
        if(this.datos.get(position).getStatus().equals("pendiente")) {
            circle.setBackgroundResource(R.drawable.circle_orange);
            status.setText("En Proceso");
            status.setTextColor(getContext().getResources().getColor(R.color.Naranja));
        }
        if(this.datos.get(position).getStatus().equals("aprobado")) {
            circle.setBackgroundResource(R.drawable.circle_green);
            status.setText("Aprobada");
            status.setTextColor(getContext().getResources().getColor(R.color.Verde));
        }

        TextView date = (TextView) item.findViewById(R.id.reservation_date);
        String startDate = this.datos.get(position).getInicio();
        String[] startDate_a = startDate.split(" ");
        String endDate = this.datos.get(position).getFin();
        String[] endDates = endDate.split(" ");
        String dateText = startDate_a[0] + "\n " + startDate_a[1] + " - " + endDates[1];
        date.setText(dateText);

        TextView content = (TextView) item.findViewById(R.id.reservation_row_discipline);
        content.setText(this.datos.get(position).getDiscipline().getNombre());

        return item;
    }

}
