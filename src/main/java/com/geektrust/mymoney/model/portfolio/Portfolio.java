package com.geektrust.mymoney.model.portfolio;

import com.geektrust.mymoney.model.portfolio.statement.SIP;

public interface Portfolio extends MonthlyEventSubscriber {

    void setSip(SIP sip);

    String getId();

}
