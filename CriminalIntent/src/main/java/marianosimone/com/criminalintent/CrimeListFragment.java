package marianosimone.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import static marianosimone.com.criminalintent.CrimePagerActivity.EXTRA_CRIME_POSITION;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CODE_CRIME = 0;
    private static final java.lang.String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private DateFormat mDateFormat;
    private RecyclerView mCrimeListRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubtitleVisible;
    private final CrimeLab mCrimeLab = CrimeLab.get();

    @Override
    public void onCreate(final @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final @Nullable Bundle savedInstanceState
    ) {
        mDateFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        final View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeListRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }

    private void updateUI() {
        final List<Crime> crimes = mCrimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeListRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onSaveInstanceState(final @NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CRIME) {
            if (data == null) {
                return;
            }
            mAdapter.notifyItemChanged(data.getIntExtra(EXTRA_CRIME_POSITION, 0) - 1);
        }
    }

    @Override
    public void onCreateOptionsMenu(final @NonNull Menu menu, final @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        final MenuItem subtitleMenuItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleMenuItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleMenuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final @NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                final Crime crime = new Crime();
                final int position = mCrimeLab.addCrime(crime);
                final Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId(), position);
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        final int crimeCount = mCrimeLab.getCrimes().size();
        final String subtitle = mSubtitleVisible
                ? getResources().getQuantityString(R.plurals.subtitle_format, crimeCount, crimeCount)
                : null;

        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private int mPosition;

        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final CheckBox mSolvedCheckBox;


        CrimeHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        void bindCrime(final int position, final Crime crime) {
            mCrime = crime;
            mPosition = position;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mDateFormat.format(mCrime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
                    mCrime.setSolved(b);
                }
            });
        }

        @Override
        public void onClick(final View view) {
            startActivityForResult(
                    CrimePagerActivity.newIntent(getActivity(), mCrime.getId(), mPosition),
                    REQUEST_CODE_CRIME
            );
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private final List<Crime> mCrimes;

        CrimeAdapter(final List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(final CrimeHolder holder, final int position) {
            final Crime crime = mCrimes.get(position);
            holder.bindCrime(position, crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}
