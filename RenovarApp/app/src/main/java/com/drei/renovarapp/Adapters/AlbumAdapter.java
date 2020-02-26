package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.Dialogs.FaceDialogFragment;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ResultDB> resultList;
    private FragmentActivity fragmentActivity;

    public AlbumAdapter(FragmentActivity fragmentActivity, Context context, List<ResultDB> resultList) {
        this.context = context;
        this.resultList = resultList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AlbumViewHolder resultViewHolder = (AlbumViewHolder) holder;
        resultViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageResult;
        private TextView txtDate;
        private View view;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);

            imageResult = itemView.findViewById(R.id.imageResult);
            txtDate = itemView.findViewById(R.id.txtDate);
            view = itemView;
        }

        public void bindData(final int position) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(resultList.get(position).getTime_millis());
            setDateText(calendar);
            File fileImage = new File(resultList.get(position).getPath_after());
            Picasso.get().load(fileImage).fit().centerInside()
                    .into(imageResult);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FaceDialogFragment dialogFragment = new FaceDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putLong("id", resultList.get(position).getTime_millis());
                    bundle.putString("after", resultList.get(position).getPath_after());
                    bundle.putString("before", resultList.get(position).getPath_before());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(fragmentActivity.getSupportFragmentManager(), "dialog_face");
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