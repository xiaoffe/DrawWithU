package com.xiaoffe.drawwithu.model.db;

/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @description:
 */

public interface DBHelper {

    void insertNewsId(int id);

    /**
     * 查询 阅读记录
     * @param id
     * @return
     */
    boolean queryNewsId(int id);

}
