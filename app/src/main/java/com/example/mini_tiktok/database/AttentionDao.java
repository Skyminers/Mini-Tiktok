package com.example.mini_tiktok.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface AttentionDao {
    @Query("SELECT * FROM Attention")
    List<AttentionEntity> loadAll();

    @Insert
    long addAttention(AttentionEntity entity);


    @Query("SELECT id2 FROM Attention WHERE id1 = :ID")
    List<String> findById1(String ID);

}
