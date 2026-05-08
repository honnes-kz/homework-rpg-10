package com.narxoz.rpg.quest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Traverses quests from highest reward to lowest reward.
 */
public class RewardSortedQuestIterator implements QuestIterator {

    private final List<Quest> snapshot;
    private int cursor;

    public RewardSortedQuestIterator(QuestLog questLog) {
        this.snapshot = new ArrayList<>(questLog.snapshot());
        this.snapshot.sort(
                Comparator.comparingInt(Quest::getRewardGold)
                        .reversed()
                        .thenComparing(Quest::getTitle));
        this.cursor = 0;
    }

    @Override
    public boolean hasNext() {
        return cursor < snapshot.size();
    }

    @Override
    public Quest next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more quests by reward amount.");
        }
        Quest quest = snapshot.get(cursor);
        cursor++;
        return quest;
    }
}
