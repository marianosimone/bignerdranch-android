package marianosimone.com.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends FragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.marianosimone.criminalintent.crime_id";
    public static final String EXTRA_CRIME_POSITION = "com.marianosimone.criminalintent.crime_position";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;

    public static Intent newIntent(final Context packageContext, UUID crimeId) {
        final Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(final Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_crime_pager);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        final FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(final int position) {
                final Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId(), position);
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
    }
}
