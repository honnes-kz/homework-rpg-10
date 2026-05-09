package com.narxoz.rpg.guild;

/**
 * Guild officer responsible for wounds, potions, and recovery plans.
 */
public class Healer extends GuildMember {

    public Healer(String name, GuildMediator mediator) {
        super(name, mediator);
    }

    public void prepareAid(String payload) {
        getMediator().dispatch(GuildHall.TOPIC_HEALING, this, payload);
    }

    @Override
    public void receive(String topic, GuildMember from, String payload) {
        String sender = senderName(from);
        if (GuildHall.TOPIC_ORDERS.equals(topic)) {
            System.out.println("[Healer " + getName() + "] prepares triage for "
                    + sender + "'s order: " + payload);
        } else if (GuildHall.TOPIC_SCOUTING.equals(topic)) {
            System.out.println("[Healer " + getName() + "] chooses antidotes for the route: "
                    + payload);
        } else if (GuildHall.TOPIC_SUPPLIES.equals(topic)) {
            System.out.println("[Healer " + getName() + "] reserves potions and bandages: "
                    + payload);
        } else if (GuildHall.TOPIC_URGENT.equals(topic)) {
            System.out.println("[Healer " + getName() + "] readies emergency healing circles: "
                    + payload);
        } else {
            System.out.println("[Healer " + getName() + "] records " + topic + " from "
                    + sender + ": " + payload);
        }
    }
}
