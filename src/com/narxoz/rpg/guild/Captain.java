package com.narxoz.rpg.guild;

/**
 * Guild officer responsible for orders and mission coordination.
 */
public class Captain extends GuildMember {

    public Captain(String name, GuildMediator mediator) {
        super(name, mediator);
    }

    public void issueOrder(String payload) {
        getMediator().dispatch(GuildHall.TOPIC_ORDERS, this, payload);
    }

    @Override
    public void receive(String topic, GuildMember from, String payload) {
        String sender = senderName(from);
        if (GuildHall.TOPIC_SCOUTING.equals(topic)) {
            System.out.println("[Captain " + getName() + "] updates the battle plan from "
                    + sender + "'s scouting report: " + payload);
        } else if (GuildHall.TOPIC_SUPPLIES.equals(topic)) {
            System.out.println("[Captain " + getName() + "] approves the supply plan from "
                    + sender + ": " + payload);
        } else if (GuildHall.TOPIC_HEALING.equals(topic)) {
            System.out.println("[Captain " + getName() + "] schedules recovery support from "
                    + sender + ": " + payload);
        } else if (GuildHall.TOPIC_URGENT.equals(topic)) {
            System.out.println("[Captain " + getName() + "] calls for immediate readiness: "
                    + payload);
        } else if (GuildHall.TOPIC_REWARDS.equals(topic)) {
            System.out.println("[Captain " + getName() + "] weighs the reward against the risk: "
                    + payload);
        } else if (GuildHall.TOPIC_LORE.equals(topic)) {
            System.out.println("[Captain " + getName() + "] adds historical context to the briefing: "
                    + payload);
        } else {
            System.out.println("[Captain " + getName() + "] receives " + topic + " from "
                    + sender + ": " + payload);
        }
    }
}
