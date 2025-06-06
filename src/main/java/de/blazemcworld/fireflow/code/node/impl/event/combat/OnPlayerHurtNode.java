package de.blazemcworld.fireflow.code.node.impl.event.combat;

import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.type.StringType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerHurtNode extends Node {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Double> amount;
    private final Output<String> type;

    public OnPlayerHurtNode() {
        super("on_player_hurt", "On Player Hurt", "Emits a signal when a player is about to take damage.", Items.REDSTONE);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        amount = new Output<>("amount", "Damage Amount", NumberType.INSTANCE);
        type = new Output<>("type", "Damage Type", StringType.INSTANCE);
        player.valueFromScope();
        amount.valueFromScope();
        type.valueFromScope();
    }

    public void onPlayerHurt(CodeEvaluator codeEvaluator, ServerPlayerEntity player, float damage, String type, CodeThread.EventContext ctx) {
        CodeThread thread = codeEvaluator.newCodeThread();
        thread.context = ctx;
        thread.setScopeValue(this.player, new PlayerValue(player));
        thread.setScopeValue(this.amount, (double) damage);
        thread.setScopeValue(this.type, type);
        thread.sendSignal(signal);
        thread.clearQueue();
    }

    @Override
    public Node copy() {
        return new OnPlayerHurtNode();
    }
}

