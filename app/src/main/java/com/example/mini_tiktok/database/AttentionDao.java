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
    @Query("SELECT * FROM attention")
    List<AttentionEntity> loadAll();

    @Insert
    long addAttention(AttentionEntity entity);

    @Query("DELETE FROM attention WHERE id1 = :id1 and id2 = :id2")
    void deleteAttention(String id1,String id2);

    @Query("SELECT id2 FROM attention WHERE id1 = :ID")
    List<String> findById1(String ID);

}
