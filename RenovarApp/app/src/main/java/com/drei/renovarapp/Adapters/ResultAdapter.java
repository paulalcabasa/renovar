package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Activities.AlbumActivity;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.Dialogs.FaceDialogFragment;
import com.drei.renovarapp.Dialogs.TherapyDialogFragment;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ResultDB> resultList;
    private FragmentActivity fragmentActivity;

    public ResultAdapter(FragmentActivity fragmentActivity, Context context, List<ResultDB> resultList) {
        this.context = context;
        this.resultList = resultList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_result,parent,false);

       return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ResultViewHolder resultViewHolder = (ResultViewHolder)holder;
        resultViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageResult;
        private TextView txtDate;
        private View view;
        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            imageResult = itemView.findViewById(R.id.imageResult);
            txtDate = itemView.findViewById(R.id.txtDate);
            view = itemView;
        }

        public void bindData(final int position)
        {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTimeInMillis(resultList.get(position).getTime_millis());
//            setDateText(calendar);
            File fileImage = new File(resultList.get(position).getPath_after());
            Picasso.get().load(fileImage).fit().centerInside()
                    .into(imageResult);

            TherapyDB therapyDB = TherapyDB.getInstance(context);
            therapyDB.open();
            txtDate.setText(therapyDB.getTherapyList(String.valueOf(resultList.get(position).getId())).get(0).getName());
            therapyDB.close();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,AlbumActivity.class);
                    intent.putExtra("id",resultList.get(position).getId());
                    context.startActivity(intent);
                }
            });
        }

        public void setDateText(Calendar calendarNow) {
            final DateFormat format = new DateFormat();
            Calendar compare = Calendar.getInstance();
            if (!(calendarNow == compare)) {
                txtDate.setText(format.format("MMMM dd, yyyy", calendarNow));
            } else {
                txtDate.setText("Today" + format.format(", dd MMMM", calendarNow));
            }
        }
    }
}
