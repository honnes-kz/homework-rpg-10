package com.narxoz.rpg.guild;

/**
 * Guild officer responsible for gear, supplies, and rewards.
 */
public class Quartermaster extends GuildMember {

    public Quartermaster(String name, GuildMediator mediator) {
        super(name, mediator);
    }

    public void requestSupplies(String payload) {
        getMediator().dispatch(GuildHall.TOPIC_SUPPLIES, this, payload);
    }

    @Override
    public void receive(String topic, GuildMember from, String payload) {
        String sender = senderName(from);
        if (GuildHall.TOPIC_ORDERS.equals(topic)) {
            System.out.println("[Quartermaster " + getName() + "] equips the party for "
                    + sender + "'s order: " + payload);
        } else if (GuildHall.TOPIC_SCOUTING.equals(topic)) {
            System.out.println("[Quartermaster " + getName() + "] checks ropes, maps, and torches after "
                    + sender + "'s route report: " + payload);
        } else if (GuildHall.TOPIC_URGENT.equals(topic)) {
            System.out.println("[Quartermaster " + getName() + "] opens the emergency armory: "
                    + payload);
        } else if (GuildHall.TOPIC_REWARDS.equals(topic)) {
            System.out.println("[Quartermaster " + getName() + "] prepares the reward ledger: "
                    + payload);
        } else {
            System.out.println("[Quartermaster " + getName() + "] logs " + topic + " from "
                    + sender + ": " + payload);
        }
    }
}
