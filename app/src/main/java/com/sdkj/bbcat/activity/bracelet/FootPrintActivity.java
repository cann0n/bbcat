package com.sdkj.bbcat.activity.bracelet;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;

import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.GrowthVo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Rhino} on 2016/1/25 19:00
 * 成长足迹
 */
public class FootPrintActivity extends SimpleActivity {

    @ViewInject(R.id.ll_chart)
    LinearLayout ll_chart;


    @ViewInject(R.id.bra_vwselectorone)
    private View line1;

    @ViewInject(R.id.bra_vwselectortwo)
    private View line2;

    @ViewInject(R.id.bra_vwselectorthree)
    private View line3;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    @Override
    public void initBusiness() {
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < 1; i++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2, 13.9});
        values.add(new double[]{9 + 1, 10 + 1, 11 + 1, 15 + 2, 19 + 3, 23 + 1, 26 + 2, 25 + 2, 22 + 2, 18 + 4, 13 + 1, 11});
        values.add(new double[]{9 + 2, 10 + 2, 11 + 2, 15 + 3, 19 + 2, 23 + 2, 26 + 3, 25 + 3, 22 + 4, 18 + 3, 13 + 2, 13});
        values.add(new double[]{9 + 3, 10 + 3, 11 + 3, 15 + 4, 19 + 1, 23 + 3, 26 + 4, 25 + 4, 22 + 3, 18 + 6, 13 + 4, 16});
        int[] colors = new int[]{Color.GREEN, Color.rgb(200, 150, 0), Color.RED, Color.BLUE};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE, PointStyle.CIRCLE};
        mRenderer = buildRenderer(colors, styles);
        mRenderer.setPointSize(5.5f);
        int length = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = (XYSeriesRenderer) mRenderer.getSeriesRendererAt(i);
            r.setLineWidth(5);
            r.setFillPoints(true);
        }
        setChartSettings(mRenderer, "", "月份", "", 0.5, 12.5, 0, 40, Color.parseColor("#666666"),  Color.parseColor("#666666"));
        mRenderer.setXLabels(12);
        mRenderer.setYLabels(20);
        mRenderer.setShowGrid(true);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);

        mRenderer.setPanLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setZoomLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setClickEnabled(false);
        mRenderer.setSelectableBuffer(10);

        mDataset = buildDataset(x, values);
        String[] types = new String[]{LineChart.TYPE, LineChart.TYPE, LineChart.TYPE, LineChart.TYPE};
        GraphicalView mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mChartView.setBackgroundColor(Color.parseColor("#F6F6F6"));

        
        ll_chart.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    protected XYMultipleSeriesDataset buildDataset(List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, xValues, yValues, 0);
        return dataset;
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, List<double[]> xValues, List<double[]> yValues, int scale) {
        int length = 4;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries("标题" + i, scale);
            double[] xV = xValues.get(0);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }


    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        setRenderer(renderer, colors, styles);
        return renderer;
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setChartTitleTextSize(30);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);

        renderer.setPanEnabled(false, false);
        renderer.setZoomButtonsVisible(false);
        renderer.setZoomEnabled(false);
        renderer.setExternalZoomEnabled(false);
        renderer.setApplyBackgroundColor(true);
        renderer.setMarginsColor(Color.parseColor("#F6F6F6"));
        renderer.setBackgroundColor(Color.parseColor("#F6F6F6"));
        renderer.setLabelsColor(Color.parseColor("#666666"));
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(20);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(20);
        renderer.setLegendTextSize(20);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{20, 30, 50, 20});
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
    }

    public void changeBtn(int position) {
        if (position == 0) {
            line1.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            line2.setVisibility(View.VISIBLE);
            line1.setVisibility(View.INVISIBLE);
            line3.setVisibility(View.INVISIBLE);
        } else {
            line3.setVisibility(View.VISIBLE);
            line2.setVisibility(View.INVISIBLE);
            line1.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.tv_ring)
    void ring(View view) {
        changeBtn(0);
    }


    @OnClick(R.id.tv_footprint)
    void footPrint(View view) {
        changeBtn(1);
    }
    
    @OnClick(R.id.tv_head)
    void head(View view){
        changeBtn(2);
    }

    @OnClick(R.id.iv_back)
    void back(View view) {
        finish();
    }

    @OnClick(R.id.iv_edit)
    void edit(View view) {
        skip(BodyFeaturesActivity.class, (GrowthVo.BobyState) getVo("0"));
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_foot_print;
    }
}
