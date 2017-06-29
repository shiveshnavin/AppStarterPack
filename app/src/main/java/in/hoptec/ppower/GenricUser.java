package in.hoptec.ppower;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by shivesh on 29/6/17.
 */

public class GenricUser {

    public String user_name;
    public String user_fname;
    public String user_password;
    public String uid;
    public String suid;
    public String auth;
    public String user_created;
    public String user_email;
    public String user_image;
    public String user_phone;
    public String social;

    public String extra0;
    public String extra1;
    public String extra2;

    public GenricUser(FirebaseUser user)
    {
        user_name=utl.refineString(user.getEmail(),"");
        user_fname=user.getDisplayName();
        user_password=user.getUid();
        uid=user.getUid();
        suid=user.getUid();
        auth=user.getUid();
        user_email=user.getEmail();
        user_phone=user.getPhoneNumber();
        user_image=user.getPhotoUrl().toString();

    }

    public GenricUser()
    {

    }


}
