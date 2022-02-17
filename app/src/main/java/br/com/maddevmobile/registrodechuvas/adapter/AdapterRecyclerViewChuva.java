package br.com.maddevmobile.registrodechuvas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.maddevmobile.registrodechuvas.R;
import br.com.maddevmobile.registrodechuvas.model.Chuva;

public  class AdapterRecyclerViewChuva extends RecyclerView.Adapter<AdapterRecyclerViewChuva.ViewHolder> {


    private Context context;
    private List<Chuva> chuvas = new ArrayList<>();
    private ClickChuva clickChuva;


    public AdapterRecyclerViewChuva() {
    }

    public AdapterRecyclerViewChuva(Context context, List<Chuva> chuvas, ClickChuva clickChuva){

        this.context = context;
        this.chuvas = chuvas;
        this.clickChuva = clickChuva;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.itemadapterchuva, parent, false);

        return new ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Chuva chuva = chuvas.get(position);

            holder.text_cidade.setText("Cidade : "+ chuva.getNomeCidade());
            holder.text_estado.setText("UF : "+chuva.getNomeEstado());
            holder.text_bairro.setText("Bairro: "+chuva.getNomeBairro());
            holder.text_chuva.setText("Chuva : "+chuva.getChuvaMM() + "mm");
            holder.text_data_hora.setText("Data/hora : "+chuva.getData() +" - "+ chuva.getHora());

            holder.image_excluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        clickChuva.onClickChuva( chuva );

                }
            });
    }

    @Override
    public int getItemCount() {
        return chuvas.size();
    }


    public void exlcuir(Chuva chuva){
            this.chuvas.remove(chuva);
            this.notifyDataSetChanged();

    }


    public void adiciona(Chuva chuva){
        this.chuvas.add(chuva);
        this.notifyItemInserted(getItemCount());

    }

    public interface ClickChuva{

        void onClickChuva(Chuva chuva);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView text_cidade, text_estado, text_bairro, text_chuva, text_data_hora;
        private ImageView image_excluir;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_cidade = itemView.findViewById(R.id.text_cidade);
            text_estado = itemView.findViewById(R.id.text_estado);
            text_bairro = itemView.findViewById(R.id.text_bairro);
            text_chuva = itemView.findViewById(R.id.text_chuva);
            text_data_hora = itemView.findViewById(R.id.text_data_hora);

            image_excluir = itemView.findViewById(R.id.image_excluir);


        }
    }


}
