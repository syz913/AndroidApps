package com.example.shi.fd;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.example.shi.fd.R;

public class MainActivity extends Activity {

    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) this.findViewById(R.id.imageview);
        imageView.setOnTouchListener(new TouchListener());
    }

    
    private final class TouchListener implements OnTouchListener {
        private PointF startPoint = new PointF();
        private Matrix matrix = new Matrix();
        private Matrix currentMatrix = new Matrix();
        private int mode = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
        private float startDis;// 开始距离
        private PointF midPoint;// 中间点
        private double startAngle;// 开始角度

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:// 手指压下屏幕
                    Log.e("onTouch", "ACTION_DOWN");
                    mode = DRAG;
                    currentMatrix.set(imageView.getImageMatrix());// 记录ImageView当前的移动位置
                    startPoint.set(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:// 手指在屏幕移动，该 事件会不断地触发
                    // Log.e("onTouch", "ACTION_MOVE");
                    if (mode == DRAG) {
                        float dx = event.getX() - startPoint.x;// 得到在x轴的移动距离
                        float dy = event.getY() - startPoint.y;// 得到在y轴的移动距离
                        matrix.set(currentMatrix);// 在没有进行移动之前的位置基础上进行移动
                        matrix.postTranslate(dx, dy);
                    } else if (mode == ZOOM) {// 缩放
                        float endDis = distance(event);// 结束距离
                        int trunAngel = (int) (angle(event) - startAngle);// 变化的角度
                        // Log.v("ACTION_MOVE", "trunAngel="+trunAngel);
                        if (endDis > 10f) {
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                            Log.v("ACTION_MOVE", "imageView.getHeight()="
                                    + imageView.getHeight());
                            Log.v("ACTION_MOVE", "imageView.getWidth()="
                                    + imageView.getWidth());
                            if (Math.abs(trunAngel) > 5) {
                                // 设置变化的角度
                                matrix.postRotate(trunAngel, midPoint.x, midPoint.y);
                            }

                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:// 手指离开屏
                    // Log.e("onTouch", "ACTION_UP");
                    break;
                case MotionEvent.ACTION_POINTER_UP:// 有手指离开屏幕,但屏幕还有触点（手指）
                    // Log.e("onTouch", "ACTION_POINTER_UP");
                    mode = 0;
                    break;

                case MotionEvent.ACTION_POINTER_DOWN:// 当屏幕上还有触点（手指），再有一个手指压下屏幕
                    // Log.e("onTouch", "ACTION_POINTER_DOWN");
                    mode = ZOOM;
                    startDis = distance(event);
                    startAngle = angle(event);
                    if (startDis > 10f) {
                        midPoint = mid(event);
                        currentMatrix.set(imageView.getImageMatrix());// 记录ImageView当前的缩放倍数
                    }
                    break;
            }
            // Bitmap
            // bitmap0=((BitmapDrawable)getResources().getDrawable(R.drawable.test2)).getBitmap();
            // LayerDrawable layerDrawable=LayerDrawable.
            // Bitmap bitmap=Bitmap.createBitmap(bitmap0, x, y, width, height,
            // m, filter)
            imageView.setImageMatrix(matrix);
            return true;
        }

    }

    /**
     * 计算两点之间的距离
     *
     * @param event
     * @return
     */
    public static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算两点之间的中间点
     *
     * @param event
     * @return
     */
    public static PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }

    /**
     * 计算两个手指连线与坐标轴的角度（单位为。C）
     *
     * @param event
     * @return
     */
    public static double angle(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }
}
