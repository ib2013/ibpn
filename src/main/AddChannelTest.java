package main;

public class AddChannelTest {

	public static void main(String[] args) {
		ChannelHandler channelHandler = new ChannelHandler();
		ChannelModel channelModel = new ChannelModel("Ekipa", "testiranje dodavanaj kanala");
		channelHandler.addChannel(channelModel);

	}

}
