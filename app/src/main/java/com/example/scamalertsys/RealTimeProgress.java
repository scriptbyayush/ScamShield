package com.example.scamalertsys;


import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class RealTimeProgress extends View {

    private int scamProgress = 0;
    private int normalProgress = 0;

    private Paint backgroundPaint;
    private Paint scamPaint;
    private Paint normalPaint;
    private Paint textPaint;
    private RectF rectF;

    public RealTimeProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Background circle paint.
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20f);
        backgroundPaint.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        // Red paint for scam calls.
        scamPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scamPaint.setStyle(Paint.Style.STROKE);
        scamPaint.setStrokeWidth(20f);
        scamPaint.setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));

        // Green paint for normal calls.
        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(20f);
        normalPaint.setColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));

        // Text paint for drawing the total count.
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(48f);
        textPaint.setColor(ContextCompat.getColor(context, android.R.color.black));
        textPaint.setTextAlign(Paint.Align.CENTER);

        // Apply Product Sans font
        Typeface typeface = ResourcesCompat.getFont(context, R.font.productsans);
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }

        // Rectangle for drawing arcs.
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY) - 20f;

        // Define bounds for drawing the arcs.
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Draw background circle.
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint);

        int total = scamProgress + normalProgress;
        if (total == 0) return;

        // Calculate percentage for each segment.
        float scamPercentage = scamProgress / (float) total;
        float normalPercentage = normalProgress / (float) total;

        // Dynamic color: interpolate between light and dark colors.
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int lowScamColor = Color.parseColor("#FF2400");   // Light red.
        int highScamColor = Color.parseColor("#ff2400");    // Dark red.
        int currentScamColor = (int) evaluator.evaluate(scamPercentage, lowScamColor, highScamColor);
        scamPaint.setColor(currentScamColor);

        int lowNormalColor = Color.parseColor("#32cd32");   // Light green.
        int highNormalColor = Color.parseColor("#32cd32");    // Dark green.
        int currentNormalColor = (int) evaluator.evaluate(normalPercentage, lowNormalColor, highNormalColor);
        normalPaint.setColor(currentNormalColor);

        // Calculate angles.
        float scamAngle = scamPercentage * 360f;
        float normalAngle = normalPercentage * 360f;

        // Draw arcs: start at -90° so the progress begins at the top.
        canvas.drawArc(rectF, -90f, scamAngle, false, scamPaint);
        canvas.drawArc(rectF, -90f + scamAngle, normalAngle, false, normalPaint);

        // Draw the total number of calls in the center.
        canvas.drawText("Danger:" + scamProgress, centerX, centerY - (textPaint.getTextSize() / 3), textPaint);
        canvas.drawText("Safe:" + normalProgress, centerX, centerY + (textPaint.getTextSize()), textPaint);



    }

    // Getter and setter for scamProgress (for animation).
    public int getScamProgress() {
        return scamProgress;
    }

    public void setScamProgress(int scamProgress) {
        this.scamProgress = scamProgress;
        invalidate();
    }

    // Getter and setter for normalProgress (for animation).
    public int getNormalProgress() {
        return normalProgress;
    }

    public void setNormalProgress(int normalProgress) {
        this.normalProgress = normalProgress;
        invalidate();
    }

    /**
     * Update the progress with smooth animations.
     *
     * @param newScam   New count for scam calls.
     * @param newNormal New count for normal calls.
     */
    public void setProgressAnimated(int newScam, int newNormal) {
        ObjectAnimator animatorScam = ObjectAnimator.ofInt(this, "scamProgress", scamProgress, newScam);
        ObjectAnimator animatorNormal = ObjectAnimator.ofInt(this, "normalProgress", normalProgress, newNormal);
        animatorScam.setDuration(1000);   // 1 second animation.
        animatorNormal.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorScam, animatorNormal);
        animatorSet.start();
    }
}
