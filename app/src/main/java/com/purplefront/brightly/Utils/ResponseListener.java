package com.purplefront.brightly.Utils;

/**
 * Created by ponna on 16-01-2018.
 */

public interface ResponseListener {

    void onData(MetaData metaData);

    void onError(Exception e);
}
