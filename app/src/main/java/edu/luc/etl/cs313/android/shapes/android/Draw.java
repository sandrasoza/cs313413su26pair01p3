package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    private final Canvas canvas;
    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        final int oldColor = paint.getColor();

        paint.setColor(c.getColor());
        c.getShape().accept(this);
        paint.setColor(oldColor);

        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        final Style oldStyle = paint.getStyle();

        paint.setStyle(Style.FILL_AND_STROKE);
        f.getShape().accept(this);
        paint.setStyle(oldStyle);

        return null;
    }

    @Override
    public Void onGroup(final Group g) {
        for (Shape s : g.getShapes()) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.translate(l.getX(), l.getY());

        l.getShape().accept(this);

        canvas.translate(-l.getX(), -l.getY());

        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0, 0, r.getWidth(), r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(final Outline o) {
        final Style oldStyle = paint.getStyle();

        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        paint.setStyle(oldStyle);

        return null;
    }

    @Override
    public Void onPolygon(final Polygon p) {
        final int n = p.getPoints().size();
        final float[] pts = new float[n * 4];

        for (int i = 0; i < n; i++) {
            Point a = p.getPoints().get(i);
            Point b = p.getPoints().get((i + 1) % n);

            pts[i * 4] = a.getX();
            pts[i * 4 + 1] = a.getY();
            pts[i * 4 + 2] = b.getX();
            pts[i * 4 + 3] = b.getY();
        }

        canvas.drawLines(pts, paint);
        return null;
    }
}
