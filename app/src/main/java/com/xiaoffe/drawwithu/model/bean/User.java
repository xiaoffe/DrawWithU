package com.xiaoffe.drawwithu.model.bean;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/9.
 */
@Table(name="Users" )
public class User extends Model  implements Serializable {
//    @Expose
//    @SerializedName("_id")
//    @Column(name="userId" , unique = true , onUniqueConflict = Column.ConflictAction.REPLACE )
//    public String id;

    @Expose
    @Column(name="username",unique = true , onUniqueConflict = Column.ConflictAction.REPLACE )
    public String username;

    @Column(name = "nickname")
    public String nickname;

    @Column(name = "faceurl")
    public String faceurl;

    @Column(name="token")
    public String token;

    public User(){
        super();
    }
}
