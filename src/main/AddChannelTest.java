package main;

public class AddChannelTest {

	public static void main(String[] args) {
		ChannelHandler channelHandler = new ChannelHandler();
		ChannelModel channelModel = new ChannelModel("Elmir test", "testiranje dodavanaj kanala");
		channelHandler.addChannel(channelModel);

	}

}
