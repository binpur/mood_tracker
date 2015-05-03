package com.example.gtqq;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PieChartBuilder extends Activity 
{
  /** Colors to be used for the pie slices. */
  private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW };
  /** The main series that will include all the data. */
  private CategorySeries mSeries = new CategorySeries("");
  /** The main renderer for the main dataset. */
  private DefaultRenderer mRenderer = new DefaultRenderer();
  /** Button for adding entered data to the current series. */
  private Button mAdd;
  /** Edit text field for entering the slice value. */
  private GraphicalView mChartView;
  private String [] moodTitles = {"Awesome ","Good ","OK ","Bad ","Terrible "};

  @Override
  protected void onRestoreInstanceState(Bundle savedState) 
  {
    super.onRestoreInstanceState(savedState);
    mSeries = (CategorySeries) savedState.getSerializable("current_series");
    mRenderer = (DefaultRenderer) savedState.getSerializable("current_renderer");
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) 
  {
    super.onSaveInstanceState(outState);
    outState.putSerializable("current_series", mSeries);
    outState.putSerializable("current_renderer", mRenderer);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.xy_chart);
    int [] values = getIntent().getIntArrayExtra("mood_of_the_week");
    mRenderer.setZoomButtonsVisible(true);
    mRenderer.setStartAngle(180);
    mRenderer.setDisplayValues(true);
    if(values != null && values.length != 0) {
        for(int i=0;i<5;i++)
        {
        	mSeries.add(moodTitles[i] + (mSeries.getItemCount() + 1), values[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }
    }
    else {
    	Toast.makeText(this, "No mood added !", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  protected void onResume() 
  {
    super.onResume();
    if (mChartView == null) 
    {
      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);
      mRenderer.setClickEnabled(true);
      mChartView.setOnClickListener(new View.OnClickListener() 
      {
        @Override
        public void onClick(View v) 
        {
          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
          if (seriesSelection == null) 
          {
          } 
          else 
          {
            for (int i = 0; i < mSeries.getItemCount(); i++) 
            {
              mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
            }
            mChartView.repaint();
            Toast.makeText(
                PieChartBuilder.this,
                "You have " + (int)seriesSelection.getValue() + " " + moodTitles[seriesSelection.getPointIndex()] +" day(s)", Toast.LENGTH_SHORT).show();
          }
        }
      });
      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,
          LayoutParams.FILL_PARENT));
    } 
    else 
    {
      mChartView.repaint();
    }
  }
}
