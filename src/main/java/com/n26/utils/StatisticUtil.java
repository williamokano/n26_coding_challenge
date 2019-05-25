package com.n26.utils;

import com.n26.model.Statistic;
import com.n26.model.Transaction;

public abstract class StatisticUtil {
    public static void updateStatisticMinIfTransactionIsLesser(Statistic statistic, Transaction transaction) {
        if (transaction.getAmount().compareTo(statistic.getMin()) < 0) {
            statistic.setMin(transaction.getAmount());
        }
    }

    public static void updateStatisticMaxIfTransactionIsGreater(Statistic statistic, Transaction transaction) {
        if (transaction.getAmount().compareTo(statistic.getMax()) > 0) {
            statistic.setMax(transaction.getAmount());
        }
    }
}
