package com.narxoz.rpg.guild;

/**
 * Guild officer responsible for route reports and reconnaissance.
 */
public class Scout extends GuildMember {

    public Scout(String name, GuildMediator mediator) {
        super(name, mediator);
    }

    public void reportRoute(String payload) {
        getMediator().dispatch(GuildHall.TOPIC_SCOUTING, this, payload);
    }

    @Override
    public void receive(String topic, GuildMember from, String payload) {
        String sender = senderName(from);
        if (GuildHall.TOPIC_ORDERS.equals(topic)) {
            System.out.println("[Scout " + getName() + "] marks a route for "
                    + sender + "'s order: " + payload);
        } else if (GuildHall.TOPIC_SUPPLIES.equals(topic)) {
            System.out.println("[Scout " + getName() + "] adjusts the field kit request: "
                    + payload);
        } else if (GuildHall.TOPIC_URGENT.equals(topic)) {
            System.out.println("[Scout " + getName() + "] sends runners to verify the threat: "
                    + payload);
        } else if (GuildHall.TOPIC_LORE.equals(topic)) {
            System.out.println("[Scout " + getName() + "] adds the lore warning to the route map: "
                    + payload);
        } else {
            System.out.println("[Scout " + getName() + "] notes " + topic + " from "
                    + sender + ": " + payload);
        }
    }
}
