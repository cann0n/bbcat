package com.sdkj.bbcat.activity.bracelet;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.huaxi100.networkapp.network.HttpUtils;
import com.huaxi100.networkapp.network.PostParams;
import com.huaxi100.networkapp.network.RespJSONObjectListener;
import com.huaxi100.networkapp.utils.GsonTools;
import com.huaxi100.networkapp.xutils.view.annotation.ViewInject;
import com.huaxi100.networkapp.xutils.view.annotation.event.OnClick;
import com.sdkj.bbcat.R;
import com.sdkj.bbcat.SimpleActivity;
import com.sdkj.bbcat.bean.BodyFeaturesHistoryVo;
import com.sdkj.bbcat.bean.GrowthVo;
import com.sdkj.bbcat.bean.RespVo;
import com.sdkj.bbcat.constValue.Const;
import com.sdkj.bbcat.constValue.SimpleUtils;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

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
    XYSeries A;
    XYSeries B;
    XYSeries C;
    XYSeries D;

    GraphicalView mChartView;

    BodyFeaturesHistoryVo vo;

    @Override
    public void initBusiness() {
        EventBus.getDefault().register(this);
        List<double[]> x = new ArrayList<double[]>();
        for (int i = 0; i < 1; i++) {
            x.add(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
        }
        List<Double[]> values = new ArrayList<Double[]>();
        values.add(new Double[]{0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D});
        values.add(new Double[]{0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D});
        values.add(new Double[]{0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D});
        values.add(new Double[]{0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D, 0D});
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
        setChartSettings(mRenderer, "", "月份", "", 0.5, 12.5, 0, 40, Color.parseColor("#666666"), Color.parseColor("#666666"));
        mRenderer.setXLabels(12);
        mRenderer.setYLabels(20);
        mRenderer.setShowGrid(true);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);

        mRenderer.setPanLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setZoomLimits(new double[]{-10, 20, -10, 40});
        mRenderer.setClickEnabled(false);
        mRenderer.setSelectableBuffer(10);


        String[] types = new String[]{LineChart.TYPE, LineChart.TYPE, LineChart.TYPE, LineChart.TYPE};
        mDataset = buildDataset(x, values);

        mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
        mChartView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mChartView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        ll_chart.addView(mChartView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        queryData();
    }

    protected XYMultipleSeriesDataset buildDataset(List<double[]> xValues, List<Double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, xValues, yValues, 0);
        return dataset;
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, List<double[]> xValues, List<Double[]> yValues, int scale) {

        A = new XYSeries("超标", scale);
        B = new XYSeries("宝宝", scale);
        C = new XYSeries("标准", scale);
        D = new XYSeries("低标", scale);
        dataset.addSeries(A);
        dataset.addSeries(B);
        dataset.addSeries(C);
        dataset.addSeries(D);
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
        renderer.setMargins(new int[]{30, 50, 50, 20});
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

    private void queryData() {
        HttpUtils.postJSONObject(activity, Const.GetBodyFeatures, SimpleUtils.buildUrl(activity, new PostParams()), new RespJSONObjectListener(activity) {
            @Override
            public void getResp(JSONObject jsonObject) {
                RespVo<BodyFeaturesHistoryVo> resp = GsonTools.getVo(jsonObject.toString(), RespVo.class);
                if (resp.isSuccess()) {
                    vo = resp.getData(jsonObject, BodyFeaturesHistoryVo.class);
                    drawLine(0,vo);
                }else{
                    toast(resp.getMessage());
                }
            }

            @Override
            public void doFailed() {
                System.out.println("jsonObject = faid");
            }
        });
    }

    private void drawLine(int type, BodyFeaturesHistoryVo vo) {
        try {
            A.clear();
            B.clear();
            C.clear();
            D.clear();
            if (type == 0) {
                mRenderer.setXAxisMin(0);
                mRenderer.setXAxisMax(12);
                mRenderer.setYAxisMin(0);
                mRenderer.setYAxisMax(80);
                mRenderer.setYTitle("单位(cm)");
                for (GrowthVo.BobyState state : vo.getLogs()) {
                    A.add(state.getMonth(), state.getMax_height());
                    B.add(state.getMonth(), Double.parseDouble(state.getHeight()));
                    C.add(state.getMonth(), state.getAvg_height());
                    D.add(state.getMonth(), state.getMin_height());
                }
            } else if (type == 1) {
                mRenderer.setXAxisMin(0);
                mRenderer.setXAxisMax(12);
                mRenderer.setYAxisMin(0);
                mRenderer.setYAxisMax(30);
                mRenderer.setXAxisMin(0);
                mRenderer.setXAxisMax(12);
                mRenderer.setYAxisMin(0);
                mRenderer.setYAxisMax(80);
                mRenderer.setYTitle("单位(kg)");
                for (GrowthVo.BobyState state : vo.getLogs()) {
                    A.add(state.getMonth(), state.getMax_weight());
                    B.add(state.getMonth(), Double.parseDouble(state.getWeight()));
                    C.add(state.getMonth(), state.getAvg_weight());
                    D.add(state.getMonth(), state.getMin_weight());
                }
            } else {
                mRenderer.setXAxisMin(0);
                mRenderer.setXAxisMax(12);
                mRenderer.setYAxisMin(0);
                mRenderer.setYAxisMax(40);
                mRenderer.setYTitle("单位(cm)");
                for (GrowthVo.BobyState state : vo.getLogs()) {
                    A.add(state.getMonth(), state.getMax_head());
                    B.add(state.getMonth(), Double.parseDouble(state.getHead()));
                    C.add(state.getMonth(), state.getAvg_head());
                    D.add(state.getMonth(), state.getMin_head());
                }
            }

            mChartView.repaint();
        } catch (Exception ex) {

        }
    }

    @OnClick(R.id.tv_ring)
    void ring(View view) {
        if (vo == null) {
            return;
        }
        drawLine(0, vo);
        changeBtn(0);
    }


    @OnClick(R.id.tv_footprint)
    void footPrint(View view) {
        if (vo == null) {
            return;
        }
        drawLine(1, vo);
        changeBtn(1);
    }

    @OnClick(R.id.tv_head)
    void head(View view) {
        if (vo == null) {
            return;
        }
        drawLine(2, vo);
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

    public void onEventMainThread(RefreshEvent event) {
        queryData();
    }
    
    public static class RefreshEvent{
        
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public int setLayoutResID() {
        return R.layout.activity_foot_print;
    }
}
