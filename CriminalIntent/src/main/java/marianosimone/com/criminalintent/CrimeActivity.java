package marianosimone.com.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

public class CrimeActivity extends SingleFragmentActivity {

    public static final String EXTRA_CRIME_ID = "com.marianosimone.criminalintent.crime_id";
    public static final String EXTRA_CRIME_POSITION = "com.marianosimone.criminalintent.crime_position";

    @Override
    protected Fragment createFragment() {
        final UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        final int crimePosition = getIntent().getIntExtra(EXTRA_CRIME_POSITION, 0);
        return CrimeFragment.newInstance(crimeId, crimePosition);
    }

    public static Intent newIntent(final Context context, final UUID crimeId, final int crimePosition) {
        final Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        intent.putExtra(EXTRA_CRIME_POSITION, crimePosition);
        return intent;
    }
}
