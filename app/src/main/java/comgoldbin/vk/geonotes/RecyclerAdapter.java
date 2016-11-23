/**
 * Created by Goldbin on 11.10.2016.
 */

package comgoldbin.vk.geonotes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.ArrayList;

import static android.net.Uri.*;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public long pol;

    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textnote;
        public TextView datenote;
        public TextView myid;
        public ImageView imgnote;
        public long pol;


        public ViewHolder(View v) {
            super(v);
            textnote = (TextView) v.findViewById(R.id.note_text_item);
            datenote = (TextView) v.findViewById(R.id.note_date_item);
            imgnote = (ImageView) v.findViewById(R.id.note_photo_item);
            myid = (TextView) v.findViewById(R.id.note_myidbd_item);

            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AddActivity.class);
                    intent.putExtra("iddbd", pol);
                    context.startActivity(intent);
                }
            });
        }
    }


  ArrayList<DBData> dBData;

    // Конструктор
    public  RecyclerAdapter(ArrayList<DBData> datenotes) {
        this.dBData = datenotes;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // Создаем новые вью
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public long getItemId (int position) {
        DBData dbd = dBData.get(position);
        if (dbd != null) {
            pol = dbd.getID();
        }
        return 0;
    }

    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
        DBData dbd = dBData.get(i);
        holder.textnote.setText(dbd.getText());
        holder.datenote.setText(dbd.getDate());
        holder.imgnote.setImageURI(Uri.parse(dbd.getPhoto().toString()));
        holder.pol = dbd.getID();
    }

    // Возвращает размер данных (вызывается layout manager-ом)
   @Override
    public int getItemCount() {
       return dBData.size();
   }

}