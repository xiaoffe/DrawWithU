package com.xiaoffe.drawwithu.widget.doodle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.List;

public class Doodle extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = "doodle";
    private SurfaceHolder mSurfaceHolder = null;

    //当前所选画笔的形状
    private Action mAction = null;
    private Action peerAction = null;
    //默认画笔为黑色
    private int mColor = Color.BLACK;
    private int peerColor = Color.BLACK;
    //画笔的粗细
    private int mSize = 4;
    private int peerSize = 4;
    //
    private ActionType mType = ActionType.Path;
    private ActionType peerType = ActionType.Path;
    //记录画笔的列表
    private List<Action> mActions = new ArrayList<>();
    //
    private Paint mPaint;
    /**
     * 设置画笔的颜色
     * @param color
     */
    public void setMyColor(String color) {mColor = Color.parseColor(color);}
    public int getMyColor(){
        return mColor;
    }
    public void setPeerColor(int color){
        peerColor = color;
    }
    public int getPeerColor(){return peerColor;}
    /**
     * 设置画笔的粗细
     * @param size
     */
    public void setMySize(int size) {
        mSize = size;
    }
    public int getMySize(){
        return mSize;
    }
    public void setPeerSize(int size){
        peerSize = size;
    }
    public int getPeerSize(){
        return peerSize;
    }
    /**
     *  设置type
     */
    public ActionType getMyType() {
        return mType;
    }
    public void setMyType(ActionType actionType){
        mType = actionType;
    }
    public ActionType getPeerType(){
        return peerType;
    }
    public void setPeerType(ActionType actionType){
        peerType = actionType;
    }
    /**
     * 回退
     * @return
     */
    public boolean back() {
        if (mActions != null && mActions.size() > 0) {
            mActions.remove(mActions.size() - 1);
            Canvas canvas = mSurfaceHolder.lockCanvas();
            canvas.drawColor(Color.parseColor("#ffcccccc"));
            for (Action a : mActions) {
                a.draw(canvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
        return false;
    }

    public Doodle(Context context) {
        super(context);
        init();
    }

    public Doodle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public Doodle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mSize);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.parseColor("#ffcccccc"));
        for (Action a : mActions) {
            a.draw(canvas);
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);

        Log.d("look", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("look", "surface changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("look", "surface destroyed");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        float touchX = event.getRawX();
        float touchY = event.getRawY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setMyAction(touchX, touchY);
                if(mDrawListener != null){
                    mDrawListener.onDown(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Canvas canvas = mSurfaceHolder.lockCanvas();
                canvas.drawColor(Color.parseColor("#ffcccccc"));
                for (Action a : mActions) {
                    a.draw(canvas);
                }
                mAction.move(touchX, touchY);
                mAction.draw(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
                if(mDrawListener != null){
                    mDrawListener.onMove(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                mActions.add(mAction);
                mAction = null;
                if(mDrawListener != null){
                    mDrawListener.onUp(event);
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    // 得到当前画笔的类型，并进行实例
    public void setMyAction(float x, float y) {
        switch (mType) {
            case Point:
                mAction = new MyPoint(x, y, mColor);
                break;
            case Path:
                mAction = new MyPath(x, y, mSize, mColor);
                break;
            case Line:
                mAction = new MyLine(x, y, mSize, mColor);
                break;
            case Rect:
                mAction = new MyRect(x, y, mSize, mColor);
                break;
            case Circle:
                mAction = new MyCircle(x, y, mSize, mColor);
                break;
            case FillecRect:
                mAction = new MyFillRect(x, y, mSize, mColor);
                break;
            case FilledCircle:
                mAction = new MyFillCircle(x, y, mSize, mColor);
                break;
        }
    }
    public void setPeerAction(float x, float y) {
        switch (peerType) {
            case Point:
                peerAction = new MyPoint(x, y, peerColor);
                break;
            case Path:
                peerAction = new MyPath(x, y, peerSize, peerColor);
                break;
            case Line:
                peerAction = new MyLine(x, y, peerSize, peerColor);
                break;
            case Rect:
                peerAction = new MyRect(x, y, peerSize, peerColor);
                break;
            case Circle:
                peerAction = new MyCircle(x, y, peerSize, peerColor);
                break;
            case FillecRect:
                peerAction = new MyFillRect(x, y, peerSize, peerColor);
                break;
            case FilledCircle:
                peerAction = new MyFillCircle(x, y, peerSize, peerColor);
                break;
        }
    }


    public enum ActionType {
        Point, Path, Line, Rect, Circle, FillecRect, FilledCircle, Eraser, Pic
    }

    public void drawPic(Bitmap bitmap, float width, float height){
        ActionType tempActionType = getMyType();
        //设置当前的类型
        setMyType(ActionType.Pic);
        mAction = new MyPic(bitmap, mPaint, width, height);
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.parseColor("#ffcccccc"));
        for (Action a : mActions) {
            a.draw(canvas);
        }
        mAction.draw(canvas);
        mActions.add(mAction);
        mAction = null;
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        //设置回默认的类型
        setMyType(tempActionType);
    }

    public void drawPic(Bitmap bitmap){
        ActionType tempActionType = getMyType();
        //设置当前的类型
        setMyType(ActionType.Pic);
        mAction = new MyPic(bitmap, mPaint);
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.parseColor("#ffcccccc"));
        for (Action a : mActions) {
            a.draw(canvas);
        }
        mAction.draw(canvas);
        mActions.add(mAction);
        mAction = null;
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        //设置回默认的类型
        setMyType(tempActionType);
    }
    /**
     * 给自己画画设置listener
     */
    public interface OnDrawListener{
        void onDown(MotionEvent event);
        void onMove(MotionEvent event);
        void onUp(MotionEvent event);
    }
    private OnDrawListener mDrawListener;
    public void setOnDrawListener(OnDrawListener onDrawListener){
        mDrawListener = onDrawListener;
    }

    /**
     * 下面的写法，没有给对方画画设置listener （down up move）
     */
    public void downPeer(ActionType peerType, int peerSize, int peerColor, float peerX, float peerY){
        Log.d(TAG,"in downPeer");
        setPeerType(peerType);
        setPeerSize(peerSize);
        setPeerColor(peerColor);
        setPeerAction(peerX, peerY);
        Log.d(TAG,"out downPeer");
    }

    public void upPeer(){
        Log.d(TAG,"in upPeer");
        mActions.add(peerAction);
        peerAction = null;
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.parseColor("#ffcccccc"));
        for (Action a : mActions) {
            a.draw(canvas);
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
        Log.d(TAG,"out upPeer");
    }
    public void movePeer(float x, float y){
        Canvas canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.parseColor("#ffcccccc"));
        for (Action a : mActions) {
            a.draw(canvas);
        }
        if(peerAction != null){
            peerAction.move(x, y);
            peerAction.draw(canvas);
        }
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void justMovePeer(float x, float y){
        Log.d(TAG,"in justMovePeer");
        if(peerAction != null){
            peerAction.move(x, y);
        }
        Log.d(TAG,"out justMovePeer");
    }

    public void recycleBitmap(){
        for (Action action : mActions) {
            if (action instanceof MyPic) {
                ((MyPic)action).recycleAskBitmap();
            }
        }
    }
}

