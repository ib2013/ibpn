package dbmodels;

public class RssPopisModel {

	private int id;
	private String rssFeed;
	private String opis;
	private int idRssSource;

	public RssPopisModel() {

	}

	public RssPopisModel(String rssFeed, String opis, int idRssSource) {
		this.rssFeed = rssFeed;
		this.opis = opis;
		this.idRssSource = idRssSource;
	}

	public RssPopisModel(int id, String rssFeed, String opis, int idRssSource) {
		this.id = id;
		this.rssFeed = rssFeed;
		this.opis = opis;
		this.idRssSource = idRssSource;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRssFeed() {
		return rssFeed;
	}

	public void setRssFeed(String rssFeed) {
		this.rssFeed = rssFeed;
	}

	public String getOpis() {
		return opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getIdRssSource() {
		return idRssSource;
	}

	public void setIdRssSource(int idRssSource) {
		this.idRssSource = idRssSource;
	}

	@Override
	public String toString() {
		return "RssPopisModel [id=" + id + ", rssFeed=" + rssFeed + ", opis="
				+ opis + ", idRssSource=" + idRssSource + "]";
	}

}
