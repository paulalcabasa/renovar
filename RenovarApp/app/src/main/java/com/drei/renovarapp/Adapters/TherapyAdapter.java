package com.drei.renovarapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drei.renovarapp.Database.TherapyDB;
import com.drei.renovarapp.Dialogs.TherapyDialogFragment;
import com.drei.renovarapp.R;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class TherapyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<TherapyDB> therapyList;
    private FragmentActivity fragmentActivity;

    public TherapyAdapter(Context context, List<TherapyDB> therapyList, FragmentActivity fragmentActivity) {
        this.context = context;
        this.therapyList = therapyList;
        this.fragmentActivity = fragmentActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_therapy,parent,false);
        return new TherapyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TherapyViewHolder therapyViewHolder = (TherapyViewHolder)holder;
        therapyViewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return therapyList.size();
    }

    public class TherapyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName,txtTime,txtSchedule;
        private View view;


        public TherapyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.productName);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtSchedule = itemView.findViewById(R.id.txtSchedule);
            view = itemView;
        }

        public void bindData(final int position)
        {
            txtName.setText(therapyList.get(position).getName());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(therapyList.get(position).getTime_millis());
            setCalendarText(calendar);

            if(therapyList.get(position).getDuration() == 1) {
                txtSchedule.setText("Everyday");
            }
            else
            {
                txtSchedule.setText("Every " + therapyList.get(position).getDuration() + " days");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, therapyList.get(position).getId() + "", Toast.LENGTH_SHORT).show();
                    TherapyDialogFragment dialogFragment = new TherapyDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",therapyList.get(position).getId());
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(fragmentActivity.getSupportFragmentManager(),"dialog_product");
                }
            });
        }

        public void setCalendarText(Calendar calendar)
        {
            String am_pm = "";
            if (calendar.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (calendar.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";

            String strHrsToShow = (calendar.get(Calendar.HOUR) == 0) ?"12":calendar.get(Calendar.HOUR)+"";
            txtTime.setText(strHrsToShow+":"+String.format("%02d",calendar.get(Calendar.MINUTE))+" "+am_pm);
        }
    }
}
