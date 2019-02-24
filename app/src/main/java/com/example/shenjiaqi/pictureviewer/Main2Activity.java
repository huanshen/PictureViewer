package com.example.shenjiaqi.pictureviewer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";
    /** 动画时间 */
    public static final int DURATION_IN = 500;
    /** 图片位置参数 */
    private ArrayList<Integer> mPosArr;
    /** 图片的索引，此处用于设置图片 */
    private int mIndex;
    /** ImageView */
    private ImageView mImageView;
    /** Resources */
    private Resources mResources;
    /** 屏幕显示信息 */
    private DisplayMetrics mMetrics;
    /** 存储图片缩放比例 */
    private float[] mScale = new float[2];
    /** 存储图片位移距离 */
    private float[] mTransition = new float[2];
    float mRadio;
    /** 图片初始宽度 */
    private int mOriginWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        init();
        getIntent(getIntent());
        computerAnimationParams();
        doReadyJob();
        showEnterAnim();
    }

    /**
     * 初始化
     */
    public void init() {
        mImageView = findViewById(R.id.image);
        mResources = mImageView.getContext().getResources();
        mMetrics = mResources.getDisplayMetrics();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOutAnim();
            }
        });
    }

    /**
     * 动画前的准备工作
     **/
    private void doReadyJob() {
        // 需要对图片的宽度进行调整
        FrameLayout.LayoutParams params
                = new FrameLayout.LayoutParams(mOriginWidth, FrameLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(mPosArr.get(0), 0, mPosArr.get(1), 0);
        mImageView.setLayoutParams(params);
    }

    /**
     * 获取intent中的数据
     *
     * @param intent intent
     **/
    public void getIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        mPosArr = intent.getIntegerArrayListExtra(MainActivity.POS_ARRAY);
        mIndex = intent.getIntExtra(MainActivity.IMAGE_POS, 0);
        if (mIndex % 2 == 0) {
            mImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.timg1));
        } else {
            mImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.timg));
        }
    }

    /**
     * 模拟入场动画
     */
    private void showEnterAnim() {
        final ValueAnimator enterAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION_IN);
        enterAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        enterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mImageView.setAlpha(1f);
                mImageView.setScaleX(1 + (mRadio - 1) * value);
                mImageView.setScaleY(1 + (mRadio - 1) * value);
                mImageView.setTranslationX(mTransition[0] * value);
                mImageView.setTranslationY(-mTransition[1] * (1f - value));
                mImageView.invalidate();
            }
        });
        enterAnimator.start();
    }

    /**
     * 计算动画位移和缩放
     */
    private void computerAnimationParams() {
        if (mPosArr.size() != 4) {
            return;
        }
        int screenHigh = mMetrics.heightPixels;
        int screenWidth = mMetrics.widthPixels;
        // 计算可用于显示图片的屏幕高度
        int availableScreenHeight = screenHigh - getStatusBarHeight();

        int imageHeight = mPosArr.get(3) - mPosArr.get(2);
        mOriginWidth = mPosArr.get(1) - mPosArr.get(0);

        float radioWidth = screenWidth * 1.0f / mOriginWidth;
        float radioHeight = availableScreenHeight * 1.0f / imageHeight;
        // 获取放大系数
        mRadio = Math.min(radioHeight, radioWidth);

        // 计算位移距离，屏幕中心与图片中心的距离，
        mTransition[0] = (float) (screenWidth / 2 - (mPosArr.get(0)
                + (mPosArr.get(1) - mPosArr.get(0)) / 2));
        mTransition[1] = (float) ((availableScreenHeight) / 2
                - (mPosArr.get(2) - getStatusBarHeight() + (mPosArr.get(3) - mPosArr.get(2)) / 2));
    }

    /**
     * 获取状态栏得高度
     */
    public int getStatusBarHeight() {
        int result = 0;
        Resources resources = mImageView.getContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            try {
                result = resources.getDimensionPixelSize(resourceId);
            } catch (Exception var3) {
                result = 0;
            }
        }

        if (result == 0) {
            result = (int)(25.0F * mMetrics.density);
        }
        Log.d(TAG, "getStatusBarHeight: " + result);
        return result;
    }


    /**
     * 模拟退场动画
     */
    private void showOutAnim() {
        final ValueAnimator exitAnimator = ValueAnimator.ofFloat(1f, 0f).setDuration(DURATION_IN);
        mImageView.setAlpha(1f);
        exitAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        exitAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mImageView.setScaleX((mRadio - 1) * value + 1);
                mImageView.setScaleY((mRadio - 1) * value + 1);
                mImageView.setTranslationX(mTransition[0] * value);
                mImageView.setTranslationY(-mTransition[1] * (1f - value));
            }
        });
        exitAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                quitActivityWithoutAnimation();
            }
        });
        exitAnimator.start();

    }

    /**
     * activity无动画退出
     */
    public void quitActivityWithoutAnimation() {
        finish();
        overridePendingTransition(0, 0);
    }

}
