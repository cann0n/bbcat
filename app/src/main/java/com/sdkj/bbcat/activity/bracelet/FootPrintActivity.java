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

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    @Override
    public void initBusiness() {
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < 4; i++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }
        List<double[]> values = new ArrayList<double[]>();
        values.add(new double[]{12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2, 13.9});
        values.add(new double[]{9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10});
        values.add(new double[]{9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10});
        values.add(new double[]{9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10});
        int[] colors = new int[]{Color.GREEN, Color.rgb(200, 150, 0)};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE, PointStyle.CIRCLE,PointStyle.CIRCLE,PointStyle.CIRCLE};
        mRenderer = buildRenderer(colors, styles);
        mRenderer.setPointSize(5.5f);
        int length = mRenderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = (XYSeriesRenderer) mRenderer.getSeriesRendererAt(i);
            r.setLineWidth(5);
            r.setFillPoints(true);
        }
        setChartSettings(mRenderer, "成长曲线", "月份", "", 0.5, 12.5, 0, 40, Color.LTGRAY, Color.LTGRAY);
        mRenderer.setXLabels(12);
        mRenderer.setYLabels(10);
        mRenderer.setShowGrid(true);
        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPanLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setZoomLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setClickEnabled(false);
        mRenderer.setSelectableBuffer(10);

        mDataset = buildDataset(x, values);
        String[] types = new String[]{LineChart.TYPE, LineChart.TYPE, LineChart.TYPE, LineChart.TYPE};
        GraphicalView mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        ll_chart.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
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
            double[] xV = xValues.get(i);
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
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setPointSize(5f);
        renderer.setMargins(new int[]{20, 30, 15, 20});
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(colors[i]);
            r.setPointStyle(styles[i]);
            renderer.addSeriesRenderer(r);
        }
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
