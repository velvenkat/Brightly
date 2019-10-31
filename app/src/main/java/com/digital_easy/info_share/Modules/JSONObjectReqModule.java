package com.digital_easy.info_share.Modules;

import java.util.ArrayList;
import java.util.List;

public class JSONObjectReqModule<T> {
    public List<T> jsonReqList = new ArrayList<T>();

    public void populate(int Position, T t) {
        if (jsonReqList.size() == 0)
            jsonReqList.add(t);
        else {
            if (Position == 1) {
                if (!(t instanceof ContactHelperModule))
                    jsonReqList.set(Position - 1, t);
                else
                    jsonReqList.add(Position - 1, t);


            } else if (Position - 1 <= jsonReqList.size()) {
                if (!(t instanceof ContactHelperModule))

                    jsonReqList.set(Position - 2, t);
                else
                    jsonReqList.add(Position - 2, t);
            } else {
                jsonReqList.add(t);
            }
        }
    }

  /*  public void contactAdd(int Position, T t) {
       // jsonReqList.add(Position, t);
        if (jsonReqList.size() == 0)
            jsonReqList.add(t);
        else {
            if (Position == 1) {
                jsonReqList.set(Position - 1, t);

            } else if (Position - 1 <= jsonReqList.size()) {
                jsonReqList.set(Position - 2, t);
            } else {
                jsonReqList.add(t);
            }
        }
    }*/

    public T get(int Position) {
        if (Position == 1) {
            return jsonReqList.get(0);
        } else {
            return jsonReqList.get(Position - 2);
        }
    }
}
