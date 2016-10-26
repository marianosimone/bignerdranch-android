package marianosimone.com.criminalintent;

import java.util.UUID;

public class Crime {

    private final UUID mId;

    private String mTitle;

    public Crime() {
        mId = UUID.randomUUID();
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
}
