package com.example.mini_tiktok.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Database(entities = {AccountEntity.class}, version = 2, exportSchema = false)
public abstract class AccountDatabase extends RoomDatabase {
    private static volatile AccountDatabase INSTANCE;

    public abstract AccountDao AccountListDao();

    public AccountDatabase() {

    }

    public static AccountDatabase inst(Context context) {
        if (INSTANCE == null) {
            synchronized (AccountDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AccountDatabase.class, "todo.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
