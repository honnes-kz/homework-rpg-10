package com.narxoz.rpg.council;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.guild.GuildHall;
import com.narxoz.rpg.guild.GuildMediator;
import com.narxoz.rpg.quest.Quest;
import com.narxoz.rpg.quest.QuestIterator;
import com.narxoz.rpg.quest.QuestLog;
import com.narxoz.rpg.quest.QuestPriority;
import java.util.List;

/**
 * Orchestrates a planning session that uses both Iterator and Mediator.
 */
public class CouncilEngine {

    public CouncilRunResult runCouncil(List<Hero> party, QuestLog questLog, GuildMediator hall) {
        if (party == null || questLog == null || hall == null) {
            throw new IllegalArgumentException("Council run requires a party, quest log, and mediator.");
        }

        if (hall instanceof GuildHall) {
            ((GuildHall) hall).resetStatistics();
        }

        int questsTraversed = 0;
        int messagesRouted = 0;
        int membersNotified = 0;

        System.out.println();
        System.out.println("=== War Council Engine ===");
        System.out.println("[Council] " + describeParty(party));

        QuestIterator ordered = questLog.ordered();
        int slot = 1;
        while (ordered.hasNext()) {
            Quest quest = ordered.next();
            questsTraversed++;
            System.out.println("[Iterator: ordered] Slot " + slot + " -> " + quest);

            String topic = quest.isUrgent() || quest.getPriority() == QuestPriority.URGENT
                    ? GuildHall.TOPIC_URGENT
                    : GuildHall.TOPIC_ORDERS;
            membersNotified += dispatchAndCount(hall, topic,
                    "Plan slot " + slot + ": " + quest.getTitle()
                            + " (" + quest.getPriority() + ", " + quest.getRewardGold() + " gold)");
            messagesRouted++;

            if (quest.getRewardGold() >= 500) {
                membersNotified += dispatchAndCount(hall, GuildHall.TOPIC_REWARDS,
                        quest.getTitle() + " offers a high reward of "
                                + quest.getRewardGold() + " gold.");
                messagesRouted++;
            }
            slot++;
        }

        QuestIterator highPriority = questLog.priorityAtLeast(QuestPriority.HIGH);
        while (highPriority.hasNext()) {
            Quest quest = highPriority.next();
            questsTraversed++;
            System.out.println("[Iterator: priority >= HIGH] Threat review -> " + quest);

            membersNotified += dispatchAndCount(hall, GuildHall.TOPIC_SCOUTING,
                    "Scout the approach for " + quest.getTitle() + ".");
            messagesRouted++;

            membersNotified += dispatchAndCount(hall, GuildHall.TOPIC_HEALING,
                    "Prepare recovery support for " + quest.getTitle() + ".");
            messagesRouted++;
        }

        return new CouncilRunResult(questsTraversed, messagesRouted, membersNotified);
    }

    private int dispatchAndCount(GuildMediator hall, String topic, String payload) {
        hall.dispatch(topic, null, payload);
        if (hall instanceof GuildHall) {
            return ((GuildHall) hall).getLastDispatchNotificationCount();
        }
        return 0;
    }

    private String describeParty(List<Hero> party) {
        int totalHp = 0;
        int totalAttack = 0;
        int totalDefense = 0;
        for (Hero hero : party) {
            totalHp += hero.getHp();
            totalAttack += hero.getAttackPower();
            totalDefense += hero.getDefense();
        }
        return "Party size=" + party.size()
                + ", totalHp=" + totalHp
                + ", totalAttack=" + totalAttack
                + ", totalDefense=" + totalDefense;
    }
}
