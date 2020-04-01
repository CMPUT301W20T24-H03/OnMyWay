package com.CMPUT301W20T24.OnMyWay;

import android.os.Parcel;
import android.os.Parcelable;

public class DriverHistoryInfo implements Parcelable {
    private String history_id;
    private String history_start;
    private String history_dest;

    DriverHistoryInfo(String history_id, String history_start, String history_dest){
        this.history_id = history_id;
        this.history_start = history_start;
        this.history_dest = history_dest;
    }

    protected DriverHistoryInfo(Parcel in) {
        history_id = in.readString();
        history_start = in.readString();
        history_dest = in.readString();
    }

    public static final Creator<DriverHistoryInfo> CREATOR = new Creator<DriverHistoryInfo>() {
        @Override
        public DriverHistoryInfo createFromParcel(Parcel in) {
            return new DriverHistoryInfo(in);
        }

        @Override
        public DriverHistoryInfo[] newArray(int size) {
            return new DriverHistoryInfo[size];
        }
    };

    public String getHistory_id() {
        return history_id;
    }

    public void setHistory_id(String history_id) {
        this.history_id = history_id;
    }

    public String getHistory_start() {
        return history_start;
    }

    public void setHistory_start(String history_start) {
        this.history_start = history_start;
    }

    public String getHistory_dest() {
        return history_dest;
    }

    public void setHistory_dest(String history_dest) {
        this.history_dest = history_dest;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.history_id);
        dest.writeString(this.history_dest);
        dest.writeString(this.history_start);
    }
}
