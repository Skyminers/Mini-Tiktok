package com.example.mini_tiktok.utils;

import android.content.Context;

import com.example.mini_tiktok.database.AccountDao;
import com.example.mini_tiktok.database.AccountDatabase;
import com.example.mini_tiktok.database.AccountEntity;

public class UserAccountUtils {

    public static String userID = "Guest";
    public static String userNick = "Guest";

    public static AccountEntity findAccountById(Context context,String ID){
        AccountDao accountDao = AccountDatabase.inst(context).AccountListDao();
        return accountDao.findById(ID);
    }

    public static void insertAccount(Context context,AccountEntity entity){
        AccountDao accountDao = AccountDatabase.inst(context).AccountListDao();
        accountDao.addAccount(entity);
    }

    public static boolean checkAccount(Context context,AccountEntity entity){
        AccountEntity savedAccount = UserAccountUtils.findAccountById(context,entity.getMId());
        if(savedAccount == null) return false;
        if(!entity.getPwd().equals(savedAccount.getPwd())) return false;
        return true;
    }

    public static boolean registerAccount(Context context,AccountEntity entity){
        if(findAccountById(context,entity.getMId()) != null) return false;
        insertAccount(context,entity);
        return true;
    }

    public static void modifyNick(Context context, AccountEntity entity){
        AccountDao accountDao = AccountDatabase.inst(context).AccountListDao();
        accountDao.modify(entity);
    }

}
