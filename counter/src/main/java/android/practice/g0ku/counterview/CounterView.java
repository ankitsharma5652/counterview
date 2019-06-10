package android.practice.g0ku.counterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public final class CounterView extends LinearLayout implements Counter, View.OnClickListener {


    private final String TAG = getClass().getSimpleName();
    private final int DEFAULT = -1;
    private final int MIN = 0;
    private OnCountChangeListener mCountListener;
    private View mRoot;
    private int count = DEFAULT, min = MIN, max = DEFAULT;
    private TextView mMinus, mText, mPlus;
    private View.OnClickListener mButtonClickListener, mLeftClickListener, mRightClickListener;
    private View mLoader;
    private String text;
    private Drawable mBackgroundDrawable;
    private boolean isLoading;


    public CounterView(Context context) {
        this(context, null);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CounterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public CounterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


        TypedArray value = context.obtainStyledAttributes(attrs, R.styleable.CounterView);


        isLoading = value.getBoolean(R.styleable.CounterView_cv_loading, false);
        min = value.getInt(R.styleable.CounterView_cv_min, 0);
        max = value.getInt(R.styleable.CounterView_cv_max, 100);
        text = value.getString(R.styleable.CounterView_cv_info);
        mBackgroundDrawable = value.getDrawable(R.styleable.CounterView_cv_background);

        value.recycle();
        setOrientation(LinearLayout.VERTICAL);
        mRoot = LayoutInflater.from(context).inflate(R.layout.layout_count_btn, this, true);

        mMinus = mRoot.findViewById(R.id.left);
        mText = mRoot.findViewById(R.id.count);
        mPlus = mRoot.findViewById(R.id.right);

        mLoader = mRoot.findViewById(R.id.loader);


        mMinus.setOnClickListener(this);
        mPlus.setOnClickListener(this);

        setCount(null);
        setMin(min);
        setMax(max);
        setText(text);


        if (text != null)
            mMinus.setVisibility(View.INVISIBLE);

        if (isLoading)
            showLoader();

        if (mBackgroundDrawable != null)
            mRoot.setBackground(mBackgroundDrawable);


    }


    @Override
    public int count() {
        return count;
    }

    @Override
    public int max() {
        return max;
    }

    @Override
    public int min() {
        return min;
    }

    @Override
    public void setMin(Integer min) {
        if (min == null) {
            min = -1;
        }

        this.min = min;
    }

    @Override
    public void setMax(Integer max) {
        if (max == null) {
            max = -1;
        }

        this.max = max;
    }

    @Override
    public void setCount(final Integer count) {


        if (count == null) {
            mText.setText(text);
            mMinus.setVisibility(View.INVISIBLE);
            this.count = 0;
            return;
        } else if (mMinus.getVisibility() != View.VISIBLE)
            mMinus.setVisibility(View.VISIBLE);

        if (this.count == count) return;

        int oldCount = -1;

        if (count >= min && count <= max) {
            oldCount = this.count;
            this.count = count;
        } else {

            if (count < min) {
                mMinus.setVisibility(View.INVISIBLE);
                mText.setText(text);
            }

            return;
        }


        mText.setText(String.valueOf(this.count));

        if (mCountListener != null)
            mCountListener.onCountChange(oldCount, count);

    }


    @Override
    public void onClick(View v) {

        if (v.getVisibility() != View.VISIBLE) return;


        if (mButtonClickListener != null) {
            mButtonClickListener.onClick(mRoot);
            return;
        }


        int id = v.getId();
        if (R.id.left == id) {

            int count = count() - 1;

            if (mLeftClickListener != null)
                mLeftClickListener.onClick(v);
            else
                setCount(count < min() ? null : count);
        }


        if (id == R.id.right) {

            if (mRightClickListener != null)
                mRightClickListener.onClick(v);
            else
                setCount(count() + 1);

        }


    }

    public void setButtonClickListener(OnClickListener l) {
        this.mButtonClickListener = l;

        mRoot.setOnClickListener(l);

    }

    public void setLeftClickListener(OnClickListener l) {
        this.mLeftClickListener = l;
    }

    public void setRightClickListener(OnClickListener l) {
        this.mRightClickListener = l;
    }

    public void showLoader() {

        if (isLoading) return;

        isLoading = true;

        mText.setVisibility(View.INVISIBLE);
        mMinus.setVisibility(View.INVISIBLE);
        mPlus.setVisibility(View.INVISIBLE);

        mLoader.setVisibility(View.VISIBLE);
    }


    public boolean isLoading() {
        return isLoading;
    }

    public void hideLoader() {
        if (!isLoading) return;

        isLoading = false;


        mText.setVisibility(View.VISIBLE);
        mMinus.setVisibility(View.VISIBLE);
        mPlus.setVisibility(View.VISIBLE);
        mLoader.setVisibility(View.GONE);
    }


    public void setText(String text) {
        this.text = text;

        setCount(count);

    }


    public View getCounterView() {
        return mRoot;
    }


    interface OnCountChangeListener {

        void onCountChange(int oldValue, int newValue);
    }
}


