package com.xiaoffe.drawwithu.model.db;

import com.xiaoffe.drawwithu.model.bean.LoginBean;
import javax.inject.Inject;
//import io.realm.Realm;
//import io.realm.RealmConfiguration;
//import io.realm.RealmResults;

/**
 * Created by codeest on 16/8/16.
 */

public class RealmHelper implements DBHelper {

    private static final String DB_NAME = "myRealm.realm";

//    private Realm mRealm;

    @Inject
    public RealmHelper() {
//        mRealm = Realm.getInstance(new RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
//                .name(DB_NAME)
//                .build());
    }

    @Override
    public void insertToken(String s) {
//        LoginBean bean = new LoginBean();
//        bean.setId(0); // 每次让主键只为0，看会不会只保存一个数据
//        bean.setToken(s);
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(bean);
//        mRealm.commitTransaction();
    }

    @Override
    public String getToken() {
//        RealmResults<LoginBean> results = mRealm.where(LoginBean.class).findAll();
//        for(LoginBean item : results) {
//            if(item.getId() == 0) {
//                return item.getToken();
//            }
//        }
        return "dummyToken";

    }

}
