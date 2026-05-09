package com.narxoz.rpg;

import com.narxoz.rpg.combatant.Hero;
import com.narxoz.rpg.council.CouncilEngine;
import com.narxoz.rpg.council.CouncilRunResult;
import com.narxoz.rpg.guild.Captain;
import com.narxoz.rpg.guild.GuildHall;
import com.narxoz.rpg.guild.Healer;
import com.narxoz.rpg.guild.Loremaster;
import com.narxoz.rpg.guild.Quartermaster;
import com.narxoz.rpg.guild.Scout;
import com.narxoz.rpg.quest.Quest;
import com.narxoz.rpg.quest.QuestIterator;
import com.narxoz.rpg.quest.QuestLog;
import com.narxoz.rpg.quest.QuestPriority;
import java.util.List;

/**
 * Entry point for Homework 10: The Adventurers' Guild, Iterator + Mediator.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Homework 10 Demo: Iterator + Mediator ===");

        List<Hero> party = List.of(
                new Hero("Arin", 120, 35, 18, 12, 60),
                new Hero("Mira", 85, 70, 22, 8, 120));
        printParty(party);

        QuestLog questLog = buildQuestLog();
        System.out.println();
        System.out.println("Quest log size: " + questLog.size());
        printTraversal("Arrival order", questLog.ordered());
        printTraversal("Reverse arrival", questLog.reverse());
        printTraversal("Priority at least HIGH", questLog.priorityAtLeast(QuestPriority.HIGH));
        printTraversal("Reward sorted", questLog.rewardSorted());

        System.out.println();
        System.out.println("=== Guild Hall Setup ===");
        GuildHall hall = new GuildHall();
        Quartermaster quartermaster = new Quartermaster("Tamsin", hall);
        Scout scout = new Scout("Rook", hall);
        Healer healer = new Healer("Sera", hall);
        Captain captain = new Captain("Orin", hall);
        Loremaster loremaster = new Loremaster("Ilya", hall);

        System.out.println();
        System.out.println("=== Mediator Warm-Up ===");
        captain.issueOrder("Form two teams and keep all reports inside the guild hall.");
        scout.reportRoute("Northern road is clear, but the old bridge is unstable.");
        healer.prepareAid("Pack antidotes and two field stretchers.");
        quartermaster.requestSupplies("Reserve climbing kits, silver bolts, and emergency rations.");
        loremaster.shareLore("The Sunken Shrine curse repeats every seventh bell.");

        CouncilRunResult result = new CouncilEngine().runCouncil(party, questLog, hall);
        System.out.println();
        System.out.println("Final council result: " + result);
    }

    private static QuestLog buildQuestLog() {
        QuestLog questLog = new QuestLog();
        questLog.add(new Quest("Escort the moon caravan", QuestPriority.NORMAL, 180, false));
        questLog.add(new Quest("Clear the ash goblin camp", QuestPriority.HIGH, 420, false));
        questLog.add(new Quest("Seal the Sunken Shrine", QuestPriority.URGENT, 900, true));
        questLog.add(new Quest("Recover the lost herb satchel", QuestPriority.LOW, 75, false));
        questLog.add(new Quest("Guard the duke's archive", QuestPriority.HIGH, 520, false));
        questLog.add(new Quest("Stop the midnight wyvern", QuestPriority.URGENT, 1200, true));
        return questLog;
    }

    private static void printParty(List<Hero> party) {
        System.out.println();
        System.out.println("Party:");
        for (Hero hero : party) {
            System.out.println("  " + hero);
        }
    }

    private static void printTraversal(String title, QuestIterator iterator) {
        System.out.println();
        System.out.println("[Iterator Demo] " + title);
        int index = 1;
        while (iterator.hasNext()) {
            System.out.println("  " + index + ". " + iterator.next());
            index++;
        }
    }
}
