package com.example.mini_tiktok.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
@Dao
public interface AccountDao {
    @Query("SELECT * FROM todo")
    List<AccountEntity> loadAll();

    @Insert
    long addAccount(AccountEntity entity);


    @Query("SELECT * FROM todo WHERE id = :ID")
    AccountEntity findById(String ID);

}
