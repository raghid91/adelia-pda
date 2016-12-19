package com.example.yannick.adelia_pda;

/*  Name:       Yannick Decosse
 *  McGill id:  260551160
 *  Subject:    Assignment 3 & 4 - TODOList App
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class BarChart extends View
{
    Paint statTitle;
    Paint linePaint;
    Paint date1Title;
    Paint date2Title;
    Paint date3Title;
    Paint date4Title;
    Paint barColumn1;
    Paint barColumn2;
    Paint barColumn3;
    Paint barColumn4;
    String date1;
    String date2;
    String date3;
    String date4;
    int stat1;
    int stat2;
    int stat3;
    int stat4;
    float scaleFactor;

    public BarChart(Context context)
    {
        super(context);
        initialise();
    }

    void initialise()
    {
        /* Calculate Scale Factor */
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        scaleFactor = metrics.density;
        /* Initialize color for dates and columns */
        statTitle = new Paint();
        linePaint = new Paint();
        date1Title = new Paint();
        date2Title = new Paint();
        date3Title = new Paint();
        date4Title = new Paint();
        barColumn1 = new Paint();
        barColumn2 = new Paint();
        barColumn3 = new Paint();
        barColumn4 = new Paint();
        /* Setting colors of elements */
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.rgb(147, 147, 147));
        statTitle.setColor(Color.rgb(25, 0, 51));
        barColumn1.setColor(Color.rgb(51, 102, 255));
        barColumn2.setColor(Color.rgb(245, 184, 5));
        barColumn3.setColor(Color.rgb(100, 225, 245));
        barColumn4.setColor(Color.rgb(216, 100, 216));
        date1Title.setColor(Color.rgb(25, 0, 51));
        date2Title.setColor(Color.rgb(25, 0, 51));
        date3Title.setColor(Color.rgb(25, 0, 51));
        date4Title.setColor(Color.rgb(25, 0, 51));
        /* Setting size of text */
        statTitle.setTextSize(13 * scaleFactor);
        date1Title.setTextSize(10 * scaleFactor);
        date2Title.setTextSize(10 * scaleFactor);
        date3Title.setTextSize(10 * scaleFactor);
        date4Title.setTextSize(10 * scaleFactor);
    }

    /* Methods to set values of columns */
    public void setStat1 (String date1, int stat1)
    {
        this.date1 = date1;
        this.stat1 = stat1;
        invalidate();
    }

    public void setStat2 (String date2, int stat2)
    {
        this.date2 = date2;
        this.stat2 = stat2;
        invalidate();
    }

    public void setStat3 (String date3, int stat3)
    {
        this.date3 = date3;
        this.stat3 = stat3;
        invalidate();
    }

    public void setStat4 (String date4, int stat4)
    {
        this.date4 = date4;
        this.stat4 = stat4;
        invalidate();
    }

    /* Draw the elements */
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int fullWidth = getWidth();
        int fullHeight = getHeight();
        float padding = (10 * scaleFactor);
        float maxBarHeight = fullHeight - 10 * padding;
        float col1Height;
        float col2Height;
        float col3Height;
        float col4Height;

        /* Calculate highest column, max column is 4 */
        if (stat2 > stat1 && stat2 > stat3 && stat2 > stat4)
        {
            col2Height = maxBarHeight;
            col1Height = ((float) stat1 / stat2) * maxBarHeight;
            col3Height = ((float) stat3 / stat2) * maxBarHeight;
            col4Height = ((float) stat4 / stat2) * maxBarHeight;
        }

        else if (stat3 > stat1 && stat3 > stat2 && stat3 > stat4)
        {
            col3Height = maxBarHeight;
            col1Height = ((float) stat1 / stat3) * maxBarHeight;
            col2Height = ((float) stat2 / stat3) * maxBarHeight;
            col4Height = ((float) stat4 / stat3) * maxBarHeight;
        }

        else if (stat4 > stat1 && stat4 > stat2 && stat4 > stat3)
        {
            col4Height = maxBarHeight;
            col1Height = ((float) stat1 / stat4) * maxBarHeight;
            col2Height = ((float) stat2 / stat4) * maxBarHeight;
            col3Height = ((float) stat3 / stat4) * maxBarHeight;
        }

        else
        {
            col1Height = maxBarHeight;
            col2Height = ((float) stat2 / stat1) * maxBarHeight;
            col3Height = ((float) stat3 / stat1) * maxBarHeight;
            col4Height = ((float) stat4 / stat1) * maxBarHeight;
        }

        /* Draw graph lines behind columns */
        for (int x = 25; x < 375; x += 25)
        {
            canvas.drawLine(padding, fullHeight - x * scaleFactor, fullWidth - padding , fullHeight - x * scaleFactor, linePaint);
        }

        /* Set up variables to calculate width of columns */
        float oneFifth = (float) (fullWidth * 0.2);
        float twoFifth = (float) (fullWidth * 0.4);
        float thirdFifth = (float) (fullWidth * 0.6);
        float fourFifth = (float) (fullWidth * 0.8);
        int barBottom = fullHeight - 25 * 3;

        /* Draw first column */
        float bar1top = barBottom - col1Height;
        float val1pos = bar1top - padding;
        canvas.drawRect(padding, bar1top, oneFifth + padding / 2, barBottom, barColumn1);
        canvas.drawText(date1, padding, fullHeight - 15, date1Title);
        canvas.drawText(Integer.toString(stat1), oneFifth - padding * 4, val1pos, statTitle);

        /* Draw second column */
        float bar2top = barBottom - col2Height;
        float val2pos = bar2top - padding;
        canvas.drawRect((int)(oneFifth + padding * 3), bar2top, (int)(twoFifth + padding * 2.5), barBottom, barColumn2);
        canvas.drawText(date2, (int)(oneFifth + padding * 3), fullHeight - 15, date2Title);
        canvas.drawText(Integer.toString(stat2), twoFifth - padding * 2, val2pos, statTitle);

        /* Draw third column */
        float bar3top = barBottom - col3Height;
        float val3pos = bar3top - padding;
        canvas.drawRect((int)(thirdFifth - padding * 2), bar3top, (int)(fourFifth - padding * 2.5), barBottom, barColumn3);
        canvas.drawText(date3, (int)(thirdFifth - padding * 2), fullHeight - 15, date3Title);
        canvas.drawText(Integer.toString(stat3), thirdFifth, val3pos, statTitle);

        /* Draw fourth column */
        float bar4top = barBottom - col4Height;
        float val4pos = bar4top - padding;
        canvas.drawRect(fourFifth, bar4top, fullWidth - padding, barBottom, barColumn4);
        canvas.drawText(date4, fourFifth, fullHeight - 15, date4Title);
        canvas.drawText(Integer.toString(stat4), (int) (fullWidth - padding * 4.5), val4pos, statTitle);
    }
}