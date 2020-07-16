package com.example.mini_tiktok.utils;

import android.content.Context;

import com.example.mini_tiktok.database.AttentionDao;
import com.example.mini_tiktok.database.AttentionDatabase;
import com.example.mini_tiktok.database.AttentionEntity;

import java.util.List;

public class AttentionUtils {
    public static List<String> getAttentionList(Context context,String id){
        AttentionDao attentionDao = AttentionDatabase.inst(context).AttentionListDao();
        List<String> list = attentionDao.findById1(id);
        return list;
    }

    public static void insertAttention(Context context, String id1, String id2){
        AttentionDao attentionDao = AttentionDatabase.inst(context).AttentionListDao();
        AttentionEntity attentionEntity = new AttentionEntity(id1,id2);
        attentionDao.addAttention(attentionEntity);
    }
}
