package de.blazemcworld.fireflow.code.node.impl.event;

import de.blazemcworld.fireflow.code.CodeEvaluator;
import de.blazemcworld.fireflow.code.CodeThread;
import de.blazemcworld.fireflow.code.node.Node;
import de.blazemcworld.fireflow.code.type.NumberType;
import de.blazemcworld.fireflow.code.type.PlayerType;
import de.blazemcworld.fireflow.code.type.SignalType;
import de.blazemcworld.fireflow.code.value.PlayerValue;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

public class OnPlayerLoseFoodNode extends Node {

    private final Output<Void> signal;
    private final Output<PlayerValue> player;
    private final Output<Double> oldValue;
    private final Output<Double> newValue;

    public OnPlayerLoseFoodNode() {
        super("on_player_lose_food", "On Player Lose Food", "Emits a signal when a player loses some food points.", Items.ROTTEN_FLESH);

        signal = new Output<>("signal", "Signal", SignalType.INSTANCE);
        player = new Output<>("player", "Player", PlayerType.INSTANCE);
        oldValue = new Output<>("old_value", "Old Value", NumberType.INSTANCE);
        newValue = new Output<>("new_value", "New Value", NumberType.INSTANCE);
        player.valueFromScope();
        oldValue.valueFromScope();
        newValue.valueFromScope();
    }

    @Override
    public Node copy() {
        return new OnPlayerLoseFoodNode();
    }

    public void emit(CodeEvaluator evaluator, ServerPlayerEntity player, int oldValue, int newValue, CodeThread.EventContext context) {
        CodeThread thread = evaluator.newCodeThread();
        thread.context = context;
        thread.setScopeValue(this.player, new PlayerValue(player));
        thread.setScopeValue(this.oldValue, (double) oldValue);
        thread.setScopeValue(this.newValue, (double) newValue);
        thread.sendSignal(signal);
        thread.clearQueue();
    }
}
