package com.narxoz.rpg.guild;

/**
 * Guild officer responsible for curses, history, and old campaign records.
 */
public class Loremaster extends GuildMember {

    public Loremaster(String name, GuildMediator mediator) {
        super(name, mediator);
    }

    public void shareLore(String payload) {
        getMediator().dispatch(GuildHall.TOPIC_LORE, this, payload);
    }

    @Override
    public void receive(String topic, GuildMember from, String payload) {
        String sender = senderName(from);
        if (GuildHall.TOPIC_ORDERS.equals(topic)) {
            System.out.println("[Loremaster " + getName() + "] searches the archives for "
                    + sender + "'s order: " + payload);
        } else if (GuildHall.TOPIC_SCOUTING.equals(topic)) {
            System.out.println("[Loremaster " + getName() + "] compares the route with old maps: "
                    + payload);
        } else if (GuildHall.TOPIC_URGENT.equals(topic)) {
            System.out.println("[Loremaster " + getName() + "] checks curse records for the emergency: "
                    + payload);
        } else if (GuildHall.TOPIC_LORE.equals(topic)) {
            System.out.println("[Loremaster " + getName() + "] annotates the guild chronicle from "
                    + sender + ": " + payload);
        } else {
            System.out.println("[Loremaster " + getName() + "] files " + topic + " from "
                    + sender + ": " + payload);
        }
    }
}
