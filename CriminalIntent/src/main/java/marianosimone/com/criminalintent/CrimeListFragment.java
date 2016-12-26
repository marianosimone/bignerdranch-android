package marianosimone.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import static marianosimone.com.criminalintent.CrimePagerActivity.EXTRA_CRIME_POSITION;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CODE_CRIME = 0;

    private DateFormat mDateFormat;
    private RecyclerView mCrimeListRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        mDateFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        final View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeListRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        final CrimeLab crimeLab = CrimeLab.get(getActivity());
        final List<Crime> crimes = crimeLab.getCrimes();
            mAdapter = new CrimeAdapter(crimes);
            mCrimeListRecyclerView.setAdapter(mAdapter);
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
            mAdapter.notifyItemChanged(data.getIntExtra(EXTRA_CRIME_POSITION, 0));
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Crime mCrime;
        private int mPosition;

        private final TextView mTitleTextView;
        private final TextView mDateTextView;
        private final CheckBox mSolvedCheckBox;


        public CrimeHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        public void bindCrime(final int position, final Crime crime) {
            mCrime = crime;
            mPosition = position;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mDateFormat.format(mCrime.getDate()));
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(final View view) {
            startActivityForResult(
                    CrimePagerActivity.newIntent(getActivity(), mCrime.getId()),
                    REQUEST_CODE_CRIME
            );
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private final List<Crime> mCrimes;

        public CrimeAdapter(final List<Crime> crimes) {
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
