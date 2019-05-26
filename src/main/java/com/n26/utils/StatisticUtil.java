package com.n26.utils;

import com.n26.model.Statistics;
import com.n26.model.Transaction;

public abstract class StatisticUtil {
    public static void updateStatisticMinIfTransactionIsLesser(Statistics statistic, Transaction transaction) {
        if (transaction.getAmount().compareTo(statistic.getMin()) < 0) {
            statistic.setMin(transaction.getAmount());
        }
    }

    public static void updateStatisticMaxIfTransactionIsGreater(Statistics statistic, Transaction transaction) {
        if (transaction.getAmount().compareTo(statistic.getMax()) > 0) {
            statistic.setMax(transaction.getAmount());
        }
    }
}
