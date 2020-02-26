package com.drei.renovarapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drei.renovarapp.Adapters.AlbumAdapter;
import com.drei.renovarapp.Adapters.ResultAdapter;
import com.drei.renovarapp.Adapters.ViewPagerAdapter;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.FaceChange.PlayProgressActivity;
import com.drei.renovarapp.FaceChange.ProgressFragment;
import com.drei.renovarapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    //    private MaterialCalendarView calendarView;
//    private ViewPager pagerTherapies;
    private ViewPagerAdapter adapter;
    private List<Calendar> calendarList;
    //    private TextView txtDateNow;
    private List<ResultDB> resultList;
    private RecyclerView recyclerViewResults;
    private AlbumAdapter resultAdapter;
    private TextView txtNoRecord;
    private FloatingActionButton fabPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        txtNoRecord = findViewById(R.id.txtNoRecord);
        fabPlay = findViewById(R.id.fabPlay);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
//        calendarView = view.findViewById(R.id.calendarView);
//        pagerTherapies = view.findViewById(R.id.pagerTherapies);
//        txtDateNow = view.findViewById(R.id.txtDateNow);
        resultList = new ArrayList<>();

        setData();

        resultAdapter = new AlbumAdapter(this,this,resultList);
        recyclerViewResults.setLayoutManager(new GridLayoutManager(this,2));
        recyclerViewResults.setAdapter(resultAdapter);


//        setDateText(calendarNow);
//        calendarView.setSelectedDate(calendarNow);
//        calendarView.setDynamicHeightEnabled(true);

        fabPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlbumActivity.this, PlayProgressActivity.class);
                intent.putExtra("id",getIntent().getIntExtra("id",0));
                startActivity(intent);
            }
        });

    }

    public void setData() {
        ResultDB resultDB = ResultDB.getInstance(this);
        resultDB.open();
        resultList = resultDB.getResultList(String.valueOf(getIntent().getIntExtra("id",0)));
        resultDB.close();

        if(resultList.size() <= 0)
        {
            txtNoRecord.setVisibility(View.VISIBLE);
            fabPlay.hide();
        }
        else
        {
            txtNoRecord.setVisibility(View.GONE);
            fabPlay.show();
        }
    }
}
