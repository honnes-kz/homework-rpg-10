package com.narxoz.rpg.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Topic-based mediator for the Adventurers' Guild war council.
 */
public class GuildHall implements GuildMediator {

    public static final String TOPIC_ORDERS = "orders";
    public static final String TOPIC_SUPPLIES = "supplies";
    public static final String TOPIC_SCOUTING = "scouting";
    public static final String TOPIC_HEALING = "healing";
    public static final String TOPIC_URGENT = "urgent";
    public static final String TOPIC_REWARDS = "rewards";
    public static final String TOPIC_LORE = "lore";

    private final Map<String, List<GuildMember>> membersByTopic = new HashMap<>();
    private int lastDispatchNotificationCount;
    private int totalMessagesRouted;
    private int totalNotifications;

    @Override
    public void register(GuildMember member) {
        if (member == null) {
            return;
        }

        if (member instanceof Captain) {
            addSubscriber(TOPIC_SCOUTING, member);
            addSubscriber(TOPIC_SUPPLIES, member);
            addSubscriber(TOPIC_HEALING, member);
            addSubscriber(TOPIC_URGENT, member);
            addSubscriber(TOPIC_REWARDS, member);
            addSubscriber(TOPIC_LORE, member);
        } else if (member instanceof Quartermaster) {
            addSubscriber(TOPIC_ORDERS, member);
            addSubscriber(TOPIC_SCOUTING, member);
            addSubscriber(TOPIC_URGENT, member);
            addSubscriber(TOPIC_REWARDS, member);
        } else if (member instanceof Scout) {
            addSubscriber(TOPIC_ORDERS, member);
            addSubscriber(TOPIC_SUPPLIES, member);
            addSubscriber(TOPIC_URGENT, member);
            addSubscriber(TOPIC_LORE, member);
        } else if (member instanceof Healer) {
            addSubscriber(TOPIC_ORDERS, member);
            addSubscriber(TOPIC_SCOUTING, member);
            addSubscriber(TOPIC_SUPPLIES, member);
            addSubscriber(TOPIC_URGENT, member);
        } else if (member instanceof Loremaster) {
            addSubscriber(TOPIC_ORDERS, member);
            addSubscriber(TOPIC_SCOUTING, member);
            addSubscriber(TOPIC_URGENT, member);
            addSubscriber(TOPIC_LORE, member);
        } else {
            addSubscriber(TOPIC_ORDERS, member);
        }

        System.out.println("[GuildHall] Registered " + member.getClass().getSimpleName()
                + " " + member.getName());
    }

    @Override
    public void dispatch(String topic, GuildMember from, String payload) {
        lastDispatchNotificationCount = 0;
        totalMessagesRouted++;

        String source = from == null ? "War Council" : from.getName();
        List<GuildMember> subscribers = subscribersFor(topic);
        System.out.println("[GuildHall] " + source + " dispatches '" + topic + "': " + payload);

        for (GuildMember member : subscribers) {
            if (member == from) {
                continue;
            }
            member.receive(topic, from, payload);
            lastDispatchNotificationCount++;
        }

        totalNotifications += lastDispatchNotificationCount;
        System.out.println("[GuildHall] Routed to " + lastDispatchNotificationCount + " member(s).");
    }

    protected void addSubscriber(String topic, GuildMember member) {
        List<GuildMember> subscribers = membersByTopic.computeIfAbsent(topic, key -> new ArrayList<>());
        if (!subscribers.contains(member)) {
            subscribers.add(member);
        }
    }

    protected List<GuildMember> subscribersFor(String topic) {
        return membersByTopic.getOrDefault(topic, List.of());
    }

    public int getLastDispatchNotificationCount() {
        return lastDispatchNotificationCount;
    }

    public int getTotalMessagesRouted() {
        return totalMessagesRouted;
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }

    public void resetStatistics() {
        lastDispatchNotificationCount = 0;
        totalMessagesRouted = 0;
        totalNotifications = 0;
    }
}
