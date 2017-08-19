package top.soyask.calendarii.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Calendar;

import top.soyask.calendarii.fragment.MonthFragment;
import top.soyask.calendarii.global.Global;

/**
 * Created by mxf on 2017/8/8.
 */
public class MonthFragmentAdapter extends FragmentPagerAdapter {

    public static final int YEAR_END = Global.YEAR_END;
    public static final int YEAR_START = Global.YEAR_START;
    //1970-2200
    private Calendar mCalendar;
    private MonthFragment.OnDaySelectListener mOnDaySelectListener;

    public MonthFragmentAdapter(FragmentManager fm, Calendar calendar, MonthFragment.OnDaySelectListener onDaySelectListener) {
        super(fm);
        this.mCalendar = calendar;
        this.mOnDaySelectListener = onDaySelectListener;
    }

    @Override
    public Fragment getItem(int position) {
        MonthFragment monthFragment = MonthFragment.newInstance(mCalendar, position);
        monthFragment.setOnDaySelectListener(mOnDaySelectListener);
        return monthFragment;
    }

    @Override
    public int getCount() {
        return (YEAR_END - YEAR_START) * Global.MONTH_COUNT;
    }
}
