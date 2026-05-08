package com.narxoz.rpg.guild;

/**
 * Base class for all guild officers that communicate through a mediator.
 */
public abstract class GuildMember {

    private final String name;
    private final GuildMediator mediator;

    protected GuildMember(String name, GuildMediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException("Guild member requires a mediator.");
        }
        this.name = name;
        this.mediator = mediator;
        mediator.register(this);
    }

    public String getName() {
        return name;
    }

    protected GuildMediator getMediator() {
        return mediator;
    }

    protected String senderName(GuildMember from) {
        return from == null ? "War Council" : from.getName();
    }

    public abstract void receive(String topic, GuildMember from, String payload);
}
