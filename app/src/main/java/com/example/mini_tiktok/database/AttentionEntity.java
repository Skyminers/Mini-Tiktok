package com.example.mini_tiktok.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attention")
public class AttentionEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id1")
    private String id1;

    @ColumnInfo(name = "id2")
    private String id2;

    public AttentionEntity(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public int getId() {
        return id;
    }

    public void setId(int ID) {
        this.id = ID;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2(){
        return id2;
    }

    public void setId2(String id2){
        this.id2 = id2;
    }

}
