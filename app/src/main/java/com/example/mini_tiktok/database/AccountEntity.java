package com.example.mini_tiktok.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "todo")
public class AccountEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private String mId;

    @ColumnInfo(name = "pwd")
    private String pwd;

    public AccountEntity(String mId, String pwd) {
        this.mId = mId;
        this.pwd = pwd;
    }

    public String getMId() {
        return mId;
    }

    public void setMId(String ID) {
        this.mId = ID;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String mTime) {
        this.pwd = pwd;
    }

}
