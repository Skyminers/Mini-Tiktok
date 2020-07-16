package com.example.mini_tiktok.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Database(entities = {AttentionEntity.class}, version = 2, exportSchema = false)
public abstract class AttentionDatabase extends RoomDatabase {
    private static volatile AttentionDatabase INSTANCE;

    public abstract AttentionDao AttentionListDao();

    public AttentionDatabase() {

    }

    public static AttentionDatabase inst(Context context) {
        if (INSTANCE == null) {
            synchronized (AttentionDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AttentionDatabase.class, "attention.db").build();
                }
            }
        }
        return INSTANCE;
    }
}
