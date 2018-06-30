package com.example.admin.quran;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    private List<Surah> datalist;
    private Context context;
    Utils utils=new Utils();
    int index;

    public SurahAdapter(List <Surah> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;
    }

    @NonNull
    @Override
    public SurahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.ayalistitem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SurahAdapter.ViewHolder holder, final int position) {
        holder.id.setText(datalist.get(position).getStand());
        holder.name.setText(datalist.get(position).getName());
        final boolean isavailable=utils.checksdcardisvaillable();
        final boolean isConnected = ((MainActivity)context).isNetworkAvailable();
        boolean isexist=((MainActivity)context).checkFileIsExist(datalist.get(holder.getAdapterPosition()).getFilename()+".mp3");



        final String root= Environment.getExternalStorageDirectory().toString();

            if(isexist){
                holder.install.setVisibility(View.INVISIBLE);
                holder.install.setEnabled(false);
            }else{
                holder.install.setVisibility(View.VISIBLE);
                holder.install.setEnabled(true);
                holder.install.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        index =position;
                        if(isConnected && isavailable){
                            ((MainActivity) context).downloadfile(datalist.get(holder.getAdapterPosition()).getUrl());
                            notifyDataSetChanged();
                        }else if (!isConnected || !isavailable){
                            Toast.makeText(context,"The file can not be loaded. Make sure you connect to the internet and have an SD card",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }



    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,id;
        ImageView install;
        LinearLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.surahnamelist);
            id = (TextView) itemView.findViewById(R.id.surahidlist);
            install = (ImageView) itemView.findViewById(R.id.installlistimg);
            relativeLayout = (LinearLayout) itemView.findViewById(R.id.surahlayout);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,PlayerActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("index",getAdapterPosition());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }

    }
}
