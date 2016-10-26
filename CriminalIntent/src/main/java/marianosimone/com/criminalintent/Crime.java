package marianosimone.com.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private final UUID mId;

    private String mTitle;

    private Date mDate;

    private boolean mSolved;

    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public void setTitle(final String title) {
        mTitle = title;
    }

    public UUID getId() {

        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }
}
