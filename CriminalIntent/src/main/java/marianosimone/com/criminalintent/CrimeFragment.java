package marianosimone.com.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import java.text.DateFormat;
import java.util.UUID;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import static android.app.Activity.RESULT_OK;
import static marianosimone.com.criminalintent.CrimePagerActivity.EXTRA_CRIME_POSITION;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String ARG_CRIME_POSITION = "crime_position";

    private Crime mCrime;

    private DateFormat mDateFormat;

    public static CrimeFragment newInstance(final UUID crimeId, final int crimePosition) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putInt(ARG_CRIME_POSITION, crimePosition);
        final CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mDateFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        returnResult();
    }

    public void returnResult() {
        final Intent data = new Intent();
        data.putExtra(EXTRA_CRIME_POSITION, getArguments().getInt(ARG_CRIME_POSITION));
        getActivity().setResult(RESULT_OK, data);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
        final EditText titleField = (EditText) view.findViewById(R.id.crime_title);
        titleField.setText(mCrime.getTitle());
        titleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                mCrime.setTitle(text.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final Button dateButton = (Button) view.findViewById(R.id.crime_date);

        dateButton.setText(mDateFormat.format(mCrime.getDate()));
        dateButton.setEnabled(false);

        final CheckBox solvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        solvedCheckBox.setChecked(mCrime.isSolved());
        solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        return view;
    }
}
