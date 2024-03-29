package jarvis.device;


public interface ControlMessageBuilder<M> {

    public M buildMessage(ControlledDevice device, ControlMessageType type);
}
