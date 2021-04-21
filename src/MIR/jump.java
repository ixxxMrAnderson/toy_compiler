package MIR;

public class jump extends statement {
    public block destination;
    public jump(block destination) {
        super();
        this.destination = destination;
    }
}
