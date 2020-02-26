package com.drei.renovarapp.FaceChange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Transformers.DefaultTransformer;
import com.drei.renovarapp.Database.ResultDB;
import com.drei.renovarapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlayProgressActivity extends AppCompatActivity {

    List<ResultDB> resultList;
    SliderLayout sliderProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_progress);
        resultList = new ArrayList<>();
        sliderProgress = findViewById(R.id.sliderProgress);

        sliderProgress.setDuration(1000);
        sliderProgress.setPagerTransformer(true,new DefaultTransformer());
        setData();
    }

    public void setData() {
        ResultDB resultDB = ResultDB.getInstance(this);
        resultDB.open();
        resultList = resultDB.getResultList(String.valueOf(getIntent().getIntExtra("id",0)));
        resultDB.close();

        for(ResultDB result : resultList)
        {
            TextSliderView defaultSliderView = new TextSliderView(this);
            defaultSliderView.setPicasso(Picasso.get());
            File file = new File(result.getPath_after());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(result.getTime_millis());
            defaultSliderView.description(setDateText(calendar));
            defaultSliderView.image(file);
            defaultSliderView.setScaleType(BaseSliderView.ScaleType.CenterCrop);
            sliderProgress.addSlider(defaultSliderView);
        }
    }

    public String setDateText(Calendar calendarNow) {
        final DateFormat format = new DateFormat();
        Calendar compare = Calendar.getInstance();
        return format.format("MMMM dd, yyyy", calendarNow).toString();
    }
}
