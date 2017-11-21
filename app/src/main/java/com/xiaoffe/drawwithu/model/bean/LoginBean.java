package com.xiaoffe.drawwithu.model.bean;

import java.util.List;

public class LoginBean {

    public LoginBean() { }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //尽管请求返回的LoginBean里面只有token字段，现在在这个class里加friends，依然会兼容。不会出错。
    //请求好友列表什么的，就放到
    private List<FriendBean> friends;

    public List<FriendBean> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendBean> friends) {
        this.friends = friends;
    }
}