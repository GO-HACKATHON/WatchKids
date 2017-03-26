package setia.example.com.watchkids.KidsActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import setia.example.com.watchkids.Model.DataMessage;
import setia.example.com.watchkids.R;

/**
 * Created by My Computer on 3/26/2017.
 */

public class MessageAdapter extends ArrayAdapter<DataMessage> {

    /**
     * Created by My Computer on 5/14/2016.
     */

        public Context context;
        public ArrayList<DataMessage> laundries;

        static class ViewHolder {
            public TextView tv_anak;
            public TextView tv_pesan;
            public TextView tv_status_pesan;
        }

        public MessageAdapter(Context context, ArrayList<DataMessage> datamessages) {
            super(context, R.layout.single_list_message, datamessages);
            this.context = context;
            //imgLoader = new ImageLoader(context);
            this.laundries = datamessages;
        }

        public void setData(ArrayList<DataMessage> datamessages) {
            this.laundries.clear();
            for (int i = 0; i < datamessages.size(); i++) {
                this.laundries.add(datamessages.get(i));
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final DataMessage produk = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                //membuat baru item
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater. from(getContext());
                convertView = inflater.inflate(R.layout.single_list_message, parent,
                        false);
                viewHolder.tv_anak = (TextView)
                        convertView.findViewById(R.id.tv_anak);
                viewHolder.tv_pesan = (TextView)
                        convertView.findViewById(R.id.tv_pesan);
                viewHolder.tv_status_pesan = (TextView)
                        convertView.findViewById(R.id.tv_status_pesan);
                convertView.setTag(viewHolder);
            } else {
                //menggunakan item yang sudah pernah dibuat
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // Set item dengan value dari objek
            viewHolder.tv_anak.setText(produk.getKidsFirstname());
            viewHolder.tv_pesan.setText(produk.getMessage());
            viewHolder.tv_status_pesan.setText(produk.getStatus());


            return convertView;
        }
    }


