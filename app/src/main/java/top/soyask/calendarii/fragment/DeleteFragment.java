package top.soyask.calendarii.fragment;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dinuscxj.progressbar.CircleProgressBar;

import top.soyask.calendarii.R;
import top.soyask.calendarii.fragment.base.BaseFragment;


public class DeleteFragment extends BaseFragment implements View.OnTouchListener, Animator.AnimatorListener, RadioGroup.OnCheckedChangeListener {

    private static final String EVENT = "event";
    public static final int ALL = 0;
    public static final int COMP = 1;

    private CircleProgressBar mCircleProgressBar;
    private int mType = COMP;
    private OnDeleteConfirmListener mOnDeleteConfirmListener;
    private boolean isExiting;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what < 150) {
                mCircleProgressBar.setProgress(msg.what);
            } else {
                exit(getContentView(), -1, -1);
            }
        }
    };

    private Thread mProgress = new Thread() {
        @Override
        public void run() {
            int i = 0;
            while (i < 100) {
                if (isInterrupted()){
                    mHandler.sendEmptyMessage(0);
                    return;
                }
                i++;
                mHandler.sendEmptyMessage(i);
                SystemClock.sleep(20);
            }
            isExiting = true;
            mHandler.sendEmptyMessageDelayed(150, 200);
            if (mOnDeleteConfirmListener != null) {
                mOnDeleteConfirmListener.onConfirm(mType);
            }
        }
    };

    public DeleteFragment() {
        super(R.layout.fragment_delete);
    }

    public static DeleteFragment newInstance() {
        DeleteFragment fragment = new DeleteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enter(view);
    }

    public void setOnDeleteConfirmListener(OnDeleteConfirmListener onDeleteConfirmListener) {
        this.mOnDeleteConfirmListener = onDeleteConfirmListener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void enter(View view) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, width / 2, height / 2, 0, height);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void exit(View view, int x, int y) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(view, x == -1 ? width / 2 : x, y == -1 ? height / 2 : y, height, 0);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(this);
        anim.start();
    }

    @Override
    protected void setupUI() {
        getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isExiting) {
                    isExiting = true;
                    exit(v, (int)event.getX(), (int)event.getY());
                }
                return true;
            }
        });

        mCircleProgressBar = findViewById(R.id.cpb);
        mCircleProgressBar.setOnTouchListener(this);
        RadioGroup rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);
        RadioButton rb = findViewById(R.id.rb_comp);
        rb.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_all:
                mType = ALL;
                break;
            case R.id.rb_comp:
                mType = COMP;
                break;
        }
    }


    public interface OnDeleteConfirmListener {
        void onConfirm(int type);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mProgress.start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mProgress.interrupt();
                break;
        }
        return true;
    }


    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        getContentView().setVisibility(View.GONE);
        removeFragment(this);
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
