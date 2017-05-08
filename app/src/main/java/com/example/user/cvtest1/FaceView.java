package com.example.user.cvtest1;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.graphics.Path;
import android.graphics.Canvas.VertexMode;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;
import com.google.android.gms.vision.face.internal.client.LandmarkParcel;

/**
 * View which displays a bitmap containing a face along with overlay graphics that identify the
 * locations of detected facial landmarks.
 */
public class FaceView extends View {
    private Bitmap mBitmap;
    private SparseArray<Face> mFaces;

    public FaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the bitmap background and the associated face detections.
     */
    void setContent(Bitmap bitmap, SparseArray<Face> faces) {
        mBitmap = bitmap;
        mFaces = faces;
        invalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            drawFaceAnnotations(canvas, scale);
        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     * <p>
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private void drawFaceAnnotations(Canvas canvas, double scale) {
        PointF leftMPoint = new PointF();
        PointF rightMPoint = new PointF();
        PointF bottomMPoint = new PointF();

        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
            for (Landmark landmark : face.getLandmarks()) {
                if((landmark.getType() == landmark.BOTTOM_MOUTH) || (landmark.getType() == landmark.LEFT_MOUTH) || (landmark.getType() == landmark.RIGHT_MOUTH)){
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);

                    if(landmark.getType() == landmark.BOTTOM_MOUTH) {
                        bottomMPoint.x = cx;
                        bottomMPoint.y = cy;
                    }
                    if (landmark.getType() == landmark.LEFT_MOUTH) {
                        leftMPoint.x = cx;
                        leftMPoint.y = cy;
                    }
                    if (landmark.getType() == landmark.RIGHT_MOUTH){
                        rightMPoint.x = cx;
                        rightMPoint.y = cy;
                    }

                }else{
                    //Do nothing
                }
            }
        }




        Paint paint = new Paint();

        paint.setStrokeWidth(5);
        paint.setColor(android.graphics.Color.GREEN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        float [] points  = new float[8];
        points[0] = rightMPoint.x;
        points[1] = rightMPoint.y;
        points[2] = leftMPoint.x;
        points[3] = leftMPoint.y;
        points[4] = bottomMPoint.x;
        points[5] = bottomMPoint.y;
        points[6] = rightMPoint.x;
        points[7] = rightMPoint.y;

        canvas.drawVertices(VertexMode.TRIANGLES, 8, points, 0, null, 0, null, 0, null, 0, 0, paint);
        Path path = new Path();
        path.moveTo(rightMPoint.x , rightMPoint.y);
        path.lineTo(leftMPoint.x,leftMPoint.y);
        path.lineTo(bottomMPoint.x,bottomMPoint.y);
        canvas.drawPath(path,paint);
//
//        //Point 1 left mouth
//        //Point 2 right Mouth
//        //Point 3 Bottom Mouth
//        Path path = new Path();
//        path.setFillType(Path.FillType.EVEN_ODD);
//        path.moveTo(rightMPoint.x, rightMPoint.y);
//        path.lineTo(leftMPoint.x, leftMPoint.y);
//        path.moveTo(leftMPoint.x, leftMPoint.y);
//        path.lineTo(bottomMPoint.x, bottomMPoint.y);
//        path.moveTo(bottomMPoint.x, bottomMPoint.y);
//        path.lineTo(rightMPoint.x, rightMPoint.y);
//        path.close();
//
//        canvas.drawPath(path, paint);



    }
}
