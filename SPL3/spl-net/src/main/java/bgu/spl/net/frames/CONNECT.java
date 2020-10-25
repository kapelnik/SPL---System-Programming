package bgu.spl.net.frames;

import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.User;

import java.util.HashMap;

public class CONNECT extends Frame {
    private User currUser;
    public CONNECT(User currUser,String[] headers, String body) {
        super(headers, body);
        this.currUser=currUser;
    }

    @Override
    public void process() {
        DataBase dataBase = DataBase.getInstance();
        boolean isNewUser = true;
        for (User u:dataBase.getUsers())
        {
            if(u.getName()==headers[2]) // login
            {
                if (u.getPassword()==headers[3]) // password
                {
                    if (!u.isActive())
                    {
                        u.setActive(true);
                    }
                    else //already inside, ignore ?
                    {
                        new ERRORfrm(u.getConnectionId(),new String[]{"","User already logged in"},"").process();//wrong password
                    }
                }
                else if (u.getPassword()!=null)
                {
                    new ERRORfrm(u.getConnectionId(),new String[]{"","Wrong password"},"").process();//wrong password
                }
                isNewUser=false;
            }
        }
        if (isNewUser) // new User name and password
        {
            DataBase.getInstance().getUsers().add(new User(headers[2],headers[3],currUser.getConnectionId(),currUser.getHandler(),null));
        }

    }
}
