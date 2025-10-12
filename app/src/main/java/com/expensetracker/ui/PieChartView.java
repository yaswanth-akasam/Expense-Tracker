package com.expensetracker.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {
    private Paint paint;
    private RectF rectF;
    private List<PieSlice> slices = new ArrayList<>();
    
    public static class PieSlice {
        public String label;
        public double value;
        public int color;
        
        public PieSlice(String label, double value, int color) {
            this.label = label;
            this.value = value;
            this.color = color;
        }
    }

    public PieChartView(Context context) {
        super(context);
        init();
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
    }

    public void setData(List<PieSlice> slices) {
        this.slices = slices;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if (slices.isEmpty()) return;
        
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 60;
        
        rectF.set(width/2 - radius, height/2 - radius, width/2 + radius, height/2 + radius);
        
        double total = slices.stream().mapToDouble(s -> s.value).sum();
        float startAngle = 0;
        
        // Draw slices
        for (PieSlice slice : slices) {
            float sweepAngle = (float) (360 * slice.value / total);
            
            paint.setColor(slice.color);
            canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
            
            startAngle += sweepAngle;
        }
        
        // Draw labels
        startAngle = 0;
        paint.setColor(Color.WHITE);
        paint.setTextSize(22);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
        
        for (PieSlice slice : slices) {
            float sweepAngle = (float) (360 * slice.value / total);
            
            if (sweepAngle > 25) { // Only show label if slice is large enough
                float angle = startAngle + sweepAngle / 2;
                float x = (float) (width/2 + (radius * 0.6) * Math.cos(Math.toRadians(angle)));
                float y = (float) (height/2 + (radius * 0.6) * Math.sin(Math.toRadians(angle)));
                
                // Draw category name
                String categoryName = slice.label.length() > 6 ? slice.label.substring(0, 6) : slice.label;
                canvas.drawText(categoryName, x, y - 12, paint);
                
                // Draw percentage below name
                String percentage = String.format("%.0f%%", (slice.value / total) * 100);
                paint.setTextSize(18);
                canvas.drawText(percentage, x, y + 18, paint);
                paint.setTextSize(22); // Reset text size
            }
            
            startAngle += sweepAngle;
        }
    }
}