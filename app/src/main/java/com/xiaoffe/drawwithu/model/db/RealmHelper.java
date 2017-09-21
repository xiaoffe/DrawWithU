package com.xiaoffe.drawwithu.model.db;

import javax.inject.Inject;

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

    /**
     * 增加 阅读记录
     * @param id
     * 使用@PrimaryKey注解后copyToRealm需要替换为copyToRealmOrUpdate
     */
    @Override
    public void insertNewsId(int id) {
//        ReadStateBean bean = new ReadStateBean();
//        bean.setId(id);
//        mRealm.beginTransaction();
//        mRealm.copyToRealmOrUpdate(bean);
//        mRealm.commitTransaction();
    }

    /**
     * 查询 阅读记录
     * @param id
     * @return
     */
    @Override
    public boolean queryNewsId(int id) {
//        RealmResults<ReadStateBean> results = mRealm.where(ReadStateBean.class).findAll();
//        for(ReadStateBean item : results) {
//            if(item.getId() == id) {
//                return true;
//            }
//        }
        return false;
    }

}
